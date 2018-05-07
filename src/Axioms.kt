
var stringAxioms = arrayOf(
        "A->B->A",
        "(A->B)->(A->B->C)->(A->C)",
        "A->B->A&B",
        "(A&B)->A",
        "(A&B)->B",
        "A->(A|B)",
        "B->(A|B)",
        "(A->C)->(B->C)->((A|B)->C)",
        "(A->B)->(A->(!B))->(!A)",
        "(!!A)->A"
)

var axioms = arrayOfNulls<Node>(10)
var axiomVariables = hashMapOf<String, Node>()

fun initAxioms(p: Parser) {
    for (i in 0 until 10) {
        axioms[i] = p.parse(stringAxioms[i])
    }
}

fun checkTree(root: Node?, axiom: Node?): Boolean {
    if (root == null)
        return axiom == null

    if (axiom!!.operation.isVariable()){
        if (!axiomVariables.containsKey(axiom.operation.value)) {
            axiomVariables[axiom.operation.value!!] = root
            return true
        }
        return axiomVariables[axiom.operation.value!!] == root
    }
    return when {
        root.operation.type != axiom.operation.type -> false
        axiom.operation.isBinary() -> checkTree(root.leftChild, axiom.leftChild) && checkTree(root.rightChild, axiom.rightChild)
        else -> checkTree(root.leftChild, axiom.leftChild) //unary
    }
}

fun checkAxioms(root: Node): Int {
    for (i in 0 until 10) {
        axiomVariables.clear()
        if (checkTree(root, axioms[i])) {
            return i + 1
        }
    }
    return 0
}

fun createFirstAxiom(a: Node, b:Node): Node{
    return Node(Impl,a,Node(Impl, b, a))
}

fun createSecondAxiom(a: Node, b: Node, c: Node): Node {
    return Node(Impl, Node(Impl, a, b), Node(Impl, Node(Impl, a, Node(Impl, b, c)), Node(Impl, a, c)))
}
/*
fun checkFirst(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.rightChild!!.operation.type == Operation.Type.IMPLICATION)
            if (root.leftChild == root.rightChild.rightChild)
                return true
    return false
}

fun checkSecond(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION) // depth 1
        if (root.rightChild!!.operation.type == Operation.Type.IMPLICATION && root.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 2
            if (root.rightChild.rightChild!!.operation.type == Operation.Type.IMPLICATION && root.rightChild.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 3
                if (root.rightChild.leftChild.rightChild!!.operation.type == Operation.Type.IMPLICATION) // depth 4
                    if (root.leftChild.leftChild == root.rightChild.leftChild.leftChild && root.leftChild.leftChild == root.rightChild.rightChild.leftChild) // var a
                        if (root.leftChild.rightChild == root.rightChild.leftChild.rightChild.leftChild) // var b
                            if (root.rightChild.leftChild.rightChild.rightChild == root.rightChild.rightChild.rightChild) // var c
                                return true
    return false
}

fun checkThird(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.rightChild!!.operation.type == Operation.Type.IMPLICATION)
            if (root.rightChild.rightChild!!.operation.type == Operation.Type.CONJUNCTION)
                if (root.leftChild == root.rightChild.rightChild.leftChild) // var a
                    if (root.rightChild.leftChild == root.rightChild.rightChild.rightChild) // var b
                        return true
    return false
}

fun checkFourth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.leftChild!!.operation.type == Operation.Type.CONJUNCTION)
            if (root.leftChild.leftChild == root.rightChild) // var a
                return true
    return false
}

fun checkFifth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.leftChild!!.operation.type == Operation.Type.CONJUNCTION)
            if (root.leftChild.rightChild == root.rightChild) // var b
                return true
    return false
}

fun checkSixth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.rightChild!!.operation.type == Operation.Type.DISJUNCTION)
            if (root.leftChild == root.rightChild.leftChild) // var a
                return true
    return false
}

fun checkSeventh(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.rightChild!!.operation.type == Operation.Type.DISJUNCTION)
            if (root.leftChild == root.rightChild.rightChild) // var b
                return true
    return false
}

fun checkEighth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION) // depth 1
        if (root.rightChild!!.operation.type == Operation.Type.IMPLICATION && root.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 2
            if (root.rightChild.rightChild!!.operation.type == Operation.Type.IMPLICATION && root.rightChild.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 3
                if (root.rightChild.rightChild.leftChild!!.operation.type == Operation.Type.DISJUNCTION) // depth 4
                    if (root.leftChild.leftChild == root.rightChild.rightChild.leftChild.leftChild) // var a
                        if (root.leftChild.rightChild == root.rightChild.leftChild.rightChild && root.leftChild.rightChild == root.rightChild.rightChild.rightChild) // var c
                            if (root.rightChild.leftChild.leftChild == root.rightChild.rightChild.leftChild.rightChild) // var b
                                return true
    return false
}

fun checkNinth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION) // depth 1
        if (root.rightChild!!.operation.type == Operation.Type.IMPLICATION && root.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 2
            if (root.rightChild.rightChild!!.operation.type == Operation.Type.NEGATION && root.rightChild.leftChild!!.operation.type == Operation.Type.IMPLICATION) // depth 3
                if (root.rightChild.leftChild.rightChild!!.operation.type == Operation.Type.NEGATION) // depth 4
                    if (root.leftChild.leftChild == root.rightChild.leftChild.leftChild && root.leftChild.leftChild == root.rightChild.rightChild.leftChild) // var a
                        if (root.leftChild.rightChild == root.rightChild.leftChild.rightChild.leftChild) // var b
                            return true
    return false
}

fun checkTenth(root: Node): Boolean {
    if (root.operation.type == Operation.Type.IMPLICATION)
        if (root.leftChild!!.operation.type == Operation.Type.NEGATION && root.leftChild.leftChild!!.operation.type == Operation.Type.NEGATION)
            if (root.leftChild.leftChild.leftChild == root.rightChild)
                return true
    return false
}

fun checkAxioms(root: Node): Int {
    if (checkFirst(root)) {
        return 1
    }
    if (checkFourth(root)) {
        return 4
    }
    if (checkFifth(root)) {
        return 5
    }
    if (checkSixth(root)) {
        return 6
    }
    if (checkTenth(root)) {
        return 10
    }
    if (checkSeventh(root)) {
        return 7
    }
    if (checkThird(root)) {
        return 3
    }
    if (checkSecond(root)) {
        return 2
    }
    if (checkEighth(root)) {
        return 8
    }
    if (checkNinth(root)) {
        return 9
    }
    return 0
}
*/