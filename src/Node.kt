import java.util.*

class Node(val operation: Operation = Operation(Operation.Type.NOTHING), val leftChild: Node? = null, val rightChild: Node? = null) {
    var hash: Int? = null
    var string: String? = null
    var builder: StringBuilder? = null

    override fun toString(): String {
        if (string == null) {
            string = if (operation.isVariable()) operation.toString()
            else if (operation.isUnary() //return StringBuilder("(").append(operation).append(leftChild).append(")").toString()
            ) "(${operation}${leftChild})"
            else //return StringBuilder("(").append(leftChild).append(operation).append(rightChild).append(")").toString()
                "(${leftChild}${operation}${rightChild})"
        }
        return string!!
    }

    fun toStrBuilder(): StringBuilder {
        if (builder == null) {
            builder = StringBuilder()
            when {
                operation.isVariable() -> builder!!.append(operation)
                operation.isUnary() -> builder!!.append("(").append(operation).append(leftChild).append(")")
            //return "(${operation}${leftChild.toString()})"
                else -> builder!!.append("(").append(leftChild).append(operation).append(rightChild).append(")")
            }
        }
        return builder!!
    }

    fun toCoolString(): String {
        if (operation.isVariable()) {
            return operation.toString()
        }
        if (operation.isUnary()) {
            return "(" + operation.toString() + leftChild.toString() + ")"
        }
        return "(" + operation.toString() + "," + leftChild.toString() + "," + rightChild.toString() + ")"
    }

    override fun hashCode(): Int {
        if (hash == null) {
            if (operation.isVariable()) {
                hash = operation.hashCode();
            } else if (operation.isUnary()) {
                hash = Objects.hash(operation, leftChild)
            } else hash = Objects.hash(operation, leftChild, rightChild)
        }
        return hash!!
    }

    override fun equals(other: Any?): Boolean {
        if (other is Node)
            if (hashCode() == other.hashCode() && toString() == other.toString()) {
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