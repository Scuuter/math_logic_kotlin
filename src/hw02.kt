import java.io.BufferedReader
import java.io.BufferedWriter


val Impl = Operation(Operation.Type.IMPLICATION)

fun hw02(reader: BufferedReader, writer: BufferedWriter, parser: Parser) {
    val strings = reader.readLines();
    var string = strings[0];
    val sList = string.replace("\\p{javaWhitespace}".toRegex(), "").replace("|-", ",").split(",")
    var index = 0
    var buf: Node?
    val alfa = parser.parse(sList[sList.size - 2])
    if (sList.size > 2){
        writer.append(parser.parse(sList[0])!!.toStrBuilder());
    }
    while (index < sList.size - 2) {
        if (index > 0){
            writer.append(", ").append(parser.parse(sList[index])!!.toStrBuilder())
        }
        hypothesis[parser.parse(sList[index])] = ++index
    }
    writer.append("|-").append(Node(Impl, parser.parse(sList[sList.size - 2]), parser.parse(sList.last())).toStrBuilder())
    writer.newLine()

    for (lineNum in 1 until strings.size) {
        string = strings[lineNum].replace("\\p{javaWhitespace}".toRegex(), "")
        if (string == "") {
            continue
        }

        buf = parser.parse(string)




        val axioms = checkAxioms(buf!!)

        when {
            hypothesis.containsKey(buf) || axioms != 0 -> {
                writer.append(buf.toStrBuilder()).append("\n")
                writer.append(Node(Impl, buf, Node(Impl, alfa, buf)).toStrBuilder()).append("\n")
            }
            buf == alfa -> {
                writeAlfaToAlfa(writer, alfa)
            }
            proofedMP.containsKey(buf) -> {
                val bj = proofedMP[buf]!!.first
                writer.append(createSecondAxiom(alfa!!, bj, buf).toStrBuilder()).append("\n")
                writer.append(Node(Impl, Node(Impl, alfa, Node(Impl, bj, buf)), Node(Impl, alfa, buf)).toStrBuilder()).append("\n")
            }

        }

        writer.append(Node(Impl, alfa, buf).toStrBuilder())
        writer.newLine()

        if (reversedMP.containsKey(buf)) {
            for (mp in reversedMP[buf]!!) {
                proofedMP[mp.first] = Pair(buf, mp.second)
            }
        }
        if (buf.operation.type == Operation.Type.IMPLICATION) {
            if (proofed.containsKey(buf.leftChild!!)) {
                proofedMP[buf.rightChild!!] = Pair(buf.leftChild!!, lineNum)
            } else {
                if (!reversedMP.containsKey(buf.leftChild!!))
                    reversedMP[buf.leftChild!!] = ArrayList()
                reversedMP[buf.leftChild!!]!!.add(Pair(buf.rightChild!!, lineNum))
            }
        }
        proofed[buf] = lineNum

    }
}

fun writeAlfaToAlfa(writer: BufferedWriter, alfa: Node){
    val first = createFirstAxiom(alfa, alfa)
    val third = Node(Impl, alfa, Node(Impl, Node(Impl, alfa, alfa), alfa))
    val second = Node(Impl, third, Node(Impl, alfa, alfa))
    writer.append(first.toStrBuilder()).append("\n")
    writer.append(Node(Impl,first,second).toStrBuilder()).append("\n")
    writer.append(second.toStrBuilder()).append("\n")
    writer.append(third.toStrBuilder()).append("\n")
}