package quizzo.app.util

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }