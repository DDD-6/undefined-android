package com.editor.appcha.data.source

import com.editor.appcha.data.model.GreeterData

interface GreeterRemoteDataSource {

    fun sayHello(name: String): GreeterData
}