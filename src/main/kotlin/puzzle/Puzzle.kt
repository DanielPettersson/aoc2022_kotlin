package puzzle

import java.io.IOException

fun solve(inputFileName: String, solver: (List<String>) -> Any): Any {
    val fileUrl = object {}.javaClass.getResource("/$inputFileName") ?: throw IOException(inputFileName)
    return solver(fileUrl.readText().lines())
}
