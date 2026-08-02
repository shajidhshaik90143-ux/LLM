package com.example.llm.model

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String?
)