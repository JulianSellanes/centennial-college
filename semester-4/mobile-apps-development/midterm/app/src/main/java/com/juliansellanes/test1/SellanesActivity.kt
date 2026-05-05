package com.juliansellanes.test1

// Julian Sellanes (301494667)

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.ArrowBack

class SellanesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                ContactsScreen()
            }
        }
    }
}

private enum class Screen { List, Add }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsScreen(vm: ContactsViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var screen by rememberSaveable { mutableStateOf(Screen.List) }

    var idText by rememberSaveable { mutableStateOf("") }
    var nameText by rememberSaveable { mutableStateOf("") }
    var phoneText by rememberSaveable { mutableStateOf("") }
    var emailText by rememberSaveable { mutableStateOf("") }

    var contactType by rememberSaveable { mutableStateOf(ContactType.Business) }

    var favExpanded by remember { mutableStateOf(false) }
    var favorite by rememberSaveable { mutableStateOf(FavoriteLevel.Normal) }

    fun idError(): String? {
        if (idText.isBlank()) return "ID is required"
        if (!idText.all { it.isDigit() }) return "ID must be numeric"
        if (idText.length != 4) return "ID must be 4 digits"
        val value = idText.toIntOrNull() ?: return "Invalid ID"
        if (value !in 1001..9999) return "ID must be between 1001 and 9999"
        return null
    }

    fun nameError(): String? = if (nameText.trim().isEmpty()) "Name is required" else null

    fun phoneError(): String? {
        if (phoneText.isBlank()) return "Cell phone is required"
        if (!phoneText.all { it.isDigit() }) return "Phone must be numeric"
        if (phoneText.length != 10) return "Phone must be 10 digits"
        return null
    }

    fun emailError(): String? {
        if (emailText.isBlank()) return "Email is required"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) return "Invalid email format"
        return null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (screen == Screen.List) "Contacts" else "Add Contact") },
                navigationIcon = {
                    if (screen == Screen.Add) {
                        IconButton(onClick = { screen = Screen.List }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (screen == Screen.List) {
                FloatingActionButton(onClick = { screen = Screen.Add }) {
                    Icon(Icons.Filled.Add, contentDescription = "New Contact")
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        val idErr = idError()
                        val nErr = nameError()
                        val pErr = phoneError()
                        val eErr = emailError()

                        if (idErr != null || nErr != null || pErr != null || eErr != null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fix the form errors first.")
                            }
                            return@FloatingActionButton
                        }

                        val contact = Contact(
                            id = idText.toInt(),
                            name = nameText.trim(),
                            phone = phoneText,
                            email = emailText.trim(),
                            contactType = contactType,
                            favorite = favorite
                        )

                        vm.addContact(contact)

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Added: ${contact.id} | ${contact.name} | ${contact.phone} | ${contact.email} | ${contact.contactType} | ${contact.favorite}"
                            )
                        }

                        // clear fields
                        idText = ""
                        nameText = ""
                        phoneText = ""
                        emailText = ""
                        contactType = ContactType.Business
                        favorite = FavoriteLevel.Normal

                        screen = Screen.List
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Contact")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (screen == Screen.List) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(vm.contacts, key = { it.id }) { c ->
                        ContactCard(c)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    OutlinedTextField(
                        value = idText,
                        onValueChange = { if (it.length <= 4) idText = it.trim() },
                        label = { Text("ID (1001–9999)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = idError() != null,
                        supportingText = { idError()?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("Name") },
                        singleLine = true,
                        isError = nameError() != null,
                        supportingText = { nameError()?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phoneText,
                        onValueChange = { if (it.length <= 10) phoneText = it.trim() },
                        label = { Text("Cell Phone (10 digits)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError() != null,
                        supportingText = { phoneError()?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailText,
                        onValueChange = { emailText = it.trim() },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError() != null,
                        supportingText = { emailError()?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Contact Type", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = contactType == ContactType.Business,
                                        onClick = { contactType = ContactType.Business }
                                    )
                                    Text("Business")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = contactType == ContactType.Work,
                                        onClick = { contactType = ContactType.Work }
                                    )
                                    Text("Work")
                                }
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = favExpanded,
                        onExpandedChange = { favExpanded = !favExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = favorite.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Favorite") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = favExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = favExpanded,
                            onDismissRequest = { favExpanded = false }
                        ) {
                            FavoriteLevel.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name) },
                                    onClick = {
                                        favorite = option
                                        favExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactCard(c: Contact) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${c.id} - ${c.name}", style = MaterialTheme.typography.titleMedium)
            Text("Phone: ${c.phone}")
            Text("Email: ${c.email}")
            Text("Type: ${c.contactType}")
            Text("Favorite: ${c.favorite}")
        }
    }
}