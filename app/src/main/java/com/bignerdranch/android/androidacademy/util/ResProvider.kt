package com.bignerdranch.android.androidacademy.util

import android.content.Context
import com.bignerdranch.android.androidacademy.data.Movie
import com.bignerdranch.android.androidacademy.data.MoviesLoad

class ResProvider(val context: Context): IResProvider{

    override suspend fun getLoadedMovies(): List<Movie> {
        return MoviesLoad().loadMovies()
    }
}