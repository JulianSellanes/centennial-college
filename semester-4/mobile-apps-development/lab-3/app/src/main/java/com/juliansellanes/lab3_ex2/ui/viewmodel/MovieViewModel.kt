package com.juliansellanes.lab3_ex2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.juliansellanes.lab3_ex2.data.Movie
import com.juliansellanes.lab3_ex2.data.MovieDatabase
import com.juliansellanes.lab3_ex2.data.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository =
        MovieRepository(MovieDatabase.getDatabase(application).movieDao())

    val allMovies: StateFlow<List<Movie>> = repository.allMovies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteMovies: StateFlow<List<Movie>> = repository.favoriteMovies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    data class MovieFormState(
        val id: String = "",
        val title: String = "",
        val director: String = "",
        val price: String = "",
        val releaseDate: String = "",
        val durationMinutes: String = "",
        val genre: String = "",
        val isFavorite: Boolean = false,
        val errors: List<String> = emptyList()
    )

    private val _movieFormState = MutableStateFlow(MovieFormState())
    val movieFormState: StateFlow<MovieFormState> = _movieFormState.asStateFlow()

    private val _addMovieSuccess = MutableStateFlow(false)
    val addMovieSuccess: StateFlow<Boolean> = _addMovieSuccess.asStateFlow()

    fun updateFormState(
        id: String? = null,
        title: String? = null,
        director: String? = null,
        price: String? = null,
        releaseDate: String? = null,
        durationMinutes: String? = null,
        genre: String? = null,
        isFavorite: Boolean? = null
    ) {
        _movieFormState.update { current ->
            current.copy(
                id = id ?: current.id,
                title = title ?: current.title,
                director = director ?: current.director,
                price = price ?: current.price,
                releaseDate = releaseDate ?: current.releaseDate,
                durationMinutes = durationMinutes ?: current.durationMinutes,
                genre = genre ?: current.genre,
                isFavorite = isFavorite ?: current.isFavorite
            )
        }
    }

    fun validateAndAddMovie() {
        val state = _movieFormState.value
        val errors = mutableListOf<String>()

        val idValue = state.id.toIntOrNull()
        val priceValue = state.price.toDoubleOrNull()
        val durationValue = state.durationMinutes.toIntOrNull()

        if (idValue == null || idValue !in 101..999) {
            errors.add("Movie ID must be between 101 and 999")
        } else if (allMovies.value.any { it.id == idValue }) {
            errors.add("A movie with this ID already exists")
        }

        if (state.title.isBlank()) {
            errors.add("Title is required")
        }

        if (state.director.isBlank()) {
            errors.add("Director name is required")
        }

        if (priceValue == null || priceValue <= 0) {
            errors.add("Price must be positive")
        }

        if (!isValidDate(state.releaseDate)) {
            errors.add("Release date is required")
        }

        if (durationValue == null || durationValue <= 0) {
            errors.add("Duration must be greater than 0 minutes")
        }

        if (state.genre.isBlank()) {
            errors.add("Genre is required")
        }

        if (errors.isNotEmpty()) {
            _movieFormState.update { it.copy(errors = errors) }
            _addMovieSuccess.value = false
            return
        }

        val movie = Movie(
            id = idValue!!,
            title = state.title.trim(),
            director = state.director.trim(),
            price = priceValue!!,
            releaseDate = state.releaseDate,
            durationMinutes = durationValue!!,
            genre = state.genre,
            isFavorite = state.isFavorite
        )

        viewModelScope.launch {
            val result = repository.addMovie(movie)
            if (result == -1L) {
                _movieFormState.update {
                    it.copy(errors = listOf("A movie with this ID already exists"))
                }
                _addMovieSuccess.value = false
            } else {
                _movieFormState.value = MovieFormState()
                _addMovieSuccess.value = true
            }
        }
    }

    fun resetAddMovieSuccess() {
        _addMovieSuccess.value = false
    }

    fun getMovieById(movieId: Int): Flow<Movie?> = repository.getMovieById(movieId)

    fun updateMovie(movie: Movie) = viewModelScope.launch {
        repository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun toggleFavorite(movie: Movie) = viewModelScope.launch {
        repository.updateMovie(movie.copy(isFavorite = !movie.isFavorite))
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
}