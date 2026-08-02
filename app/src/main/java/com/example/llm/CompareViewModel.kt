package com.example.llm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.llm.model.EvaluationResult
import com.example.llm.repository.AIRepository
import com.example.llm.repository.EvaluationRepository
import com.example.llm.utils.Resource
import kotlinx.coroutines.launch

class CompareViewModel : ViewModel() {

    private val aiRepository = AIRepository()
    private val evaluationRepository = EvaluationRepository()

    private val _resultA = MutableLiveData<Resource<EvaluationResult>>()
    val resultA: LiveData<Resource<EvaluationResult>> = _resultA

    private val _resultB = MutableLiveData<Resource<EvaluationResult>>()
    val resultB: LiveData<Resource<EvaluationResult>> = _resultB

    fun compareModels(
        apiKey: String,
        prompt: String,
        referenceText: String,
        modelA: String,
        modelB: String
    ) {

        loadModel(
            apiKey = apiKey,
            model = modelA,
            prompt = prompt,
            reference = referenceText,
            liveData = _resultA
        )

        loadModel(
            apiKey = apiKey,
            model = modelB,
            prompt = prompt,
            reference = referenceText,
            liveData = _resultB
        )
    }

    private fun loadModel(
        apiKey: String,
        model: String,
        prompt: String,
        reference: String,
        liveData: MutableLiveData<Resource<EvaluationResult>>
    ) {

        viewModelScope.launch {

            liveData.value = Resource.Loading()

            val response = aiRepository.fetchCompletion(
                apiKey = apiKey,
                model = model,
                prompt = prompt
            )

            when (response) {

                is Resource.Success -> {

                    val evaluation =
                        evaluationRepository.evaluateResponse(
                            modelName = model,
                            referenceText = reference,
                            responseText = response.data,
                            prompt = prompt
                        )

                    liveData.value = Resource.Success(evaluation)
                }

                is Resource.Error -> {

                    liveData.value = Resource.Error(
                        response.message
                    )
                }

                is Resource.Loading -> {
                    // Ignore
                }
            }
        }
    }
}