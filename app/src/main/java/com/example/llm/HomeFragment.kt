package com.example.llm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm.databinding.FragmentHomeBinding
import com.example.llm.ui.ResultActivity
import com.example.llm.ui.adapter.ResponseAdapter
import com.example.llm.utils.Resource
import com.example.llm.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var responseAdapter: ResponseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {

        responseAdapter = ResponseAdapter(
            emptyList()
        ) { result ->

            val intent = Intent(
                requireContext(),
                ResultActivity::class.java
            )

            intent.putExtra("EXTRA_MODEL", result.modelName)
            intent.putExtra("EXTRA_RESPONSE", result.responseText)

            startActivity(intent)
        }

        binding.rvHomeResponses.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvHomeResponses.adapter = responseAdapter
    }

    private fun setupListeners() {

        binding.btnHomeExecute.setOnClickListener {

            val prompt =
                binding.etHomePrompt.text.toString().trim()

            val reference =
                binding.etHomeReference.text.toString().trim()

            if (prompt.isBlank()) {

                Toast.makeText(
                    requireContext(),
                    "Enter prompt",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (reference.isBlank()) {

                Toast.makeText(
                    requireContext(),
                    "Enter reference text",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            viewModel.runSingleEvaluation(
                prompt,
                reference
            )
        }
    }

    private fun observeViewModel() {

        viewModel.evaluationState.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {

                    binding.pbHomeLoading.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    binding.pbHomeLoading.visibility = View.GONE

                    resource.data?.let {

                        responseAdapter.updateData(
                            listOf(it)
                        )
                    }
                }

                is Resource.Error -> {

                    binding.pbHomeLoading.visibility = View.GONE

                    Toast.makeText(
                        requireContext(),
                        resource.message ?: "Execution failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}