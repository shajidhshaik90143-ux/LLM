package com.example.llm.evaluator

import com.chaquo.python.Python

object MetricCalculator {

    fun calculateAccuracy(reference: String, candidate: String): Double {
        return try {
            val py = Python.getInstance()
            val module = py.getModule("accuracy")
            module.callAttr("compute_accuracy", reference, candidate).toDouble()
        } catch (e: Exception) {
            0.0
        }
    }

    fun calculateCoherence(text: String): Double {
        return try {
            val py = Python.getInstance()
            val module = py.getModule("coherence")
            module.callAttr("compute_coherence", text).toDouble()
        } catch (e: Exception) {
            0.0
        }
    }

    fun calculatePerplexity(text: String): Double {
        return try {
            val py = Python.getInstance()
            val module = py.getModule("perplexity")
            module.callAttr("compute_perplexity", text).toDouble()
        } catch (e: Exception) {
            0.0
        }
    }
}