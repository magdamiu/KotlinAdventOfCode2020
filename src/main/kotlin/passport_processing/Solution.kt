package passport_processing

import java.io.File

// the requirements are here https://adventofcode.com/2020/day/4

private val passportKeys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
private val eyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

const val BLANK_LINE_WINDOWS = "\n\n"
const val BLANK_LINE_LINUX_MAC = "\r\n\r\n"

const val NEW_LINE = "\r\n"
const val SPACE_LINE = "\n"
const val EMPTY_SPACE = " "
const val KEY_SEPARATOR = ":"

const val FILE_NAME = "src/main/kotlin/passport_processing/input.txt"

fun main() {
    val passports = readContentFromFile()
    firstPart(passports)
    secondPart(passports)
}

class Passport(private val content: Map<String, String>) {
    companion object {
        fun fromString(line: String): Passport {
            val processedLine = line.replace(SPACE_LINE, EMPTY_SPACE)
            val fieldsAndValues = processedLine.split(NEW_LINE, SPACE_LINE, EMPTY_SPACE)

            val currentPassportMap = mutableMapOf<String, String>()
            fieldsAndValues.forEach { passport ->
                currentPassportMap[passport.substringBefore(KEY_SEPARATOR)] = passport.substringAfter(KEY_SEPARATOR)
            }
            return Passport(currentPassportMap)
        }
    }

    fun hasAllRequiredFields() = content.keys.containsAll(passportKeys)

    fun hasValidValues() = content.all { (key, value) ->
        when (key) {
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
            "pid" -> value.matches(Regex("[0-9]{9}")) // value.length == 9 && value.all(Char::isDigit)
            else -> true
        }
    }
}

private fun readContentFromFile() = File(FILE_NAME)
    .readText()
    .split(BLANK_LINE_WINDOWS, BLANK_LINE_LINUX_MAC)
    .map { Passport.fromString(it) }

private fun firstPart(passports: List<Passport>) {
    println(passports.count(Passport::hasAllRequiredFields))
}

private fun secondPart(passports: List<Passport>) {
    println(passports.count { it.hasValidValues() && it.hasAllRequiredFields() })
}