package com.juliansellanes.lab3_ex2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.juliansellanes.lab3_ex2.data.Movie

@Composable
fun MovieItem(
    movie: Movie,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Movie ${movie.title} directed by ${movie.director}"
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(movie.title, style = MaterialTheme.typography.titleMedium)
            Text("ID: ${movie.id}")
            Text("Director: ${movie.director}")
            Text("Price: $${movie.price}")
            Text("Release Date: ${movie.releaseDate}")
            Text("Duration: ${movie.durationMinutes} minutes")
            Text("Genre: ${movie.genre}")
            Text(
                text = if (movie.isFavorite) "Favorite: Yes" else "Favorite: No",
                color = if (movie.isFavorite) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onToggleFavorite) {
                    Text(if (movie.isFavorite) "Unfavorite" else "Favorite")
                }

                OutlinedButton(onClick = onEdit) {
                    Text("Edit")
                }

                OutlinedButton(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}