package customs_declaration_forms

import passport_processing.EMPTY_SPACE
import java.io.File

// the requirements are here https://adventofcode.com/2020/day/6
// assumptions done: the input is valid

const val SPLIT_BY_LINE = "\n\n"
const val SPLIT_BY_N = "\n"
const val INPUT_FILE_FORMS = "src/main/kotlin/customs_declaration_forms/input.txt"

fun main() {
    val fullText = readContentFromFile(INPUT_FILE_FORMS)
    val listOfAnswers = processTheContentOfFile(fullText)

    println(firstPart(listOfAnswers))
    println(secondPart(fullText))
}

private fun readContentFromFile(fileName: String) = File(fileName).readText().trim().split(SPLIT_BY_LINE)

private fun processTheContentOfFile(fullText: List<String>) =
    fullText.map { it.replace(SPLIT_BY_N, EMPTY_SPACE) }.toList()

private fun firstPart(listOfAnswers: List<String>): Int {
    return listOfAnswers.sumOf { answer ->
        answer.filter { !it.isWhitespace() }.toSet().size
    }
}

private fun secondPart(fullText: List<String>): Int {
    return fullText
        .map { it.split(SPLIT_BY_N) }
        .map { lines -> lines.map { it.toSet() }.reduce { a, b -> a.intersect(b) } }
        .sumOf { it.size }
}
