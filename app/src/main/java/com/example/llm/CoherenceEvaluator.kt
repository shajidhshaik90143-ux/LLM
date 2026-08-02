package com.example.llm.evaluator

class CoherenceEvaluator {
    fun evaluate(text: String): Double {
        return MetricCalculator.calculateCoherence(text)
    }
}