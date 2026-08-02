package com.example.llm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.llm.databinding.ActivityMainBinding
import com.example.llm.repository.AIRepository
import com.example.llm.utils.Constants
import com.example.llm.utils.Resource
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: AIRepository

    // Available models for selection
    private val availableModels = listOf(
        Constants.DEFAULT_MODEL_A,
        Constants.DEFAULT_MODEL_B
    )

    private var selectedModel = Constants.DEFAULT_MODEL_A
    private var temperature = 0.7
    private var maxTokens = 512

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instantiate repository using parameterless constructor
        repository = AIRepository()

        setupToolbar()
        setupModelDropdown()
        setupTemperatureSlider()
        setupTokenSlider()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "LLM Evaluation"
    }

    private fun setupModelDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            availableModels
        )

        binding.autoModel.setAdapter(adapter)
        binding.autoModel.setText(selectedModel, false)

        binding.autoModel.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedModel = parent.getItemAtPosition(position).toString()
            }
    }

    private fun setupTemperatureSlider() {
        binding.sliderTemperature.value = 0.7f
        binding.tvTemperature.text = "Temperature : 0.7"

        binding.sliderTemperature.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                temperature = value.toDouble()
                binding.tvTemperature.text = "Temperature : %.1f".format(value)
            }
        )
    }

    private fun setupTokenSlider() {
        binding.sliderTokens.value = 512f
        binding.tvMaxTokens.text = "Max Tokens : 512"

        binding.sliderTokens.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                maxTokens = value.toInt()
                binding.tvMaxTokens.text = "Max Tokens : $maxTokens"
            }
        )
    }

    private fun setupClickListeners() {
        binding.btnGenerate.setOnClickListener {
            val prompt = binding.etPrompt.text.toString().trim()

            if (prompt.isEmpty()) {
                Toast.makeText(this, "Please enter a prompt.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            generateResponse(prompt)
        }

        binding.btnEvaluate.setOnClickListener {
            val prompt = binding.etPrompt.text.toString().trim()
            val reference = binding.etReference.text.toString().trim()

            if (prompt.isEmpty()) {
                Toast.makeText(this, "Please enter a prompt.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CompareActivity::class.java).apply {
                putExtra("EXTRA_PROMPT", prompt)
                putExtra("EXTRA_REFERENCE", reference)
            }
            startActivity(intent)
        }

        binding.btnGoToCompare.setOnClickListener {
            val intent = Intent(this, CompareActivity::class.java)
            startActivity(intent)
        }

        binding.fabClear.setOnClickListener {
            binding.etPrompt.text?.clear()
            binding.etReference.text?.clear()
            binding.tvResponse.text = ""
            binding.cardResponse.visibility = View.GONE
        }
    }

    private fun generateResponse(prompt: String) {
        lifecycleScope.launch {
            binding.btnGenerate.isEnabled = false
            binding.cardResponse.visibility = View.VISIBLE
            binding.tvResponse.text = "Generating response..."

            val responseResource = repository.fetchCompletion(
                apiKey = Constants.OPENROUTER_API_KEY,
                model = selectedModel,
                prompt = prompt,
                temperature = temperature,
                maxTokens = maxTokens
            )

            binding.btnGenerate.isEnabled = true

            when (responseResource) {
                is Resource.Success -> {
                    binding.tvResponse.text = responseResource.data
                }
                is Resource.Error -> {
                    binding.tvResponse.text = "Error: ${responseResource.message}"
                    Toast.makeText(
                        this@MainActivity,
                        responseResource.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    binding.tvResponse.text = "Generating response..."
                }
            }
        }
    }
}