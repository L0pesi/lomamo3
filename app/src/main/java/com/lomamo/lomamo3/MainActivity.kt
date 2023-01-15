package com.lomamo.lomamo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lomamo.lomamo3.models.Movie
import com.lomamo.lomamo3.models.MovieResponse
import com.lomamo.lomamo3.services.MovieApiInterface
import com.lomamo.lomamo3.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    private fun getMovieData(callback: (List<Movie>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }


        })
    }

}