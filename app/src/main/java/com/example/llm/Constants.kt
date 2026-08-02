package com.example.llm.utils

import com.example.llm.BuildConfig

object Constants {

    const val BASE_URL = "https://openrouter.ai/api/v1/"

    const val CHAT_COMPLETION = "chat/completions"

    const val CONNECT_TIMEOUT = 60L

    const val READ_TIMEOUT = 60L

    // OpenRouter API Key
    const val OPENROUTER_API_KEY = "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"

    // Models
    const val MODEL_GPT4O = "openai/gpt-4o"

    const val MODEL_GPT4_1 = "openai/gpt-4.1"

    const val MODEL_CLAUDE = "anthropic/claude-3.5-sonnet"

    const val MODEL_GEMINI = "google/gemini-2.5-pro"

    // Default models used for comparison
    const val DEFAULT_MODEL_A = MODEL_GPT4O

    const val DEFAULT_MODEL_B = MODEL_CLAUDE

    val MODELS = listOf(
        MODEL_GPT4O,
        MODEL_GPT4_1,
        MODEL_CLAUDE,
        MODEL_GEMINI
    )
}