package passport_processing

import java.io.File

// the requirements are here https://adventofcode.com/2020/day/4

val passportKeys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
val eyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

const val EMPTY_LINE = "\r\n\r"
const val NEW_LINE = "\r\n"
const val SPACE_LINE = "\n"
const val EMPTY_SPACE = " "
const val KEY_SEPARATOR = ":"
const val FILE_NAME = "src/main/kotlin/passport_processing/input.txt"

fun main() {
    processPassports()
}

private fun processPassports() {
    val fileFullText = readFileDirectlyAsText()
    var countFirstPartValid = 0
    var countSecondPartValid = 0

    fileFullText.split(EMPTY_LINE).forEach { content ->
        val inputCurrentPassport = cleanExtraLines(content)
        val currentPassport = mutableMapOf<String, String>()
        inputCurrentPassport.split(EMPTY_SPACE).forEach { p ->
            currentPassport[p.substringBefore(KEY_SEPARATOR)] = p.substringAfter(KEY_SEPARATOR)
        }

        if (currentPassport.keys.containsAll(passportKeys)) {
            countFirstPartValid++
            if (currentPassport.entries.all { it.isValid() }) {
                countSecondPartValid++
            }
        }
    }
    println(countFirstPartValid)
    println(countSecondPartValid)
}

private fun readFileDirectlyAsText(): String = File(FILE_NAME).readText(Charsets.UTF_8)

private fun cleanExtraLines(content: String) =
    content.replace(NEW_LINE, EMPTY_SPACE).replace(SPACE_LINE, EMPTY_SPACE)

private fun Map.Entry<String, String>.isValid(): Boolean = when (key) {
    "byr" -> value.length == 4 && value.toInt() in 1920..2002
    "iyr" -> value.length == 4 && value.toInt() in 2010..2020
    "eyr" -> value.length == 4 && value.toInt() in 2020..2030
    "hgt" -> when {
        value.endsWith("cm") -> value.dropLast(2).toInt() in 150..193
        value.endsWith("in") -> value.dropLast(2).toInt() in 59..76
        else -> false
    }
    "hcl" -> value.matches(Regex("#[0-9a-f]{6}"))
    "ecl" -> value in eyeColors
    "pid" -> value.matches(Regex("[0-9]{9}"))
    else -> true
}