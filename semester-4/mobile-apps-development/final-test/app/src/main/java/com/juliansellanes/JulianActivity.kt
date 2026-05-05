//Julian Sellanes (301494667)

package com.juliansellanes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.juliansellanes.data.StockInfo
import com.juliansellanes.viewmodel.StockViewModel
import java.util.Locale

class JulianActivity : ComponentActivity() {

    private val stockViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    JulianScreen(
                        viewModel = stockViewModel,
                        onOpenStockDetails = { symbol ->
                            val intent = Intent(this, SellanesActivity::class.java).apply {
                                putExtra(SellanesActivity.EXTRA_STOCK_SYMBOL, symbol)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JulianScreen(
    viewModel: StockViewModel,
    onOpenStockDetails: (String) -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val allStocks by viewModel.allStocks.collectAsState()

    var selectedSymbol by rememberSaveable { mutableStateOf("") }
    var searchSymbol by rememberSaveable { mutableStateOf("") }
    var actionError by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock Entry - JulianActivity") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = formState.stockSymbol,
                    onValueChange = { viewModel.updateStockSymbol(it) },
                    label = { Text("Stock Symbol") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = formState.companyName,
                    onValueChange = { viewModel.updateCompanyName(it) },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = formState.stockQuote,
                    onValueChange = { viewModel.updateStockQuote(it) },
                    label = { Text("Stock Quote") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = formState.sharesSold,
                    onValueChange = { viewModel.updateSharesSold(it) },
                    label = { Text("Shares Sold") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        actionError = ""
                        viewModel.insertStock()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Insert Stock")
                }

                if (formState.errors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    formState.errors.forEach { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                formState.message?.let { message ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Stocks in Database:",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (allStocks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No stocks inserted yet.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allStocks) { stock ->
                        StockRowItem(
                            stock = stock,
                            isSelected = selectedSymbol == stock.stockSymbol,
                            onClick = {
                                selectedSymbol = stock.stockSymbol
                                searchSymbol = stock.stockSymbol
                                actionError = ""
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchSymbol,
                onValueChange = {
                    searchSymbol = it.uppercase()
                    actionError = ""
                },
                label = { Text("Search Stock Symbol") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (actionError.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = actionError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val normalizedSearch = searchSymbol.trim().replace(" ", "").uppercase()
                        when {
                            normalizedSearch.isBlank() -> {
                                actionError = "Enter a stock symbol to search."
                            }

                            allStocks.none { it.stockSymbol == normalizedSearch } -> {
                                actionError = "Stock symbol not found."
                            }

                            else -> {
                                actionError = ""
                                onOpenStockDetails(normalizedSearch)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Search")
                }

                Button(
                    onClick = {
                        if (selectedSymbol.isBlank()) {
                            actionError = "Select a stock from the list first."
                        } else {
                            actionError = ""
                            onOpenStockDetails(selectedSymbol)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Display Stock Info")
                }
            }
        }
    }
}

@Composable
private fun StockRowItem(
    stock: StockInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stock.stockSymbol,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stock.companyName,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Quote: $${formatQuote(stock.stockQuote)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

private fun formatQuote(value: Double): String {
    return String.format(Locale.getDefault(), "%.2f", value)
}