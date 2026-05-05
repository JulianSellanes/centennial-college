package com.juliansellanes.lab2_ex1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.time.ZoneOffset
import com.juliansellanes.lab2_ex1.ui.theme.Juliansellanes_COMP304401_Lab02_Ex01Theme

// Julian Sellanes (301494667)

// Exercise 1:

enum class Priority { LOW, MEDIUM, HIGH, CRITICAL }

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val heading: String,
    val description: String,
    val email: String,
    val priority: Priority,
    val dateCreated: LocalDate,
    val dueDate: LocalDate?
)

object TaskRepository {
    val tasks = mutableStateListOf<Task>()

    init {
        // At least one predefined task (required)
        tasks.add(
            Task(
                heading = "Welcome Task",
                description = "This is a predefined task. Tap to edit it!",
                email = "student@example.com",
                priority = Priority.MEDIUM,
                dateCreated = LocalDate.now(),
                dueDate = LocalDate.now().plusDays(7)
            )
        )
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun findById(taskId: String): Task? {
        return tasks.firstOrNull { it.id == taskId }
    }

    fun updateTask(taskId: String, updated: Task) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            // Replace element to trigger recomposition reliably
            tasks[index] = updated
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WritingTasksApp", "MainActivity onCreate")

        setContent {
            // If your project already has a Theme from the template,
            // wrap everything with it instead of MaterialTheme { }
            MaterialTheme {
                WritingTasksApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("WritingTasksApp", "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WritingTasksApp", "MainActivity onResume")
    }
}

private object Routes {
    const val HOME = "home"
    const val CREATE = "create"
    const val EDIT = "edit/{taskId}"

    fun edit(taskId: String) = "edit/$taskId"
}

@Composable
fun WritingTasksApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            // FAB only on Home screen (matches requirement)
            if (currentRoute == Routes.HOME) {
                FloatingActionButton(onClick = { navController.navigate(Routes.CREATE) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Create new task")
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    tasks = TaskRepository.tasks,
                    onTaskClick = { taskId -> navController.navigate(Routes.edit(taskId)) }
                )
            }

            composable(Routes.CREATE) {
                CreateTaskScreen(
                    onSave = { newTask ->
                        TaskRepository.addTask(newTask)
                        navController.popBackStack() // return to Home
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.EDIT,
                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
            ) { entry ->
                val taskId = entry.arguments?.getString("taskId")
                val task = taskId?.let { TaskRepository.findById(it) }

                if (taskId == null || task == null) {
                    // Simple fallback if something went wrong
                    MissingTaskScreen(onBack = { navController.popBackStack() })
                } else {
                    EditTaskScreen(
                        task = task,
                        onUpdate = { updated ->
                            TaskRepository.updateTask(taskId, updated)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

// Home Screen

@Composable
fun HomeScreen(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "WritingTasksApp",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        if (tasks.isEmpty()) {
            Text("No tasks yet. Tap + to create one.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(task = task, onClick = { onTaskClick(task.id) })
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.heading, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(task.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(10.dp))

            Text("Email: ${task.email}", style = MaterialTheme.typography.bodySmall)
            Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
            Text("Created: ${task.dateCreated.format(formatter)}", style = MaterialTheme.typography.bodySmall)

            val due = task.dueDate?.format(formatter) ?: "Not set"
            Text("Due: $due", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Create Screen

@Composable
fun CreateTaskScreen(
    onSave: (Task) -> Unit,
    onCancel: () -> Unit
) {
    val today = remember { LocalDate.now() }

    TaskForm(
        title = "Create New Task",
        initialHeading = "",
        initialDescription = "",
        initialEmail = "",
        initialPriority = Priority.LOW,
        dateCreated = today,
        initialDueDate = null,
        primaryButtonText = "Save",
        onPrimary = { heading, desc, email, priority, dueDate ->
            onSave(
                Task(
                    heading = heading,
                    description = desc,
                    email = email,
                    priority = priority,
                    dateCreated = today,
                    dueDate = dueDate
                )
            )
        },
        secondaryButtonText = "Cancel",
        onSecondary = onCancel
    )
}

// Edit Screen

@Composable
fun EditTaskScreen(
    task: Task,
    onUpdate: (Task) -> Unit
) {
    TaskForm(
        title = "View / Edit Task",
        initialHeading = task.heading,
        initialDescription = task.description,
        initialEmail = task.email,
        initialPriority = task.priority,
        dateCreated = task.dateCreated,   // keep original
        initialDueDate = task.dueDate,
        primaryButtonText = "Save / Update",
        onPrimary = { heading, desc, email, priority, dueDate ->
            onUpdate(
                task.copy(
                    heading = heading,
                    description = desc,
                    email = email,
                    priority = priority,
                    dueDate = dueDate
                )
            )
        },
        secondaryButtonText = "Back",
        onSecondary = { /* just go back */ onUpdate(task) } // will be replaced by caller popBackStack
    )
}

// Helper screen if task is missing

@Composable
fun MissingTaskScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Task not found.")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Go Back") }
    }
}

// Reusable Form (Create + Edit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm(
    title: String,
    initialHeading: String,
    initialDescription: String,
    initialEmail: String,
    initialPriority: Priority,
    dateCreated: LocalDate,
    initialDueDate: LocalDate?,
    primaryButtonText: String,
    onPrimary: (heading: String, desc: String, email: String, priority: Priority, dueDate: LocalDate?) -> Unit,
    secondaryButtonText: String,
    onSecondary: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }

    var heading by remember { mutableStateOf(initialHeading) }
    var description by remember { mutableStateOf(initialDescription) }
    var email by remember { mutableStateOf(initialEmail) }
    var priority by remember { mutableStateOf(initialPriority) }
    var dueDate by remember { mutableStateOf(initialDueDate) }

    // Simple validation (friendly I/O)
    var headingError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = heading,
            onValueChange = {
                heading = it
                headingError = null
            },
            label = { Text("Task heading") },
            modifier = Modifier.fillMaxWidth(),
            isError = headingError != null,
            supportingText = { if (headingError != null) Text(headingError!!) }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError!!) }
        )

        PriorityDropdown(
            selected = priority,
            onSelected = { priority = it }
        )

        // Date created (auto today, read-only)
        OutlinedTextField(
            value = dateCreated.format(formatter),
            onValueChange = { },
            label = { Text("Date created (auto)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false
        )

        DueDatePickerField(
            dueDate = dueDate,
            onDueDateChange = { dueDate = it }
        )

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // Basic validation using control structures (Kotlin basics)
                    var ok = true

                    if (heading.trim().isEmpty()) {
                        headingError = "Heading is required"
                        ok = false
                    }
                    if (!email.contains("@") || !email.contains(".")) {
                        emailError = "Enter a valid email"
                        ok = false
                    }

                    if (ok) {
                        onPrimary(heading.trim(), description.trim(), email.trim(), priority, dueDate)
                    }
                }
            ) {
                Text(primaryButtonText)
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onSecondary
            ) {
                Text(secondaryButtonText)
            }
        }
    }
}

// Priority Dropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    selected: Priority,
    onSelected: (Priority) -> Unit
) {
    val options = remember { Priority.values().toList() }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = { },
            label = { Text("Priority") },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { p ->
                DropdownMenuItem(
                    text = { Text(p.name) },
                    onClick = {
                        onSelected(p)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Due Date Picker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePickerField(
    dueDate: LocalDate?,
    onDueDateChange: (LocalDate?) -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }
    var showDialog by remember { mutableStateOf(false) }

    val state = rememberDatePickerState()
    val displayText = dueDate?.format(formatter) ?: "Click to select"

    Box(Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            label = { Text("Due date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDialog = true }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val picked = state.selectedDateMillis?.let { ms ->
                        Instant.ofEpochMilli(ms)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                    }
                    onDueDateChange(picked)
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    MaterialTheme {
        TaskCard(
            task = Task(
                heading = "Preview Task",
                description = "This is how the task card will look in the Home screen.",
                email = "preview@example.com",
                priority = Priority.HIGH,
                dateCreated = LocalDate.now(),
                dueDate = LocalDate.now().plusDays(3)
            ),
            onClick = { }
        )
    }
}