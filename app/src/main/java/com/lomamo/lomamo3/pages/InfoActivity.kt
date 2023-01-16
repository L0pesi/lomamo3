package com.lomamo.lomamo3.pages

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lomamo.lomamo3.R
import com.lomamo.lomamo3.models.MovieInfo
import com.lomamo.lomamo3.services.MovieApiInterface
import com.lomamo.lomamo3.services.MovieApiService
import kotlinx.android.synthetic.main.activity_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    private var movieId: Int = 0
    private var movieInfo: MovieInfo? = null
    private var uniqueId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        FirebaseApp.initializeApp(this)
        movieId = intent.getStringExtra("movieId").toString().toInt()
        uniqueId = intent.getStringExtra("uniqueId").toString()

        val database = FirebaseDatabase.getInstance()

        getMovieInfo(movieId) { movie ->
            movieInfo = movie
            Toast.makeText(this@InfoActivity, movie.title, Toast.LENGTH_SHORT).show()
        }

        btnFav.setOnClickListener {
            try {
                val movieRef = database.getReference(uniqueId + "/movies/fav").child(movieId.toString())
                movieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // movie already exists in the database, show a message
                            Toast.makeText(
                                this@InfoActivity,
                                "Filme já está adicionado aos favoritos!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // movie does not exist in the database, add it
                            val data = mapOf("movieId" to movieId, "movieName" to movieInfo!!.title)
                            movieRef.setValue(data).addOnSuccessListener {
                                Toast.makeText(
                                    this@InfoActivity,
                                    "Adicionado aos favoritos com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@InfoActivity,
                                    "Ocorreu um erro!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // An error occurred, show a message
                        Toast.makeText(this@InfoActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } catch (e: Exception) {
                Log.e("INFO_ACTIVITY", e.message.toString())
            }

        }

        btnRemoveFav.setOnClickListener {
            try {
                val movieRef = database.getReference(uniqueId + "/movies/fav").child(movieId.toString())
                    .removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Filme removido com sucesso!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Log.e("INFO_ACTIVITY", e.message.toString())
            }
        }
    }

    private fun getMovieInfo(id: Int, callback: (MovieInfo) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieInfo(id).enqueue(object : Callback<MovieInfo> {

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                Toast.makeText(this@InfoActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                if (response.isSuccessful) {
                    return callback(response.body()!!)
                } else {
                    Toast.makeText(this@InfoActivity, "Ocorreu um erro!", Toast.LENGTH_SHORT).show()
                }
            }


        })
    }
}