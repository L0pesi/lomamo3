package com.lomamo.lomamo3.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lomamo.lomamo3.R
import com.lomamo.lomamo3.models.Movie
import com.lomamo.lomamo3.models.MovieInfo
import com.lomamo.lomamo3.models.MovieResponse
import com.lomamo.lomamo3.services.MovieApiInterface
import com.lomamo.lomamo3.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val movieId = intent.getStringExtra("movieId")


        getMovieInfo(movieId.toString().toInt()) { movie ->
            Toast.makeText(this@InfoActivity, movie.title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMovieInfo(id:Int ,callback: (MovieInfo) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieInfo(id).enqueue(object : Callback<MovieInfo> {

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                Toast.makeText(this@InfoActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                if (response.isSuccessful) {
                    return callback(response.body()!!)
                }
                else{
                    Toast.makeText(this@InfoActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
                }
            }


        })
    }
}