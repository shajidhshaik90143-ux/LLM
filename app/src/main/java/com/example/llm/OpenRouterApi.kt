package com.example.llm.network

import com.example.llm.model.ChatRequest
import com.example.llm.model.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenRouterApi {

    @Headers(
        "Content-Type: application/json"
    )
    @POST("chat/completions")
    suspend fun chatCompletion(
        @Body request: ChatRequest
    ): Response<ChatResponse>
}