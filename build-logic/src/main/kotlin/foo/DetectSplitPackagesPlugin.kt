package foo

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar
import java.io.File
import java.io.FileFilter
import java.util.StringTokenizer
import java.util.TreeSet
import java.util.LinkedHashSet
import java.util.SortedSet

class DetectSplitPackagesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("detectSplitPackages", DetectSplitPackagesTask::class.java)
        // Hook into check task only if required later; kept behaviour aligned with previous Groovy version.
    }
}

open class DetectSplitPackagesTask : DefaultTask() {

    companion object {
        private const val JAVA_FILE_SUFFIX = ".java"
        private const val HIDDEN_DIRECTORY_PREFIX = "."
    }

    @Input
    var projectsToScan: MutableSet<Project> = LinkedHashSet(project.subprojects)

    init {
        group = "Verification"
        description = "Detects packages split across two or more subprojects."
    }

    @TaskAction
    fun detectSplitPackages() {
        val splitPackages = doDetectSplitPackages()
        if (splitPackages.isNotEmpty()) {
            val message = buildString {
                appendLine("The following split package(s) have been detected:")
                splitPackages.forEach { (pkg, modules) ->
                    append(" - ").append(pkg)
                        .append(" (split across ").append(modules.joinToString(", ")).appendLine(")")
                }
            }
            throw GradleException(message)
        }

        val packagesByProject = mapPackagesByProject()
        val packageStepsByProject = PackageHelper.producePackageStepsByProject(packagesByProject)
        val projectModuleNames = mutableMapOf<Project, String>()
        val moduleNameProjects = mutableMapOf<String, MutableSet<Project>>()

        var correct = true
        packageStepsByProject.forEach { (proj, packageSteps) ->
            val commonSteps = PackageHelper.findCommonPrefix(packageSteps)
            val commonName = PackageHelper.toPackageName(commonSteps)
            projectModuleNames[proj] = commonName
            val projectsForModule = moduleNameProjects.getOrPut(commonName) { linkedSetOf() }
            projectsForModule += proj

            val jarTask = proj.tasks.findByName("jar") as? Jar
            val automaticModuleName = jarTask?.manifest?.attributes?.get("Automatic-Module-Name") as? String
            val archivesBaseName = resolveArchivesBaseName(proj)

            if (commonName != automaticModuleName) {
                correct = false
                if (commonName != archivesBaseName) {
                    logger.lifecycle("!!! {}\t{}\t{}", proj, archivesBaseName, commonName)
                } else {
                    logger.lifecycle("{}\t{}", proj, commonName)
                }
            }
        }

        val moduleConflictMessage = buildString {
            appendLine("The following module names have been suggested for multiple projects:")
            moduleNameProjects.filterValues { it.size > 1 }.forEach { (moduleName, projects) ->
                append(" - ").append(moduleName)
                    .append(" (suggested for ").append(projects.joinToString(", ")).appendLine(")")
            }
        }

        if (moduleNameProjects.values.any { it.size > 1 }) {
            throw GradleException(moduleConflictMessage)
        }

        if (correct) {
            logger.lifecycle("Everything is fine.")
        }
    }

    private fun doDetectSplitPackages(): Map<String, SortedSet<String>> {
        val splitPackages = mutableMapOf<String, SortedSet<String>>()
        val packagesByProject = mapPackagesByProject()

        packagesByProject.forEach { (project, packages) ->
            packages.forEach { packageDirectory ->
                val packageName = packageDirectory.substring(1).replace(File.separatorChar, '.')
                val projectSet = splitPackages.getOrPut(packageName) { TreeSet() }
                projectSet += project.name
            }
        }

        return splitPackages.filterValues { it.size > 1 }
    }

    private fun mapPackagesByProject(): Map<Project, MutableSet<String>> {
        val packagesByProject = linkedMapOf<Project, MutableSet<String>>()
        projectsToScan.forEach { targetProject ->
            val sourceSets = targetProject.extensions.findByType(SourceSetContainer::class.java) ?: return@forEach
            val packages = linkedSetOf<String>()
            sourceSets.findByName("main")?.java?.srcDirs?.forEach { dir ->
                findPackages(packages, dir, "")
            }
            if (packages.isNotEmpty()) {
                packagesByProject[targetProject] = packages
            }
        }
        return packagesByProject
    }

    private fun resolveArchivesBaseName(project: Project): String {
        val base = project.extensions.findByType(BasePluginExtension::class.java)
        val archivesNameProperty = base?.archivesName
        return archivesNameProperty?.getOrNull() ?: project.name
    }

    private fun findPackages(packages: MutableSet<String>, dir: File, packagePath: String) {
        val scanDir = File(dir, packagePath)
        val javaFiles = scanDir.listFiles(FileFilter { file ->
            !file.isDirectory && file.name.endsWith(JAVA_FILE_SUFFIX)
        })
        if (javaFiles != null && javaFiles.isNotEmpty()) {
            packages += packagePath
        }

        val directories = scanDir.listFiles(FileFilter { file ->
            file.isDirectory && !file.name.startsWith(HIDDEN_DIRECTORY_PREFIX)
        }) ?: emptyArray()

        directories.forEach { subDir ->
            val nextPath = packagePath + File.separator + subDir.name
            findPackages(packages, dir, nextPath)
        }
    }
}

private object PackageHelper {

    fun toPackageName(parts: List<String>): String =
        parts.joinToString(".")

    fun minimumSize(steps: Set<List<*>>): Int =
        steps.minOfOrNull { it.size } ?: 0

    private fun packageDirToPackageSteps(dir: String): List<String> {
        val tokenizer = StringTokenizer(dir, "/", false)
        val result = mutableListOf<String>()
        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken()
            if (token.isNotEmpty()) {
                result += token
            }
        }
        return result
    }

    fun packageDirsToPackageSteps(dirs: Set<String>): Set<List<String>> =
        dirs.mapTo(linkedSetOf()) { packageDirToPackageSteps(it) }

    fun producePackageStepsByProject(packagesByProject: Map<Project, Set<String>>): Map<Project, Set<List<String>>> =
        packagesByProject.mapValues { (_, packages) -> packageDirsToPackageSteps(packages) }

    fun findCommonPrefix(input: Set<List<String>>): List<String> {
        val upperBounds = minimumSize(input)
        if (upperBounds == 0 || input.isEmpty()) {
            return emptyList()
        }

        val result = mutableListOf<String>()
        for (index in 0 until upperBounds) {
            var currentValue: String? = null
            for (list in input) {
                val value = list[index]
                if (currentValue == null) {
                    currentValue = value
                } else if (currentValue != value) {
                    return result
                }
            }
            result += currentValue ?: return result
        }
        return result
    }
}
