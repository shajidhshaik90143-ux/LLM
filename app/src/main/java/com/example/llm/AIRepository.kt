package com.example.llm.repository

import android.content.Context
import com.example.llm.model.ChatRequest
import com.example.llm.model.ChatResponse
import com.example.llm.model.Message
import com.example.llm.network.NetworkModule
import com.example.llm.utils.Resource
import retrofit2.HttpException
import java.io.IOException

class AIRepository(context: Context? = null) {

    private val api = NetworkModule.openRouterApi

    suspend fun generateResponse(
        prompt: String,
        model: String
    ): Resource<ChatResponse> {
        return try {
            val request = ChatRequest(
                model = model,
                messages = listOf(
                    Message(
                        role = "user",
                        content = prompt
                    )
                )
            )

            val response = api.chatCompletion(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is empty.")
            } else {
                val error = response.errorBody()?.string()
                    ?: "Unknown server error"
                Resource.Error(error)
            }
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("HTTP Error: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun fetchCompletion(
        apiKey: String,
        model: String,
        prompt: String,
        temperature: Double = 0.7,
        maxTokens: Int = 512
    ): Resource<String> {
        return try {
            val request = ChatRequest(
                model = model,
                messages = listOf(
                    Message(
                        role = "user",
                        content = prompt
                    )
                )
            )

            val response = api.chatCompletion(request)

            if (response.isSuccessful) {
                val text = response.body()?.choices?.firstOrNull()?.message?.content
                if (text != null) {
                    Resource.Success(text)
                } else {
                    Resource.Error("Response body is empty.")
                }
            } else {
                val error = response.errorBody()?.string()
                    ?: "Unknown server error"
                Resource.Error(error)
            }
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("HTTP Error: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected Error")
        }
    }
}