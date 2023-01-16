package com.lomamo.lomamo3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.lomamo.lomamo3.adapters.MovieAdapter
import com.lomamo.lomamo3.models.Movie
import com.lomamo.lomamo3.models.MovieResponse
import com.lomamo.lomamo3.pages.InfoActivity
import com.lomamo.lomamo3.services.MovieApiInterface
import com.lomamo.lomamo3.services.MovieApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movies_list.layoutManager = LinearLayoutManager(this)
        rv_movies_list.setHasFixedSize(true)
        getMovieData { movies : List<Movie> ->
            rv_movies_list.adapter = MovieAdapter(movies,
                object : MovieAdapter.OnMovieClickListener {
                    override fun onMovieClick(movie: Movie) {
                        val intent = Intent(this@MainActivity, InfoActivity::class.java)
                        intent.putExtra("movieId", movie.id)
                        startActivity(intent)
                    }
                })
        }
    }


    private fun getMovieData(callback: (List<Movie>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }


        })
    }

}