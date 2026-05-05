package com.juliansellanes.lab3_ex2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.juliansellanes.lab3_ex2.data.Movie
import com.juliansellanes.lab3_ex2.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MovieViewModel = viewModel()
) {
    val allMovies by viewModel.allMovies.collectAsState()
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    var showFavoritesOnly by rememberSaveable { mutableStateOf(false) }
    var selectedMovieId by rememberSaveable { mutableIntStateOf(-1) }

    val displayedMovies = if (showFavoritesOnly) favoriteMovies else allMovies
    val selectedMovie = allMovies.firstOrNull { it.id == selectedMovieId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DVD Movie Collection") },
                actions = {
                    TextButton(onClick = { showFavoritesOnly = false }) {
                        Text("All")
                    }
                    TextButton(onClick = { showFavoritesOnly = true }) {
                        Text("Favorites")
                    }
                    TextButton(onClick = { navController.navigate("add") }) {
                        Text("Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val isWideScreen = maxWidth >= 700.dp

            if (isWideScreen) {
                Row(modifier = Modifier.fillMaxSize()) {
                    MovieListSection(
                        movies = displayedMovies,
                        onSelect = { selectedMovieId = it.id },
                        onEdit = { navController.navigate("edit/${it.id}") },
                        onDelete = { viewModel.deleteMovie(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (selectedMovie != null) {
                            SelectedMovieDetail(movie = selectedMovie)
                        } else {
                            Text(
                                text = "Select a movie to view details",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            } else {
                MovieListSection(
                    movies = displayedMovies,
                    onSelect = { selectedMovieId = it.id },
                    onEdit = { navController.navigate("edit/${it.id}") },
                    onDelete = { viewModel.deleteMovie(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MovieListSection(
    movies: List<Movie>,
    onSelect: (Movie) -> Unit,
    onEdit: (Movie) -> Unit,
    onDelete: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Stored Movies (${movies.size})",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (movies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No movies found")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(movies) { movie ->
                    Column(
                        modifier = Modifier.clickable { onSelect(movie) }
                    ) {
                        MovieItem(
                            movie = movie,
                            onEdit = { onEdit(movie) },
                            onDelete = { onDelete(movie) },
                            onToggleFavorite = { onToggleFavorite(movie) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedMovieDetail(movie: Movie) {
    Column {
        Text(movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("ID: ${movie.id}", style = MaterialTheme.typography.titleMedium)
        Text("Director: ${movie.director}")
        Text("Price: $${movie.price}")
        Text("Release Date: ${movie.releaseDate}")
        Text("Duration: ${movie.durationMinutes} minutes")
        Text("Genre: ${movie.genre}")
        Text(if (movie.isFavorite) "Favorite movie" else "Not favorite")
    }
}