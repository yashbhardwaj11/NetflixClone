package com.yash.netflixclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.yash.netflixclone.Adapters.GridAdapter
import com.yash.netflixclone.Adapters.IGridAdapter
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentMovieBinding


class MovieFragment : Fragment(), IGridAdapter {
    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var movie : ArrayList<PopularMovies>
    private lateinit var adapter : GridAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(layoutInflater,container,false)
        movie = arrayListOf()
        val page = (1..10).toList()
        val BASE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=254099e5a74c71ef5bfa775109e5e90f&language=en-US&page=${page.random()}"


        getData(BASE_URL)

        return binding.root
    }

    private fun getData(url: String) {
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
                        movie.add(popularMovies)

                    }
                    adapter = GridAdapter(requireContext(),movie,this@MovieFragment)
                    binding.movieRV.adapter = adapter
                    binding.movieRV.layoutManager = GridLayoutManager(requireContext(),2)
                    binding.movieRV.setHasFixedSize(true)

                }

            },
            { error ->
                Log.d("ErrorRetro",error.message.toString())
                Toast.makeText(requireContext(), "${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        )

        MySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest)
    }

    override fun onPosterClicked(movie: PopularMovies) {
        val bundle = Bundle()
        bundle.putParcelable("movie",movie)
        findNavController().navigate(R.id.action_mainFragment_to_moviePageFragment,bundle)
    }


}