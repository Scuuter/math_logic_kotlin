import java.io.BufferedReader
import java.io.BufferedWriter

fun hw0(reader: BufferedReader, writer: BufferedWriter, parser: Parser) {
    writer.write( parser.parse(reader.readLine())!!.toCoolString())
}