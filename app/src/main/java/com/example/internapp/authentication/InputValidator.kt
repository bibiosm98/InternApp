package com.example.internapp.authentication

import android.text.TextUtils
import java.lang.Exception

class InputValidator {
    val passwordRGX: Regex =
        """(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#${'$'}])(?=\S+${'$'}).{6,}""".toRegex()

    fun checkEmail(input: String) {
        if (TextUtils.isEmpty(input)) throw Exception("Podaj adres Email.")
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) return
        throw Exception("Niepoprawny adres Email.")
    }

    fun checkPassword(input: String) {
        if (TextUtils.isEmpty(input)) throw Exception("Podaj hasło.")
        if (input.length < 6) throw Exception("Hasło zbyt krótkie, min 6 znaków.")
        if (passwordRGX.matches(input)) return
        throw Exception("Niepoprawne hasło. Wymagane są: mała litera, wielka litera, cyfra i znak zpecjalny(!?@#$)")
    }
}