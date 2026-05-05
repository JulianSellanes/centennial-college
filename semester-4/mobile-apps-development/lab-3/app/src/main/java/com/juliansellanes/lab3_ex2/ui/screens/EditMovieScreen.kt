package com.juliansellanes.lab3_ex2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.juliansellanes.lab3_ex2.data.Movie
import com.juliansellanes.lab3_ex2.ui.viewmodel.MovieViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieScreen(
    navController: NavController,
    movieId: Int?,
    viewModel: MovieViewModel = viewModel()
) {
    if (movieId == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    val movie by viewModel.getMovieById(movieId).collectAsState(initial = null)

    if (movie == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    EditMovieContent(
        movie = movie!!,
        navController = navController,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMovieContent(
    movie: Movie,
    navController: NavController,
    viewModel: MovieViewModel
) {
    var title by remember(movie.id) { mutableStateOf(movie.title) }
    var director by remember(movie.id) { mutableStateOf(movie.director) }
    var price by remember(movie.id) { mutableStateOf(movie.price.toString()) }
    var releaseDate by remember(movie.id) { mutableStateOf(movie.releaseDate) }
    var durationMinutes by remember(movie.id) { mutableStateOf(movie.durationMinutes.toString()) }
    var genre by remember(movie.id) { mutableStateOf(movie.genre) }
    var isFavorite by remember(movie.id) { mutableStateOf(movie.isFavorite) }
    var errorMessage by remember { mutableStateOf("") }

    val currentNavController by rememberUpdatedState(navController)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View / Edit Movie") },
                navigationIcon = {
                    TextButton(onClick = { currentNavController.popBackStack() }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = movie.id.toString(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Movie ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = director,
                onValueChange = { director = it },
                label = { Text("Director") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price of DVD") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReleaseDateField(
                value = releaseDate,
                onDateSelected = { releaseDate = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = durationMinutes,
                onValueChange = { durationMinutes = it.filter(Char::isDigit) },
                label = { Text("Duration in minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            GenreDropdown(
                selectedGenre = genre,
                onGenreSelected = { genre = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Favorite")
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.deleteMovie(movie)
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }

                Button(
                    onClick = {
                        val priceValue = price.toDoubleOrNull()
                        val durationValue = durationMinutes.toIntOrNull()

                        when {
                            title.isBlank() -> errorMessage = "Title is required"
                            director.isBlank() -> errorMessage = "Director is required"
                            priceValue == null || priceValue <= 0 -> errorMessage = "Price must be positive"
                            !isValidDate(releaseDate) -> errorMessage = "Release date is required"
                            durationValue == null || durationValue <= 0 -> errorMessage = "Duration must be greater than 0"
                            genre.isBlank() -> errorMessage = "Genre is required"
                            else -> {
                                errorMessage = ""
                                viewModel.updateMovie(
                                    movie.copy(
                                        title = title.trim(),
                                        director = director.trim(),
                                        price = priceValue,
                                        releaseDate = releaseDate,
                                        durationMinutes = durationValue,
                                        genre = genre,
                                        isFavorite = isFavorite
                                    )
                                )
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

private fun isValidDate(value: String): Boolean {
    if (value.isBlank()) return false
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.isLenient = false
        formatter.parse(value)
        true
    } catch (_: Exception) {
        false
    }
}