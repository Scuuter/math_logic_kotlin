import java.io.BufferedReader
import java.io.BufferedWriter

val hypothesis: HashMap<Node?, Int> = hashMapOf()
val proofed: HashMap<Node, Int> = hashMapOf()
val reversedMP: HashMap<Node, ArrayList<Pair<Node, Int>>> = hashMapOf()
val proofedMP: HashMap<Node, Pair<Node, Int>> = hashMapOf()

fun hw01(reader: BufferedReader, writer: BufferedWriter, parser: Parser) {
    val strings = reader.readLines();
    var string = strings[0];
    val sList = string.replace("\\p{javaWhitespace}".toRegex(), "").split(",")
    var index = 0
    var buf: Node?

    while (index < sList.size - 1) {
        hypothesis[parser.parse(sList[index])] = ++index
    }

    if (sList.last().substringBefore("|-") != "") {
        hypothesis[parser.parse(sList.last().substringBefore('|'))] = sList.size
    }

    for (lineNum in 1 until strings.size) {
        string = strings[lineNum].replace("\\p{javaWhitespace}".toRegex(), "")
        if (string == "") {
            continue
        }


        buf = parser.parse(string)

        writer.append("($lineNum) ")

        writer.append(buf!!.toString())

        writer.append(" ")
        var flag = false

        val axioms = checkAxioms(buf)

        when {
            axioms != 0 -> {
                flag = true
                writer.append("(Сх. акс. $axioms)")
            }
            proofedMP.containsKey(buf) -> {
                flag = true
                writer.append("(M.P. ${proofedMP[buf]!!.second}, ${proofed[proofedMP[buf]!!.first]})")
            }
            hypothesis.containsKey(buf) -> {
                flag = true
                writer.append("(Предп. ${hypothesis[buf]})")
            }
        }

        if (!flag) {
            writer.append("(Не доказано)")
        }

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
        writer.newLine()
    }
}