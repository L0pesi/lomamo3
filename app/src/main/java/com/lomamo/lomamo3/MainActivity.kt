package com.lomamo.lomamo3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lomamo.lomamo3.adapters.MovieAdapter
import com.lomamo.lomamo3.database.LomamoDatabase
import com.lomamo.lomamo3.database.models.MovieData
import com.lomamo.lomamo3.database.models.UserData
import com.lomamo.lomamo3.models.Movie
import com.lomamo.lomamo3.models.MovieResponse
import com.lomamo.lomamo3.pages.InfoActivity
import com.lomamo.lomamo3.services.MovieApiInterface
import com.lomamo.lomamo3.services.MovieApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val lomamoDatabase by lazy { LomamoDatabase.getDatabase(this) }
    private var movieDataList = listOf<MovieData>()
    private var uniqueId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movies_list.layoutManager = LinearLayoutManager(this)
        rv_movies_list.setHasFixedSize(true)

        createUser()
        fillList()
    }

    private fun createUser() {
        lifecycleScope.launch {
            // This code will run in a background thread
            // lomamoDatabase.userDao().deleteAll() tests
            //lomamoDatabase.movieDao().deleteAll()
            val userDataFlow = lomamoDatabase.userDao().get()
            userDataFlow.collect { userData ->
                if (userData === null) {
                    uniqueId = UUID.randomUUID().toString()
                    lomamoDatabase.userDao().add(UserData(uniqueId!!))
                } else
                    uniqueId = userData.uniqueId

            }
        }
    }


    private fun fillList() {

        getMovieData { movies: List<Movie> ->
            for (item in movies) {
                lifecycleScope.launch {
                    // This code will run in a background thread
                    lomamoDatabase.movieDao()
                        .add(MovieData(item.id!!, item.title, item.poster, item.release))
                }
            }
        }

        lifecycleScope.launch {
            // This code will run in a background thread
            lomamoDatabase.movieDao().get().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    movieDataList = notesList
                    loadUi()
                }
            }
        }


    }

    private fun loadUi() {
        //update UI
        rv_movies_list.adapter = MovieAdapter(movieDataList,
            object : MovieAdapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieData) {
                    val intent = Intent(this@MainActivity, InfoActivity::class.java)
                    intent.putExtra("movieId", movie.id)
                    intent.putExtra("uniqueId", uniqueId)
                    startActivity(intent)
                }
            })
    }


    private fun getMovieData(callback: (List<Movie>) -> Unit) {
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