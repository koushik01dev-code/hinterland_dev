package com.hinterland.app.components.network

object NetworkClient {

    fun <T> create(service: Class<T>): T {
        return RetrofitWrapper.Companion.instance.client.create(service)
    }
}


