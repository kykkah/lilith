package de.huxhorn.lilith.services.clipboard;

import static org.junit.jupiter.api.Assertions.assertNull;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LoggingEventJsonFormatterTest extends AbstractClipboardFormatterTest {

    private static final String NEWLINE = System.lineSeparator();

    @Override
    protected LoggingEventJsonFormatter createInstance() {
        return new LoggingEventJsonFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return EventWrapperCorpus.matchAnyLoggingEventSet();
    }

    @Override
    protected Set<Integer> excludedIndices() {
        return Set.of(88, 93, 94, 99, 121);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                normalize("{ }"),
                normalize("{ }"),
                normalize("{\n  \"level\" : \"TRACE\"\n}"),
                normalize("{\n  \"level\" : \"DEBUG\"\n}"),
                normalize("{\n  \"level\" : \"INFO\"\n}"),
                normalize("{\n  \"level\" : \"WARN\"\n}"),
                normalize("{\n  \"level\" : \"ERROR\"\n}"),
                normalize("{\n  \"logger\" : \"com.foo.Foo\"\n}"),
                normalize("{\n  \"logger\" : \"com.foo.Bar\"\n}"),
                normalize("{\n  \"message\" : { }\n}"),
                normalize("{\n  \"message\" : { }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"a message.\"\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"another message.\"\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"a message with parameter {}.\",\n    \"arguments\" : [ \"paramValue\" ]\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"a message with unresolved parameter {}.\"\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"a message with parameter {} and unresolved parameter {}.\",\n    \"arguments\" : [ \"paramValue\" ]\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"{}\",\n    \"arguments\" : [ \"paramValue\" ]\n  }\n}"),
                normalize("{\n  \"message\" : {\n    \"messagePattern\" : \"{}\"\n  }\n}"),
                normalize("{\n  \"mdc\" : {\n    \"mdcKey\" : \"mdcValue\"\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0,\n    \"cause\" : {\n      \"name\" : \"java.lang.NullPointerException\",\n      \"omittedElements\" : 0\n    }\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0,\n    \"cause\" : {\n      \"name\" : \"java.lang.NullPointerException\",\n      \"omittedElements\" : 0,\n      \"cause\" : {\n        \"name\" : \"java.lang.FooException\",\n        \"omittedElements\" : 0\n      }\n    }\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0,\n    \"suppressed\" : [ {\n      \"name\" : \"java.lang.NullPointerException\",\n      \"omittedElements\" : 0\n    } ]\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0,\n    \"suppressed\" : [ {\n      \"name\" : \"java.lang.NullPointerException\",\n      \"omittedElements\" : 0\n    }, {\n      \"name\" : \"java.lang.FooException\",\n      \"omittedElements\" : 0\n    } ]\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"java.lang.RuntimeException\",\n    \"omittedElements\" : 0,\n    \"suppressed\" : [ {\n      \"name\" : \"java.lang.NullPointerException\",\n      \"omittedElements\" : 0\n    }, {\n      \"name\" : \"java.lang.FooException\",\n      \"omittedElements\" : 0\n    } ],\n    \"cause\" : {\n      \"name\" : \"java.lang.BarException\",\n      \"omittedElements\" : 0\n    }\n  }\n}"),
                normalize("{\n  \"marker\" : {\n    \"name\" : \"Foo-Marker\",\n    \"references\" : {\n      \"Bar-Marker\" : {\n        \"name\" : \"Bar-Marker\"\n      }\n    }\n  }\n}"),
                normalize("{\n  \"marker\" : {\n    \"name\" : \"Bar-Marker\"\n  }\n}"),
                normalize("{\n  \"ndc\" : [ ]\n}"),
                normalize("{\n  \"ndc\" : [ { } ]\n}"),
                normalize("{\n  \"ndc\" : [ { } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"a message.\"\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"another message.\"\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"a message with parameter {}.\",\n    \"arguments\" : [ \"paramValue\" ]\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"a message with unresolved parameter {}.\"\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"a message with parameter {} and unresolved parameter {}.\",\n    \"arguments\" : [ \"paramValue\" ]\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"{}\",\n    \"arguments\" : [ \"paramValue\" ]\n  } ]\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"{}\"\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"codeLocation\" : \"de.huxhorn.lilith-8.1.0-SNAPSHOT.jar\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"codeLocation\" : \"de.huxhorn.lilith-8.1.0-SNAPSHOT.jar\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.plaf.basic.BasicButtonListener\",\n    \"methodName\" : \"mouseReleased\",\n    \"fileName\" : \"BasicButtonListener.java\",\n    \"lineNumber\" : 252,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.plaf.basic.BasicButtonListener\",\n    \"methodName\" : \"mouseReleased\",\n    \"fileName\" : \"BasicButtonListener.java\",\n    \"lineNumber\" : 252,\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.plaf.basic.BasicButtonListener\",\n    \"methodName\" : \"mouseReleased\",\n    \"fileName\" : \"BasicButtonListener.java\",\n    \"lineNumber\" : 252,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.plaf.basic.BasicButtonListener\",\n    \"methodName\" : \"mouseReleased\",\n    \"fileName\" : \"BasicButtonListener.java\",\n    \"lineNumber\" : 252,\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ null, {\n    \"className\" : \"javax.swing.AbstractButton\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2022,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"callStack\" : [ {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"codeLocation\" : \"de.huxhorn.lilith-8.1.0-SNAPSHOT.jar\",\n    \"exact\" : false\n  }, null, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"mdc\" : { }\n}"),
                normalize("{\n  \"mdc\" : {\n    \"mdcKey\" : \"otherMdcValue\"\n  }\n}"),
                normalize("{\n  \"mdc\" : {\n    \"mdcKey\" : null\n  }\n}"),
                normalize("{\n  \"loggerContext\" : {\n    \"name\" : \"loggerContextName\"\n  }\n}"),
                normalize("{\n  \"loggerContext\" : {\n    \"properties\" : { }\n  }\n}"),
                normalize("{\n  \"loggerContext\" : {\n    \"properties\" : {\n      \"loggerContextKey\" : \"loggerContextValue\"\n    }\n  }\n}"),
                normalize("{\n  \"threadInfo\" : { }\n}"),
                normalize("{\n  \"threadInfo\" : {\n    \"name\" : \"threadName\"\n  }\n}"),
                normalize("{\n  \"threadInfo\" : {\n    \"id\" : 11337\n  }\n}"),
                normalize("{\n  \"threadInfo\" : {\n    \"groupName\" : \"groupName\"\n  }\n}"),
                normalize("{\n  \"threadInfo\" : {\n    \"groupId\" : 31337\n  }\n}"),
                normalize("{\n  \"ndc\" : [ {\n    \"messagePattern\" : \"b0rked1\"\n  }, null, {\n    \"messagePattern\" : \"b0rked3\"\n  } ]\n}"),
                normalize("{\n  \"throwable\" : {\n    \"message\" : \"exception1\",\n    \"omittedElements\" : 0\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"omittedElements\" : 0,\n    \"cause\" : {\n      \"message\" : \"exception2\",\n      \"omittedElements\" : 0\n    }\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"omittedElements\" : 0,\n    \"suppressed\" : [ {\n      \"message\" : \"exception3\",\n      \"omittedElements\" : 0\n    } ]\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"omittedElements\" : 0,\n    \"suppressed\" : [ {\n      \"message\" : \"exception4\",\n      \"omittedElements\" : 0\n    }, null, {\n      \"message\" : \"exception5\",\n      \"omittedElements\" : 0\n    } ]\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"stackTrace\" : [ null, {\n      \"className\" : \"javax.swing.AbstractButton\",\n      \"methodName\" : \"fireActionPerformed\",\n      \"fileName\" : \"AbstractButton.java\",\n      \"lineNumber\" : 2022,\n      \"version\" : \"1.8.0_92\",\n      \"exact\" : false\n    }, {\n      \"className\" : \"javax.swing.AbstractButton$Handler\",\n      \"methodName\" : \"actionPerformed\",\n      \"fileName\" : \"AbstractButton.java\",\n      \"lineNumber\" : 2348,\n      \"version\" : \"1.8.0_92\",\n      \"exact\" : false\n    }, {\n      \"className\" : \"javax.swing.DefaultButtonModel\",\n      \"methodName\" : \"fireActionPerformed\",\n      \"fileName\" : \"DefaultButtonModel.java\",\n      \"lineNumber\" : 402,\n      \"version\" : \"1.8.0_92\",\n      \"exact\" : false\n    }, {\n      \"className\" : \"javax.swing.DefaultButtonModel\",\n      \"methodName\" : \"setPressed\",\n      \"fileName\" : \"DefaultButtonModel.java\",\n      \"lineNumber\" : 259,\n      \"version\" : \"1.8.0_92\",\n      \"exact\" : false\n    } ],\n    \"omittedElements\" : 0\n  }\n}"),
                normalize("{\n  \"throwable\" : {\n    \"omittedElements\" : 0,\n    \"cause\" : {\n      \"stackTrace\" : [ {\n        \"className\" : \"javax.swing.AbstractButton\",\n        \"methodName\" : \"fireActionPerformed\",\n        \"fileName\" : \"AbstractButton.java\",\n        \"lineNumber\" : 2022,\n        \"version\" : \"1.8.0_92\",\n        \"exact\" : false\n      }, null, {\n        \"className\" : \"javax.swing.DefaultButtonModel\",\n        \"methodName\" : \"fireActionPerformed\",\n        \"fileName\" : \"DefaultButtonModel.java\",\n        \"lineNumber\" : 402,\n        \"version\" : \"1.8.0_92\",\n        \"exact\" : false\n      }, {\n        \"className\" : \"javax.swing.DefaultButtonModel\",\n        \"methodName\" : \"setPressed\",\n        \"fileName\" : \"DefaultButtonModel.java\",\n        \"lineNumber\" : 259,\n        \"version\" : \"1.8.0_92\",\n        \"exact\" : false\n      } ],\n      \"omittedElements\" : 0\n    }\n  }\n}"),
                normalize("{\n  \"mdc\" : { }\n}"),
                normalize("{\n  \"mdc\" : {\n    \"nullMdcValueKey\" : null\n  }\n}"),
                normalize("{\n  \"logger\" : \"\"\n}"),
                normalize("{\n  \"throwable\" : {\n    \"name\" : \"\",\n    \"omittedElements\" : 0\n  }\n}"),
                normalize("{\n  \"callStack\" : [ null, {\n    \"className\" : \"de.huxhorn.lilith.debug.DebugDialog$LogAllAction\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"DebugDialog.java\",\n    \"lineNumber\" : 358,\n    \"codeLocation\" : \"de.huxhorn.lilith-8.1.0-SNAPSHOT.jar\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.AbstractButton$Handler\",\n    \"methodName\" : \"actionPerformed\",\n    \"fileName\" : \"AbstractButton.java\",\n    \"lineNumber\" : 2348,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"fireActionPerformed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 402,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  }, {\n    \"className\" : \"javax.swing.DefaultButtonModel\",\n    \"methodName\" : \"setPressed\",\n    \"fileName\" : \"DefaultButtonModel.java\",\n    \"lineNumber\" : 259,\n    \"version\" : \"1.8.0_92\",\n    \"exact\" : false\n  } ]\n}"),
                normalize("{\n  \"threadInfo\" : {\n    \"priority\" : 7\n  }\n}")
        );
    }

    @Test
    void explodingFormatterReturnsNull() {
        @SuppressWarnings("unchecked")
        List<EventWrapper<?>> corpus = (List<EventWrapper<?>>) (List<?>) EventWrapperCorpus.createCorpus();
        ClipboardFormatter formatter = createInstance();
        EventWrapper<?> exploding = corpus.get(88);
        assertNull(formatter.toString(exploding));
    }

    private static String normalize(String value) {
        return value.replace("\n", NEWLINE);
    }
}
