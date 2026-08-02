package com.example.llm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.llm.databinding.ActivityCompareBinding
import com.example.llm.ui.adapter.MetricAdapter
import com.example.llm.utils.Constants
import com.example.llm.utils.Resource
import com.example.llm.viewmodel.CompareViewModel

class CompareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompareBinding

    private val viewModel: CompareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(applicationContext))
        }

        setupRecyclerViews()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvMetricsA.layoutManager = LinearLayoutManager(this)
        binding.rvMetricsB.layoutManager = LinearLayoutManager(this)

        binding.rvMetricsA.adapter = MetricAdapter()
        binding.rvMetricsB.adapter = MetricAdapter()
    }

    private fun setupListeners() {

        binding.btnRunCompare.setOnClickListener {

            val prompt = binding.etPrompt.text.toString().trim()
            val reference = binding.etReference.text.toString().trim()

            if (prompt.isBlank()) {
                Toast.makeText(this, "Enter a prompt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (reference.isBlank()) {
                Toast.makeText(this, "Enter reference text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.compareModels(
                apiKey = Constants.OPENROUTER_API_KEY,
                prompt = prompt,
                referenceText = reference,
                modelA = Constants.DEFAULT_MODEL_A,
                modelB = Constants.DEFAULT_MODEL_B
            )
        }
    }

    private fun observeViewModel() {

        viewModel.resultA.observe(this) { resource ->

            when (resource) {

                is Resource.Loading -> {
                    binding.pbLoadingA.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    binding.pbLoadingA.visibility = View.GONE

                    resource.data?.let {

                        binding.tvModelAName.text = it.modelName
                        binding.tvResponseA.text = it.responseText

                        (binding.rvMetricsA.adapter as MetricAdapter)
                            .updateMetrics(it.metrics)
                    }
                }

                is Resource.Error -> {

                    binding.pbLoadingA.visibility = View.GONE

                    binding.tvResponseA.text = resource.message
                }
            }
        }

        viewModel.resultB.observe(this) { resource ->

            when (resource) {

                is Resource.Loading -> {
                    binding.pbLoadingB.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    binding.pbLoadingB.visibility = View.GONE

                    resource.data?.let {

                        binding.tvModelBName.text = it.modelName
                        binding.tvResponseB.text = it.responseText

                        (binding.rvMetricsB.adapter as MetricAdapter)
                            .updateMetrics(it.metrics)
                    }
                }

                is Resource.Error -> {

                    binding.pbLoadingB.visibility = View.GONE

                    binding.tvResponseB.text = resource.message
                }
            }
        }
    }
}