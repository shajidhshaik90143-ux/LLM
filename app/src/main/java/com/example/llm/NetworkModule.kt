package com.example.llm.network

object NetworkModule {

    val openRouterApi: OpenRouterApi
        get() = ApiClient.api
}