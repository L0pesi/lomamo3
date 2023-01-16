package com.lomamo.lomamo3.services

import com.lomamo.lomamo3.models.Movie
import com.lomamo.lomamo3.models.MovieInfo
import com.lomamo.lomamo3.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// We used the TMDB API
// that you can consult in https://www.themoviedb.org/settings/api
//
// API KEY 17ec993f382e241025f7a7205d71bb08


interface MovieApiInterface {

    @GET("/3/movie/popular?api_key=17ec993f382e241025f7a7205d71bb08")
    fun getMovieList(): Call<MovieResponse>

    @GET("/3/movie/{movie_id}?api_key=17ec993f382e241025f7a7205d71bb08")
    fun getMovieInfo(@Path("movie_id") movieId: Int): Call<MovieInfo>

}