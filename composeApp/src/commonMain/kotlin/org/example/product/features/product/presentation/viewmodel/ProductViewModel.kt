package org.example.product.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.product.core.utils.Results
import org.example.product.features.product.domain.usecase.GetProductUseCase
import org.example.product.features.product.presentation.state.ProductState

class ProductViewModel (
    private val getProductsUseCase: GetProductUseCase,
): ViewModel(){
    private val _state = MutableStateFlow<ProductState>(ProductState.Idle)
    val state: StateFlow<ProductState> = _state

    fun getProduct(){
        viewModelScope.launch {
            _state.value = ProductState.Loading

            when ( val result = getProductsUseCase()) {
                is Results.Success -> {
                    _state.value = ProductState.Success(result.data)
                }
                is Results.Error -> {
                    _state.value = ProductState.Error(result.message)
                }
                else -> {}
            }


        }
    }
}