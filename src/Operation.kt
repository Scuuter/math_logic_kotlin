class Operation(val type: Type, val value: String? = null) {
    enum class Type(val code: Int, val symbol: String?) {
        NOTHING(-1, null),
        VARIABLE(0, null),
        NEGATION(1, "!"),
        CONJUNCTION(2, "&"),
        DISJUNCTION(3, "|"),
        IMPLICATION(4, "->")
    }

    fun isNothing(): Boolean{
        return type == Type.NOTHING
    }

    fun isVariable(): Boolean {
        return type == Type.VARIABLE;
    }

    fun isUnary(): Boolean {
        return type == Type.NEGATION;
    }

    fun isBinary(): Boolean {
        return type.code >= 2
    }

    override fun toString(): String {
        if (isVariable()) {
            return value!!;
        }
        return type.symbol!!;
    }

    override fun hashCode(): Int {
        if (isNothing()){
            return 0
        }

        if (isVariable()) {
            return value!!.hashCode();
        }
        return type.symbol!!.hashCode();
    }

    override fun equals(other: Any?): Boolean {
        if (other is Operation)
            if (hashCode() == other.hashCode()) {
                /*
                if (operation.type == other.operation.type) {
                    if (operation.isVariable() || operation.isUnary() && leftChild!!.hashCode() == other.leftChild!!.hashCode())
                        return true
                    if (operation.isBinary() && leftChild!!.hashCode() == other.leftChild!!.hashCode() && rightChild!!.hashCode() == other.rightChild!!.hashCode())
                        return true
                }
                */
                return true
            }
        return false
    }
}