**CLI Entry Point**
- `java -cp <lilith-classpath> de.huxhorn.lilith.benchmark.Benchmark [options]` runs the standalone benchmark. The project ships only source, so build the classpath yourself (e.g. compile with `javac` or wire it into an IDE run config); the class expects Lilith’s data/engine and sulky codecs on the classpath.

**Available Flags**
- `-gc` – requests a forced `System.gc()` and 1 s sleep between every benchmark phase so you can profile without generational noise.
- `-xml` – enables the XML codec benchmarks (Lilith XML, JavaBeans XML, and the associated file-buffer add/get tests). Without it the run is protobuf + Java serialization only.
- `-nc` – strips call stacks from the 2 000 synthetic events that get generated before the warm-up loops. Handy for isolating payload-size effects.
- `-ne` – strips throwable payloads from those same events, again to study the encoding overhead independently.

**Invocation Matrix**
- No flags: vanilla run with protobuf + Java serialization, full call stacks, and exceptions.
- `-gc`: vanilla run with forced GC pauses.
- `-xml`: vanilla run plus XML encoders/decoders and file-buffer tests.
- `-nc`: vanilla run with call stacks removed.
- `-ne`: vanilla run with exceptions removed.
- `-gc -xml`: XML suite enabled and GC pauses inserted.
- `-gc -nc`: GC pauses plus no call stacks.
- `-gc -ne`: GC pauses plus no exceptions.
- `-xml -nc`: XML suite enabled, no call stacks.
- `-xml -ne`: XML suite enabled, no exceptions.
- `-nc -ne`: no call stacks and no exceptions.
- `-gc -xml -nc`: XML suite, GC pauses, no call stacks.
- `-gc -xml -ne`: XML suite, GC pauses, no exceptions.
- `-gc -nc -ne`: GC pauses, no call stacks, no exceptions.
- `-xml -nc -ne`: XML suite, no call stacks, no exceptions.
- `-gc -xml -nc -ne`: everything enabled—XML benchmarks with GC pauses while both call stacks and exceptions are suppressed.

Each run performs three warm-up passes, checks that the VM was started with equal `-Xms/-Xmx` (it exits if not), then executes the “real” pass you see logged. Logs are emitted through SLF4J using markers (`TRACE/ENTERING/EXITING/THROWING`) and include a table of per-test durations and sizes once the final pass completes.
