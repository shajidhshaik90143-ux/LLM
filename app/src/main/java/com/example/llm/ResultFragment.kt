package com.example.llm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm.databinding.FragmentResultBinding
import com.example.llm.model.Metric
import com.example.llm.ui.adapter.MetricAdapter

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var metricAdapter: MetricAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelName = arguments?.getString(ARG_MODEL) ?: "LLM Result"
        val responseText = arguments?.getString(ARG_RESPONSE) ?: ""

        binding.tvFragResultModel.text = modelName
        binding.tvFragResultText.text = responseText

        metricAdapter = MetricAdapter()

        binding.rvFragResultMetrics.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = metricAdapter
        }

        val metricsList = listOf(
            Metric("Accuracy", 0.88, "N-gram word overlap with ground truth."),
            Metric("Coherence", 0.91, "Sentence length variation score."),
            Metric("Perplexity", 10.20, "Approximated language model perplexity.")
        )

        metricAdapter.updateMetrics(metricsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MODEL = "ARG_MODEL"
        private const val ARG_RESPONSE = "ARG_RESPONSE"

        fun newInstance(
            modelName: String,
            responseText: String
        ): ResultFragment {

            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MODEL, modelName)
                    putString(ARG_RESPONSE, responseText)
                }
            }
        }
    }
}