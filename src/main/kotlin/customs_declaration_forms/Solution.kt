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
    println(countAllYesAnswers(fullText))
}

private fun readContentFromFile(fileName: String) = File(fileName).readText().trim().split(SPLIT_BY_LINE)

private fun processTheContentOfFile(fullText: List<String>): List<String> {
    val processedListOfAnswers = mutableListOf<String>()
    fullText.forEach {
        processedListOfAnswers.add(it.replace(SPLIT_BY_N, EMPTY_SPACE))
    }
    return processedListOfAnswers
}

private fun firstPart(listOfAnswers: List<String>): Int {
    return listOfAnswers.sumBy { answer ->
        answer.filter { !it.isWhitespace() }.toCharArray().distinct().size
    }
}

private fun countAllYesAnswers(fullText: List<String>): Int {
    return fullText
        .map { it.split(SPLIT_BY_N) }
        .map { lines -> lines.map { it.toSet() }.reduce { a, b -> a.intersect(b) } }
        .sumBy { it.size }
}
