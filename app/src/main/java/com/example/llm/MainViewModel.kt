package com.example.llm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.llm.model.EvaluationResult
import com.example.llm.repository.AIRepository
import com.example.llm.repository.EvaluationRepository
import com.example.llm.utils.Constants
import com.example.llm.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val aiRepository = AIRepository()
    private val evaluationRepository = EvaluationRepository()

    private val _evaluationState = MutableLiveData<Resource<EvaluationResult>>()

    val evaluationState: LiveData<Resource<EvaluationResult>>
        get() = _evaluationState

    fun runSingleEvaluation(
        prompt: String,
        referenceText: String,
        model: String = Constants.DEFAULT_MODEL_A
    ) {

        viewModelScope.launch {

            _evaluationState.value = Resource.Loading()

            val response = aiRepository.fetchCompletion(
                apiKey = Constants.OPENROUTER_API_KEY,
                model = model,
                prompt = prompt,
                temperature = 0.7,
                maxTokens = 512
            )

            when (response) {

                is Resource.Success -> {

                    val evaluation =
                        evaluationRepository.evaluateResponse(
                            modelName = model,
                            referenceText = referenceText,
                            responseText = response.data,
                            prompt = prompt
                        )

                    _evaluationState.value = Resource.Success(evaluation)
                }

                is Resource.Error -> {

                    _evaluationState.value = Resource.Error(
                        response.message
                    )
                }

                is Resource.Loading -> {
                    // No action needed
                }
            }
        }
    }
}