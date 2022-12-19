package com.yash.netflixclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import com.yash.netflixclone.Adapters.IMoviesAdapter
import com.yash.netflixclone.Adapters.MovieAdapter
import com.yash.netflixclone.Daos.UserDao
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentMoviePageBinding
import okhttp3.internal.userAgent


class MoviePageFragment : Fragment(), IMoviesAdapter {
    private var _binding : FragmentMoviePageBinding? = null
    private val binding get() = _binding!!
    private var movie : PopularMovies? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var popularMoviesList: ArrayList<PopularMovies>
    private lateinit var adapter : MovieAdapter
    private val imageUrl = "https://image.tmdb.org/t/p/w500/"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviePageBinding.inflate(layoutInflater,container,false)

        movie = arguments?.getParcelable<PopularMovies>("movie")
        popularMoviesList = arrayListOf()

        val page = (1..4).toList()

        val base_url = "https://api.themoviedb.org/3/movie/${movie!!.id}/similar?api_key=254099e5a74c71ef5bfa775109e5e90f&language=en-US&page=${page.random()}"

        getMovies(base_url)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        binding.addToWatchList.setOnClickListener {
            val userDao = UserDao()
            userDao.addMovieToList(currentUser,movie!!.title)
            binding.addToWatchList.text = "Added to watchList"
        }


        settingViews(movie)

        binding.backBT.setOnClickListener{
            findNavController().navigate(R.id.action_moviePageFragment_to_mainFragment)
        }



        return binding.root
    }

    private fun settingViews(movie: PopularMovies?) {
        binding.movieLanguage.text = movie!!.original_language
        binding.movieRating.text = movie.vote_average.toString()
        binding.movieTitle.text = movie.title
        binding.movieTitleMain.text = movie.original_title
        binding.movieYear.text = movie.release_date
        binding.movieDesc.text = movie.overview
        binding.moviePoster.load(movie.backdrop_path){
            crossfade(true)
        }
        binding.recommendationsTT.text = "Related to ${movie.title}"
    }

    fun getMovies(url : String){

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                if (response!=null){
                    val jsonArray = response.getJSONArray("results")
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val popularMovies = PopularMovies(jsonObject.getBoolean("adult"),
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
                        popularMoviesList.add(popularMovies)

                    }
                    adapter = MovieAdapter(requireContext(),popularMoviesList,this@MoviePageFragment)
                    binding.recommendationsRV.adapter = adapter
                    binding.recommendationsRV.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,false)
                    binding.recommendationsRV.setHasFixedSize(true)
                }

            },
            { error ->
                Log.d("ErrorRetro",error.message.toString())
                Toast.makeText(requireContext(), "${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        )

        MySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMoviePosterClick(movie: PopularMovies) {
        val bundle = Bundle()
        bundle.putParcelable("movie",movie)
        findNavController().navigate(R.id.action_moviePageFragment_self,bundle)
    }
}