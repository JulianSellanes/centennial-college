package com.juliansellanes.lab3_ex2.data

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {
    val allMovies: Flow<List<Movie>> = dao.getAllMovies()
    val favoriteMovies: Flow<List<Movie>> = dao.getFavoriteMovies()

    fun getMovieById(movieId: Int): Flow<Movie?> = dao.getMovieById(movieId)

    suspend fun addMovie(movie: Movie): Long = dao.insert(movie)

    suspend fun updateMovie(movie: Movie) = dao.update(movie)

    suspend fun deleteMovie(movie: Movie) = dao.delete(movie)

    suspend fun insertSampleMovies(movies: List<Movie>) = dao.insertSampleMovies(movies)
}