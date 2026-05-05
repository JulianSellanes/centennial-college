package com.juliansellanes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.juliansellanes.data.AppDatabase
import com.juliansellanes.data.StockInfo
import com.juliansellanes.data.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StockFormState(
    val stockSymbol: String = "",
    val companyName: String = "",
    val stockQuote: String = "",
    val sharesSold: String = "",
    val errors: List<String> = emptyList(),
    val message: String? = null
)

class StockViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StockRepository(
        AppDatabase.getDatabase(application).stockDao()
    )

    val allStocks: StateFlow<List<StockInfo>> = repository.allStocks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _formState = MutableStateFlow(StockFormState())
    val formState: StateFlow<StockFormState> = _formState.asStateFlow()

    fun updateStockSymbol(value: String) {
        _formState.update {
            it.copy(
                stockSymbol = normalizeSymbol(value),
                errors = emptyList(),
                message = null
            )
        }
    }

    fun updateCompanyName(value: String) {
        _formState.update {
            it.copy(
                companyName = value,
                errors = emptyList(),
                message = null
            )
        }
    }

    fun updateStockQuote(value: String) {
        _formState.update {
            it.copy(
                stockQuote = value,
                errors = emptyList(),
                message = null
            )
        }
    }

    fun updateSharesSold(value: String) {
        _formState.update {
            it.copy(
                sharesSold = value.filter { ch -> ch.isDigit() },
                errors = emptyList(),
                message = null
            )
        }
    }

    fun insertStock() {
        val state = _formState.value
        val errors = mutableListOf<String>()

        val normalizedSymbol = normalizeSymbol(state.stockSymbol)
        val trimmedCompanyName = state.companyName.trim()
        val parsedQuote = state.stockQuote.toDoubleOrNull()
        val parsedSharesSold = state.sharesSold.toIntOrNull()

        if (normalizedSymbol.isBlank()) {
            errors.add("Stock symbol cannot be empty.")
        }

        if (trimmedCompanyName.isBlank()) {
            errors.add("Company name cannot be empty.")
        }

        if (parsedQuote == null || parsedQuote <= 0.0) {
            errors.add("Stock quote must be greater than 0.")
        }

        if (parsedSharesSold == null || parsedSharesSold <= 0) {
            errors.add("Shares sold must be greater than 0.")
        }

        if (errors.isNotEmpty()) {
            _formState.update {
                it.copy(errors = errors, message = null)
            }
            return
        }

        viewModelScope.launch {
            val result = repository.insertStock(
                StockInfo(
                    stockSymbol = normalizedSymbol,
                    companyName = trimmedCompanyName,
                    stockQuote = parsedQuote!!,
                    sharesSold = parsedSharesSold!!
                )
            )

            if (result == -1L) {
                _formState.update {
                    it.copy(
                        errors = listOf("This stock symbol already exists."),
                        message = null
                    )
                }
            } else {
                _formState.value = StockFormState(
                    message = "Stock inserted successfully."
                )
            }
        }
    }

    fun clearMessage() {
        _formState.update {
            it.copy(message = null)
        }
    }

    fun getStockBySymbol(symbol: String): Flow<StockInfo?> {
        return repository.getStockBySymbol(normalizeSymbol(symbol))
    }

    private fun normalizeSymbol(value: String): String {
        return value.trim().replace(" ", "").uppercase()
    }
}