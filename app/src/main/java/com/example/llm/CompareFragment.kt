package com.example.llm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm.databinding.FragmentCompareBinding
import com.example.llm.ui.adapter.MetricAdapter
import com.example.llm.utils.Constants
import com.example.llm.utils.Resource
import com.example.llm.viewmodel.CompareViewModel

class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompareViewModel by viewModels()

    private lateinit var adapterA: MetricAdapter
    private lateinit var adapterB: MetricAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCompareBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {

        adapterA = MetricAdapter()
        adapterB = MetricAdapter()

        binding.rvFragMetricsA.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterA
        }

        binding.rvFragMetricsB.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterB
        }
    }

    private fun setupListeners() {

        binding.btnFragCompare.setOnClickListener {

            val prompt = binding.etFragPrompt.text.toString().trim()
            val reference = binding.etFragReference.text.toString().trim()

            if (prompt.isBlank()) {
                Toast.makeText(requireContext(), "Enter prompt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (reference.isBlank()) {
                Toast.makeText(requireContext(), "Enter reference text", Toast.LENGTH_SHORT).show()
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

        viewModel.resultA.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {
                    binding.pbFragA.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    binding.pbFragA.visibility = View.GONE

                    resource.data?.let {

                        binding.tvFragModelA.text = it.modelName
                        binding.tvFragResponseA.text = it.responseText

                        adapterA.updateMetrics(it.metrics)
                    }
                }

                is Resource.Error -> {

                    binding.pbFragA.visibility = View.GONE

                    binding.tvFragResponseA.text =
                        resource.message ?: "Unknown error"
                }
            }
        }

        viewModel.resultB.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {
                    binding.pbFragB.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    binding.pbFragB.visibility = View.GONE

                    resource.data?.let {

                        binding.tvFragModelB.text = it.modelName
                        binding.tvFragResponseB.text = it.responseText

                        adapterB.updateMetrics(it.metrics)
                    }
                }

                is Resource.Error -> {

                    binding.pbFragB.visibility = View.GONE

                    binding.tvFragResponseB.text =
                        resource.message ?: "Unknown error"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}