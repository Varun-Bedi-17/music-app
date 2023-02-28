package com.example.musicbajao.model.repository

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No connectivity exception"
}