package com.yash.netflixclone.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import com.yash.netflixclone.Daos.UserDao
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var movie : PopularMovies


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        val movieName = arguments?.getString("movieName")
        val base_url = "https://api.themoviedb.org/3/search/movie?api_key=254099e5a74c71ef5bfa775109e5e90f&language=en-US&query=${movieName}&page=1&include_adult=false"

        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        getData(movieName.toString(),base_url)
        binding.addToWatchList.setOnClickListener {
            val userDao = UserDao()
            userDao.addMovieToList(currentUser, movie.title)
            binding.addToWatchList.text = "Add to watchList"
            binding.addToWatchList.setOnClickListener {
                binding.addToWatchList.text = "Remove from playlist"
                userDao.addMovieToList(currentUser,movie.title)
            }
        }

        return binding.root
    }

    private fun getData(movieName : String, url : String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                if (response!=null){
                    val jsonArray = response.getJSONArray("results")
                    val jsonObject = jsonArray.getJSONObject(0)
                        movie = PopularMovies(jsonObject.getBoolean("adult"),
                            "https://image.tmdb.org/t/p/w500/${jsonObject.getString("backdrop_path")}"
                            ,jsonObject.getInt("id")
                            ,jsonObject.getString("original_language"),
                            jsonObject.getString("original_title"),
                            jsonObject.getString("overview"),
                            jsonObject.getDouble("popularity"),
                            "https://image.tmdb.org/t/p/w500/${jsonObject.getString("poster_path")}",
                            jsonObject.getString("release_date"),
                            jsonObject.getString("title"),
                            jsonObject.getBoolean("video"),
                            jsonObject.getDouble("vote_average"),
                            jsonObject.getInt("vote_count")
                        )
                    binding.movieLanguage.text = movie.original_language
                    binding.movieRating.text = movie.vote_average.toString()
                    binding.movieTitle.text = movie.title
                    binding.movieTitleMain.text = movie.original_title
                    binding.movieYear.text = movie.release_date
                    binding.movieDesc.text = movie.overview
                    binding.moviePoster.load(movie.backdrop_path){
                        crossfade(true)
                    }
                }

            },
            { error ->
                Log.d("ErrorRetro",error.message.toString())
                Toast.makeText(requireContext(), "${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        )

        MySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest)
    }

}