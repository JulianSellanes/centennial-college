package com.juliansellanes.lab1ex1

// Julian Sellanes (301494667)

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LoanCalculatorScreen()
            }
        }
    }
}

@Composable
fun LoanCalculatorScreen() {
    val context = LocalContext.current

    var carPriceText by remember { mutableStateOf("") }
    var downPaymentText by remember { mutableStateOf("") }
    var interestRateText by remember { mutableStateOf("") }
    var yearsText by remember { mutableStateOf("") }

    var monthlyPayment by remember { mutableStateOf(0.0) }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun resetAll() {
        carPriceText = ""
        downPaymentText = ""
        interestRateText = ""
        yearsText = ""
        monthlyPayment = 0.0
    }

    fun calculate() {
        // Missing checks (Toast requirement)
        if (carPriceText.isBlank()) { showToast("Please enter Car Price"); return }
        if (downPaymentText.isBlank()) { showToast("Please enter Down Payment"); return }
        if (interestRateText.isBlank()) { showToast("Please enter Interest Rate"); return }
        if (yearsText.isBlank()) { showToast("Please enter Loan Duration (years)"); return }

        // Parse numbers
        val carPrice = carPriceText.toDoubleOrNull()
        val downPayment = downPaymentText.toDoubleOrNull()
        val annualRate = interestRateText.toDoubleOrNull()
        val years = yearsText.toIntOrNull()

        if (carPrice == null || carPrice <= 0) { showToast("Car Price must be a number > 0"); return }
        if (downPayment == null || downPayment < 0) { showToast("Down Payment must be a number ≥ 0"); return }
        if (annualRate == null || annualRate < 0) { showToast("Interest Rate must be a number ≥ 0"); return }
        if (years == null || years <= 0) { showToast("Loan Duration must be an integer > 0"); return }
        if (downPayment > carPrice) { showToast("Down Payment cannot be greater than Car Price"); return }

        val principal = carPrice - downPayment
        val months = years * 12

        val monthlyRate = (annualRate / 100.0) / 12.0

        monthlyPayment = if (monthlyRate == 0.0) {
            // No interest: simple division
            principal / months
        } else {
            // Standard amortized loan formula:
            // M = P * r * (1+r)^n / ((1+r)^n - 1)
            val pow = (1.0 + monthlyRate).pow(months.toDouble())
            principal * monthlyRate * pow / (pow - 1.0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))

        Text(
            text = "Car Loan Calculator",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(18.dp))

        Text(text = "Monthly Payment", fontSize = 18.sp)

        Text(
            text = "$" + String.format(Locale.US, "%.2f", monthlyPayment),
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(26.dp))

        OutlinedTextField(
            value = carPriceText,
            onValueChange = { carPriceText = it },
            label = { Text("Car Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = downPaymentText,
            onValueChange = { downPaymentText = it },
            label = { Text("Down Payment") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = interestRateText,
            onValueChange = { interestRateText = it },
            label = { Text("Interest Rate (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = yearsText,
            onValueChange = { yearsText = it },
            label = { Text("Loan Duration (in years)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { calculate() },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text("CALCULATE", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = { resetAll() },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text("RESET", fontWeight = FontWeight.Bold)
            }
        }
    }
}
