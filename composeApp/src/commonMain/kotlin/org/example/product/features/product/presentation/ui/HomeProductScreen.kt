package org.example.product.features.product.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.product.features.product.presentation.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeProductScreen(
    viewModel: ProductViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProduct()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ini Layar Home / Dashboard")
    }
}