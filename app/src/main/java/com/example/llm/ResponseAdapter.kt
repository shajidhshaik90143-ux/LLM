package com.example.llm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm.databinding.ItemResponseBinding
import com.example.llm.model.EvaluationResult

class ResponseAdapter(
    private var results: List<EvaluationResult> = emptyList(),
    private val onItemClick: (EvaluationResult) -> Unit
) : RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder>() {

    inner class ResponseViewHolder(
        val binding: ItemResponseBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResponseViewHolder {

        val binding = ItemResponseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ResponseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ResponseViewHolder,
        position: Int
    ) {

        val item = results[position]

        with(holder.binding) {

            tvModelTitle.text = item.modelName
            tvResponseBody.text = item.responseText

            val accuracy =
                item.metrics.firstOrNull {
                    it.name.equals("Accuracy", true)
                }?.value ?: 0.0

            tvScorePreview.text =
                String.format("Accuracy: %.2f", accuracy)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateData(newResults: List<EvaluationResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}