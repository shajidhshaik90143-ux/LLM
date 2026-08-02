package com.example.llm.repository

import com.example.llm.model.EvaluationResult
import com.example.llm.model.Metric
import kotlin.math.max
import kotlin.math.min

class EvaluationRepository {

    fun evaluate(
        prompt: String,
        reference: String,
        generated: String,
        model: String
    ): EvaluationResult {

        val accuracy = calculateAccuracy(reference, generated)
        val coherence = calculateCoherence(generated)
        val perplexity = calculatePerplexity(generated)

        val metrics = listOf(
            Metric(
                name = "Accuracy",
                score = accuracy,
                description = "Similarity between reference and generated text."
            ),
            Metric(
                name = "Coherence",
                score = coherence,
                description = "Sentence flow and readability."
            ),
            Metric(
                name = "Perplexity",
                score = perplexity,
                description = "Lower values generally indicate more predictable text."
            )
        )

        val average = metrics.map { it.score }.average()

        return EvaluationResult(
            prompt = prompt,
            reference = reference,
            generated = generated,
            model = model,
            metrics = metrics,
            averageScore = average
        )
    }

    fun evaluateResponse(
        modelName: String,
        referenceText: String,
        responseText: String,
        prompt: String = ""
    ): EvaluationResult {
        return evaluate(
            prompt = prompt,
            reference = referenceText,
            generated = responseText,
            model = modelName
        )
    }

    private fun calculateAccuracy(
        reference: String,
        generated: String
    ): Double {
        if (reference.isBlank() || generated.isBlank()) {
            return 0.0
        }

        val refWords = reference.lowercase().split("\\s+".toRegex())
        val genWords = generated.lowercase().split("\\s+".toRegex())

        val common = refWords.intersect(genWords.toSet()).size

        val score = common.toDouble() / max(refWords.size, genWords.size)

        return (score * 100).coerceIn(0.0, 100.0)
    }

    private fun calculateCoherence(
        generated: String
    ): Double {
        if (generated.isBlank()) {
            return 0.0
        }

        val sentences = generated
            .split(".", "!", "?")
            .filter { it.isNotBlank() }

        if (sentences.isEmpty()) {
            return 0.0
        }

        val avgLength = generated.split("\\s+".toRegex()).size / sentences.size.toDouble()

        return min(avgLength * 5, 100.0)
    }

    private fun calculatePerplexity(
        generated: String
    ): Double {
        if (generated.isBlank()) {
            return 0.0
        }

        val words = generated.split("\\s+".toRegex())
        val unique = words.toSet().size

        val ratio = unique.toDouble() / words.size

        return (ratio * 100).coerceIn(0.0, 100.0)
    }
}