package com.example.llm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm.databinding.ActivityResultBinding
import com.example.llm.model.Metric
import com.example.llm.ui.adapter.MetricAdapter

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val modelName = intent.getStringExtra("EXTRA_MODEL") ?: "LLM Response"
        val responseText = intent.getStringExtra("EXTRA_RESPONSE") ?: ""

        binding.tvResultModel.text = modelName
        binding.tvResultText.text = responseText

        // Sample static breakdown view for demonstration
        val sampleMetrics = listOf(
            Metric("Accuracy", 0.85, "High lexical similarity with source ground truth."),
            Metric("Coherence", 0.92, "Sentence structures flow logically."),
            Metric("Perplexity", 12.4, "Low entropy score indicates natural predictability.")
        )

        binding.rvResultMetrics.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = MetricAdapter(sampleMetrics)
        }
    }
}