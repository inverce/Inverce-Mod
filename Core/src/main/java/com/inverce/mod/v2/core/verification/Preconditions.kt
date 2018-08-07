@file:JvmName("Preconditions")

package com.inverce.mod.v2.core.verification


@JvmOverloads
fun checkArgument(expression: Boolean, errorMessageTemplate: String? = null, vararg errorMessageArgs: Any = emptyArray()) {
    if (!expression) {
        throw IllegalArgumentException(format(errorMessageTemplate, *errorMessageArgs))
    }
}

@JvmOverloads
fun checkState(expression: Boolean, errorMessageTemplate: String? = null, vararg errorMessageArgs: Any = emptyArray()) {
    if (!expression) {
        throw IllegalStateException(format(errorMessageTemplate, *errorMessageArgs))
    }
}

@JvmOverloads
fun <T> checkNotNull(reference: T?, errorMessageTemplate: String? = null, vararg errorMessageArgs: Any = emptyArray()): T {
    when (reference) {
        null -> throw NullPointerException(format(errorMessageTemplate, *errorMessageArgs))
        else -> return reference
    }
}

@JvmOverloads
fun checkElementIndex(index: Int, size: Int, desc: String? = "index"): Int = when {
    index < 0 || index >= size -> throw IndexOutOfBoundsException(badElementIndex(index, size, desc))
    else -> index
}

private fun badElementIndex(index: Int, size: Int, desc: String?): String = when {
    index < 0 -> format("%s (%s) must not be negative", desc, index)
    size < 0 -> throw IllegalArgumentException("negative size: $size")
    else -> format("%s (%s) must be less than size (%s)", desc, index, size)
}


@JvmOverloads
fun checkPositionIndex(index: Int, size: Int, desc: String? = "index"): Int = when {
    index < 0 || index > size -> throw IndexOutOfBoundsException(badPositionIndex(index, size, desc))
    else -> index
}

private fun badPositionIndex(index: Int, size: Int, desc: String?): String = when {
    index < 0 -> format("%s (%s) must not be negative", desc, index)
    size < 0 -> throw IllegalArgumentException("negative size: $size")
    else -> format("%s (%s) must not be greater than size (%s)", desc, index, size)
}

fun checkPositionIndexes(start: Int, end: Int, size: Int) {
    if (start < 0 || end < start || end > size) {
        throw IndexOutOfBoundsException(badPositionIndexes(start, end, size))
    }
}

private fun badPositionIndexes(start: Int, end: Int, size: Int): String = when {
    start < 0 || start > size -> badPositionIndex(start, size, "start index")
    end < 0 || end > size -> badPositionIndex(end, size, "end index")
    else -> format("end index (%s) must not be less than start index (%s)", end, start)
}

/**
 * Substitutes each `%s` in `template` with an argument. These are matched by
 * position: the first `%s` gets `args[0]`, etc. If there are more arguments than
 * placeholders, the unmatched arguments will be appended to the end of the formatted message in
 * square braces.
 *
 * @param msg a non-null string containing 0 or more `%s` placeholders.
 * @param args     the arguments to be substituted into the message template. Arguments are converted
 * to strings using [String.valueOf]. Arguments can be null.
 */
internal fun <T> format(template: String?, vararg args: T): String {
    val msg = template.toString() // null -> "null"
    val builder = StringBuilder(msg.length + 16 * args.size)
    var templateStart = 0
    var i = 0
    while (i < args.size) {
        val placeholderStart = msg.indexOf("%s", templateStart)
        if (placeholderStart == -1) {
            break
        }
        builder.append(msg, templateStart, placeholderStart)
        builder.append(args[i++])
        templateStart = placeholderStart + 2
    }
    builder.append(msg, templateStart, msg.length)

    // if we run out of placeholders, append the extra args in square braces
    if (i < args.size) {
        builder.append(" [")
        builder.append(args[i++])
        while (i < args.size) {
            builder.append(", ")
            builder.append(args[i++])
        }
        builder.append(']')
    }

    return builder.toString()
}
