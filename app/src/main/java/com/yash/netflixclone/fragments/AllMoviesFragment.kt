package com.yash.netflixclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.netflixclone.Adapters.IMoviesAdapter
import com.yash.netflixclone.Adapters.MovieAdapter
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentAllMoviesBinding

class AllMoviesFragment : Fragment(), IMoviesAdapter {
    private var _binding : FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularMoviesList: ArrayList<PopularMovies>
    private lateinit var adapter : MovieAdapter
    private val imageUrl = "https://image.tmdb.org/t/p/w500/"
    val api_key = "your_api_key"
    var topRated_movies_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=$api_key&language=en-US&page=1"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMoviesBinding.inflate(layoutInflater,container,false)
        popularMoviesList = arrayListOf()
        val api_key = "your_api_key"

        val page = (1..4).toList()
        val popular_movies_url = "https://api.themoviedb.org/3/movie/popular?api_key=$api_key&language=en-US&page=${page.random()}"
        topRated_movies_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=$api_key&language=en-US&page=${page.random()}"



        getMovies(popular_movies_url)


        getTopMovies(topRated_movies_url)



        return binding.root
    }

    private fun getMovies(url : String){

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
                    if (url == topRated_movies_url){
                        adapter = MovieAdapter(requireContext(),popularMoviesList,this@AllMoviesFragment)
                        binding.topRatedRV.adapter = adapter
                        binding.topRatedRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        binding.topRatedRV.setHasFixedSize(true)
                    }else{
                        adapter = MovieAdapter(requireContext(),popularMoviesList,this@AllMoviesFragment)
                        binding.popularRV.adapter = adapter
                        binding.popularRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        binding.popularRV.setHasFixedSize(true)
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

    private fun getTopMovies(url : String){

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
                    adapter = MovieAdapter(requireContext(),popularMoviesList,this@AllMoviesFragment)
                    binding.topRatedRV.adapter = adapter
                    binding.topRatedRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    binding.topRatedRV.setHasFixedSize(true)
                }

            },
            { error ->
                Log.d("ErrorRetro",error.message.toString())
                Toast.makeText(requireContext(), "${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        )

        MySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest)
    }

    override fun onMoviePosterClick(movie: PopularMovies) {
        val bundle = Bundle()
        bundle.putParcelable("movie",movie)
        findNavController().navigate(R.id.action_mainFragment_to_moviePageFragment,bundle)

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.moviePageFL , fragment)
        fragmentTransaction?.commit()
    }
}

