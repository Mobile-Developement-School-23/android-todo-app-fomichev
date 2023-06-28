package com.example.mytodoapp.data

import kotlin.properties.Delegates

interface ApiTokenProvider {
    var token: String

    companion object : ApiTokenProvider {
        override var token: String by Delegates.notNull()
    }
}