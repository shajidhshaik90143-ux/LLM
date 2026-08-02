package com.example.llm.model

data class EvaluationResult(

    val prompt: String,

    val reference: String,

    val generated: String,

    val model: String,

    val metrics: List<Metric>,

    val averageScore: Double
) {
    val modelName: String get() = model
    val responseText: String get() = generated
}