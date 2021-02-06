package com.bignerdranch.android.androidacademy.data

import android.content.Context
import com.bignerdranch.android.androidacademy.AndroidAcademy.Companion.moviesDb
import com.bignerdranch.android.androidacademy.room.ActorEntity
import com.bignerdranch.android.androidacademy.room.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class MoviesRep {

    suspend fun loadMovies(): Page {
        return RetrofitModule().moviesApi.getMovies()
    }

    suspend fun loadDetails(id: Long): MovieDetails {
        return RetrofitModule().moviesApi.getMovieDetails(id)
    }

    suspend fun loadActors(id: Long): Credit {
        return RetrofitModule().moviesApi.getActors(id)
    }

    suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        moviesDb.movieDao.getAll().map { it.toMovie() }
    }

    suspend fun deleteAllMoviesAndSetNew(movies: List<Movie>) =
        withContext(Dispatchers.IO) {
            moviesDb.movieDao.deleteAll()
            for (movie in movies) {
                moviesDb.movieDao.insert(
                    movie.toMovieEntity())
            }
        }

    suspend fun getAllActors(): List<Cast> = withContext(Dispatchers.IO) {
        moviesDb.actorsDAO.getAll().map { it.toCast() }
    }

    suspend fun deleteAllActorsAndSetNew(casts: List<Cast>) = withContext(Dispatchers.IO) {
        moviesDb.actorsDAO.deleteAll()
        for (actor in casts)
            moviesDb.actorsDAO.insert(actor.toActorEntity())
    }


    private fun MovieEntity.toMovie() = Movie(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIDS = listOf(),
        overview = this.overview,
        title = this.title,
        popularity = this.popularity,
        voteCount = this.voteCount,
        voteAverage = this.voteAverage,
        posterPath = this.posterPath,
        releaseDate = "",
        originalTitle = "",
        originalLanguage = "",
        video = false,
        detail = MovieDetails(
            runtime = this.runtime,
            genres = this.genres.split(", ").map { Genre(id = 1, name = it) }
        )
    )

     private fun Movie.toMovieEntity() = MovieEntity(
            posterPath = this.posterPath!!,
            adult = this.adult,
            overview = this.overview,
            genres = this.detail!!.genres.joinToString { it.name },
            id = this.id,
            title = this.title,
            backdropPath = this.backdropPath,
            popularity = this.popularity,
            voteCount = this.voteCount,
            voteAverage = this.voteAverage,
            runtime = this.detail!!.runtime,

    )

    private fun Cast.toActorEntity() = ActorEntity(
        id = this.id,
        name = this.name,
        profilePath = this.profilePath ?: ""
    )

    private fun ActorEntity.toCast() = Cast(
        id = this.id,
        name = this.name,
        profilePath = this.profilePath
    )

}

interface MoviesApi {
    @GET("movie/popular?language=en-US&page=1")
    suspend fun getMovies(@Query("api_key") key: String = apiKey): Page

    @GET("movie/{movie_id}?language=en-US")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("api_key") key: String = apiKey
    ): MovieDetails

    @GET("movie/{movie_id}/credits?language=en-US")
    suspend fun getActors(
        @Path("movie_id") movieId: Long,
        @Query("api_key") key: String = apiKey
    ): Credit
}


const val apiKey = "71d3014534ead31bdf3e30983069d05f"
const val BASE_URL = "https://api.themoviedb.org/3/"