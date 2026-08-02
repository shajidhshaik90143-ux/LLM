package com.example.llm.evaluator

class PerplexityEvaluator {
    fun evaluate(text: String): Double {
        return MetricCalculator.calculatePerplexity(text)
    }
}