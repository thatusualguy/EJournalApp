package io.github.thatusualguy.ejournal.domain.repo

import java.util.regex.Pattern

fun isValidPassword(password: String): Boolean {
    return if (password.isBlank()) {
        false;
    } else {
        password.length >= 8;
    }
}

fun isValidEmail(email: String): Boolean {
    return if (email.isBlank()) {
        false;
    } else {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

fun isValidPhone(phone: String): Boolean {
    return if (phone.isBlank()) {
        false;
    } else {
        Pattern.compile("[+8]?[0-9.-]+").matcher(phone).matches();
    }
}