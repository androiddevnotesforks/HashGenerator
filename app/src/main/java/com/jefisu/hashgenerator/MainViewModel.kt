package com.jefisu.hashgenerator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.security.MessageDigest

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var hash by mutableStateOf("")
        private set

    init {
        val plainText = savedStateHandle.get<String>("plainText")
        val algorithm = savedStateHandle.get<String>("algorithm")
        if (plainText != null && algorithm != null) {
            generateHash(plainText, algorithm)
        }
    }

    private fun generateHash(plainText: String, algorithm: String) {
        val bytes = MessageDigest
            .getInstance(algorithm)
            .digest(plainText.toByteArray())

        hash = bytes.joinToString(
            separator = "",
            transform = { "%02x".format(it) }
        )
    }
}