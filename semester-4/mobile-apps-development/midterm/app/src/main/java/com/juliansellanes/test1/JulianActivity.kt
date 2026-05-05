package com.juliansellanes.test1

// Julian Sellanes (301494667)

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold

class JulianActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                MainScreen(
                    onOpenContacts = {
                        startActivity(Intent(this, SellanesActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(onOpenContacts: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("BusinessContacts") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MY BUSINESS CONTACTS",
                style = MaterialTheme.typography.headlineSmall
            )

            FilledTonalIconButton(
                onClick = onOpenContacts,
                modifier = Modifier.size(88.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Badge,
                    contentDescription = "Open Contacts",
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "Tap the icon to add & view contacts",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}