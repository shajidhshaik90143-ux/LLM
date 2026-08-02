package com.example.llm.evaluator

class AccuracyEvaluator {
    fun evaluate(reference: String, candidate: String): Double {
        return MetricCalculator.calculateAccuracy(reference, candidate)
    }
}