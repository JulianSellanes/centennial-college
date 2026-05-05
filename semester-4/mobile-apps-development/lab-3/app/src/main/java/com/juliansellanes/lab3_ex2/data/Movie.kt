package com.juliansellanes.lab3_ex2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val director: String,
    val price: Double,
    val releaseDate: String,
    val durationMinutes: Int,
    val genre: String,
    val isFavorite: Boolean
)