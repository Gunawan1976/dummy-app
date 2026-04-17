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

    private var currentSkip = 0
    private val limit = 10

    fun getProduct() {
        if (_state.value is ProductState.Loading) return
        
        viewModelScope.launch {
            _state.value = ProductState.Loading
            currentSkip = 0
            
            when (val result = getProductsUseCase(limit = limit, skip = currentSkip)) {
                is Results.Success -> {
                    _state.value = ProductState.Success(
                        products = result.data,
                        isEndReached = result.data.size < limit
                    )
                    currentSkip += limit
                }
                is Results.Error -> {
                    _state.value = ProductState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun loadMore() {
        val currentState = _state.value
        if (currentState is ProductState.Success && !currentState.isEndReached && !currentState.isLoadingMore) {
            _state.value = currentState.copy(isLoadingMore = true)
            
            viewModelScope.launch {
                when (val result = getProductsUseCase(limit = limit, skip = currentSkip)) {
                    is Results.Success -> {
                        val updatedProducts = currentState.products + result.data
                        _state.value = ProductState.Success(
                            products = updatedProducts,
                            isEndReached = result.data.size < limit,
                            isLoadingMore = false
                        )
                        currentSkip += limit
                    }
                    is Results.Error -> {
                        // In case of error while loading more, we might want to just stop loading more
                        _state.value = currentState.copy(isLoadingMore = false)
                    }
                    else -> {
                        _state.value = currentState.copy(isLoadingMore = false)
                    }
                }
            }
        }
    }
}