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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.common.collect.Iterators.getNext
import com.yash.netflixclone.Adapters.GridMovieAdapter
import com.yash.netflixclone.Adapters.IGridMovieAdapter
import com.yash.netflixclone.Models.TvShows
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentTvShowsBinding


class TvShowsFragment : Fragment(), IGridMovieAdapter {
    private var _binding : FragmentTvShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvshowslist: ArrayList<TvShows>
    private lateinit var adapter : GridMovieAdapter
    private val imageUrl = "https://image.tmdb.org/t/p/w500/"
    private var totalItemCount : Int = 0

    private var firstVisibleItemCount : Int = 0
    private var visibleItemCount : Int = 0
    private var previousTotal : Int = 0
    private var page : Int = 1
    private var load : Boolean = true
    private lateinit var layoutManager : GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowsBinding.inflate(layoutInflater,container,false)
        tvshowslist = arrayListOf()
        val page = (1..10).toList()
        val url = "https://api.themoviedb.org/3/tv/popular?api_key=254099e5a74c71ef5bfa775109e5e90f&language=en-US&page=${page.random()}"


        getTvShows(url)

        layoutManager = GridLayoutManager(requireContext(),2)
        adapter = GridMovieAdapter(requireContext(),tvshowslist,this@TvShowsFragment)
        binding.tvShowsRV.adapter = adapter
        binding.tvShowsRV.layoutManager = layoutManager
        binding.tvShowsRV.setHasFixedSize(true)

        return binding.root
    }

    private fun getTvShows(url: String) {

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                if (response!=null){
                    val jsonArray = response.getJSONArray("results")
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val tvshows = TvShows(
                            "https://image.tmdb.org/t/p/w500/${jsonObject.getString("backdrop_path")}"
                            ,jsonObject.getString("first_air_date")
                            ,jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("original_language"),
                            jsonObject.getString("original_name"),
                            jsonObject.getString("overview"),
                            jsonObject.getDouble("popularity"),
                            "https://image.tmdb.org/t/p/w500/${jsonObject.getString("poster_path")}",
                            jsonObject.getDouble("vote_average"),
                            jsonObject.getInt("vote_count")
                        )
                        tvshowslist.add(tvshows)
                    }

                    layoutManager = GridLayoutManager(requireContext(),2)
                    adapter = GridMovieAdapter(requireContext(),tvshowslist,this@TvShowsFragment)
                    binding.tvShowsRV.adapter = adapter
                    binding.tvShowsRV.layoutManager = layoutManager
                    binding.tvShowsRV.setHasFixedSize(true)

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

    override fun onMoviePosterClick(tvShow: TvShows) {
        val bundle = Bundle()
        bundle.putParcelable("tvshows",tvShow)
        findNavController().navigate(R.id.action_mainFragment_to_tvPageFragment,bundle)
    }


}