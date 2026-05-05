//Julian Sellanes (301494667)

package com.juliansellanes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juliansellanes.data.StockInfo
import com.juliansellanes.viewmodel.StockViewModel
import java.util.Locale

class SellanesActivity : ComponentActivity() {

    private val stockViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val requestedSymbol = intent.getStringExtra(EXTRA_STOCK_SYMBOL).orEmpty()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val stock by stockViewModel
                        .getStockBySymbol(requestedSymbol)
                        .collectAsState(initial = null)

                    SellanesScreen(
                        stock = stock,
                        requestedSymbol = requestedSymbol,
                        onBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_STOCK_SYMBOL = "extra_stock_symbol"
    }
}

@Composable
fun SellanesScreen(
    stock: StockInfo?,
    requestedSymbol: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Stock Details",
                    style = MaterialTheme.typography.headlineSmall
                )

                if (stock == null) {
                    Text(
                        text = if (requestedSymbol.isBlank()) {
                            "No stock symbol was received."
                        } else {
                            "Stock not found for symbol: $requestedSymbol"
                        }
                    )
                } else {
                    Text("Symbol: ${stock.stockSymbol}")
                    Text("Company: ${stock.companyName}")
                    Text("Stock Quote: $${formatQuote(stock.stockQuote)}")
                    Text("Shares Sold: ${stock.sharesSold}")
                }

                Button(onClick = onBack) {
                    Text("Back")
                }
            }
        }
    }
}

private fun formatQuote(value: Double): String {
    return String.format(Locale.getDefault(), "%.2f", value)
}