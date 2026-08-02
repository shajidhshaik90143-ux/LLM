package com.example.llm.model

data class Metric(

    val name: String,

    val score: Double,

    val description: String
) {
    val value: Double get() = score
}