
import java.io.BufferedReader
import java.io.BufferedWriter

val variables: HashMap<Node?, Int> = hashMapOf()
var varBools = 0
var curVar = 0

fun hw03(reader: BufferedReader, writer: BufferedWriter, parser: Parser) {
    val string = reader.readLine()
    val sList = string.replace("\\p{javaWhitespace}".toRegex(), "").replace("|=", ",").split(",")
    var index = 0
    var buf: Node?
    var formula = parser.parse(sList.last())

    index = sList.size -2
    while (index >= 0) {
        if (!sList[index].isEmpty())
            formula = Node(Impl, parser.parse(sList[index]), formula)
        --index
    }
/*
    while (index < sList.size - 1) {
        hypothesis[parser.parse(sList[index])] = ++index
    }

    */

    getVariables(formula)

    while (varBools < (1 shl curVar)) {
         if (!logic(formula)){
             writer.append("Высказывание ложно при ")
             for (i in variables.keys){
                 writer.append(i!!.toStrBuilder())
                 if (hypothesis.containsKey(i) || isTrue(variables[i]!!, varBools)){
                     writer.append("=И")
                 } else writer.append("=Л")
                 if (i != variables.keys.last()){
                     writer.append(", ")
                 }
             }
             return
         }
        varBools++
    }



}

fun proof(root: Node?) {
    if (root == null)
        return

}

fun logic(root: Node?): Boolean {
    if (root == null) {
        return false
    }
    if (hypothesis.containsKey(root)){
        return true
    }
    return when {
        root.operation.type == Operation.Type.IMPLICATION ->  implication(logic(root.leftChild), logic(root.rightChild))
        root.operation.type == Operation.Type.CONJUNCTION ->  logic(root.leftChild) && logic(root.rightChild)
        root.operation.type == Operation.Type.DISJUNCTION ->  logic(root.leftChild) || logic(root.rightChild)
        root.operation.type == Operation.Type.NEGATION -> !logic(root.leftChild)
        else -> isTrue(variables[root]!!, varBools)
    }
}

fun isTrue(variable: Int, key: Int): Boolean {
    return (key shr variable) % 2 == 1
}

fun implication(a: Boolean, b: Boolean): Boolean {
    if (a && !b){
        return false
    }
    return true
}

fun getVariables(root: Node?) {
    if (root == null)
        return
    if (root.operation.isVariable()) {
        if (!variables.containsKey(root)) {
            variables[root] = curVar
            curVar++
            return
        }
    } else {
        getVariables(root.leftChild)
        getVariables(root.rightChild)
    }
}