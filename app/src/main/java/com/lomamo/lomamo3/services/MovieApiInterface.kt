package com.lomamo.lomamo3.services

import com.lomamo.lomamo3.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

// We used the TMDB API
// that you can consult in https://www.themoviedb.org/settings/api
//
// API KEY 17ec993f382e241025f7a7205d71bb08


interface MovieApiInterface {

    @GET("/movie/popular?api_key=17ec993f382e241025f7a7205d71bb08")
    fun getMovieList(): Call<MovieResponse>

}