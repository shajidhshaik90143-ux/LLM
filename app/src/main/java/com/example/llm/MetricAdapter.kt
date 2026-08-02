package com.example.llm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm.databinding.ItemMetricBinding
import com.example.llm.model.Metric

class MetricAdapter(
    private var metricList: List<Metric> = emptyList()
) : RecyclerView.Adapter<MetricAdapter.MetricViewHolder>() {

    inner class MetricViewHolder(
        val binding: ItemMetricBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MetricViewHolder {

        val binding = ItemMetricBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MetricViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MetricViewHolder,
        position: Int
    ) {

        val metric = metricList[position]

        with(holder.binding) {

            tvMetricName.text = metric.name

            tvMetricScore.text = when (metric.score) {
                metric.score.toInt().toDouble() ->
                    metric.score.toInt().toString()

                else ->
                    String.format("%.4f", metric.score)
            }

            tvMetricDesc.text = metric.description
        }
    }

    override fun getItemCount(): Int = metricList.size

    fun updateMetrics(newMetrics: List<Metric>) {
        metricList = newMetrics
        notifyDataSetChanged()
    }
}