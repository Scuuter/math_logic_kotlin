class Parser() {
    private var expression: String
    private var pos: Int

    init {
        this.expression = ""
        this.pos = 0
    }

    fun parse(expression: String): Node? {
        this.expression = expression.replace("\\p{javaWhitespace}".toRegex(), "") + "\u0000"
        this.pos = 0
        return parseImpl()
    }

    private fun parseImpl(): Node? {
        var root = parseDisj()
    //    var root = left

        if (isOp("->")) {
            root = Node(Operation(Operation.Type.IMPLICATION), root, parseImpl())
        }

        return root
    }

    private fun parseDisj(): Node? {
        var root = parseConj()
        //var root: Node? = null

        while (isOp("|")) {
            root = Node(Operation(Operation.Type.DISJUNCTION), root, parseConj())
        //    left = root
        }

        return root
    }

    private fun parseConj(): Node? {
        var root = parseNeg()
    //    var root: Node? = null

        while (isOp("&")) {
            root = Node(Operation(Operation.Type.CONJUNCTION), root, parseNeg())
    //        left = root
        }

        return root
    }

    private fun parseNeg(): Node? {
        if (isOp("!")) {
            return Node(Operation(Operation.Type.NEGATION), parseNeg())
        }

        if (isOp("(")) {
            val result = parseImpl()
            pos++
            return result
        }

        return parseVar()
    }

    private fun parseVar(): Node {
        var endIndex = pos

        while (Character.isLetter(expression[endIndex]) || Character.isDigit(expression[endIndex])) {
            endIndex++
        }

        val variable = expression.substring(pos, endIndex)
        pos = endIndex
        return Node(Operation(Operation.Type.VARIABLE, variable))
    }

    private fun isOp(op: String): Boolean {
        if (expression.startsWith(op, pos)) {
            pos += op.length
            return true
        }
        return false
    }
}