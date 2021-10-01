package com.editor.appcha.domain.repo

import com.editor.appcha.domain.model.Greeter

interface GreeterRepository {

    fun getName(): String

    suspend fun sayHello(name: String): Greeter
}

