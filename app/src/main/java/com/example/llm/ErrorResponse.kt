package com.example.llm.model

data class ErrorResponse(
    val error: ApiError?
)

data class ApiError(
    val message: String?,
    val code: Int?
)