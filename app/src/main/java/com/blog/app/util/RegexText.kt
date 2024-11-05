package com.blog.app.util

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexText {

    fun checkNumberUppercaseLowercaseSpecialCharacters(inputString: String): Boolean {
        val specialCharsSeq = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{6,}\$")
        val hasSpecial: Matcher = specialCharsSeq.matcher(inputString)

        return hasSpecial.find()
    }
}
