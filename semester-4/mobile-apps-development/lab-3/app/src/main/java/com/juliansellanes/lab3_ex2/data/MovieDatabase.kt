package com.juliansellanes.lab3_ex2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).movieDao().insertSampleMovies(
                                    listOf(
                                        Movie(
                                            id = 101,
                                            title = "Toy Story",
                                            director = "John Lasseter",
                                            price = 19.99,
                                            releaseDate = "1995-11-22",
                                            durationMinutes = 81,
                                            genre = "Family",
                                            isFavorite = true
                                        ),
                                        Movie(
                                            id = 102,
                                            title = "Inception",
                                            director = "Christopher Nolan",
                                            price = 24.99,
                                            releaseDate = "2010-07-16",
                                            durationMinutes = 148,
                                            genre = "Thriller",
                                            isFavorite = false
                                        ),
                                        Movie(
                                            id = 103,
                                            title = "The Dark Knight",
                                            director = "Christopher Nolan",
                                            price = 22.99,
                                            releaseDate = "2008-07-18",
                                            durationMinutes = 152,
                                            genre = "Action",
                                            isFavorite = false
                                        )
                                    )
                                )
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}