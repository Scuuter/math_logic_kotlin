import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val beginTime = System.currentTimeMillis()
    val parser = Parser()
    initAxioms(parser)
    Files.newBufferedReader(Paths.get("input.txt")).use { reader ->
        Files.newBufferedWriter(Paths.get("output.txt")).use { writer ->
            hw03(reader, writer, parser)
        }
    }
    val endTime = System.currentTimeMillis()
//    println(endTime - beginTime)
}