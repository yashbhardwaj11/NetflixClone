package com.yash.netflixclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.yash.netflixclone.Adapters.*
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.Models.TvShows
import com.yash.netflixclone.MySingleton
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentTvPageBinding

class tvPageFragment : Fragment(), IRecommendedTvAdapter {
    private var _binding : FragmentTvPageBinding? = null
    private val binding get() = _binding!!
    private var tvShow : TvShows? = null
    private lateinit var recommendedTvList: ArrayList<TvShows>
    private lateinit var adapter : RecommendedTvAdapter
    private val imageUrl = "https://image.tmdb.org/t/p/w500/"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvPageBinding.inflate(layoutInflater,container,false)

        tvShow = arguments?.getParcelable<TvShows>("tvshows")
        recommendedTvList = arrayListOf()
        val api_key = "your_api_key"
        val page = (1..4).toList()
        val BASE_URL = "https://api.themoviedb.org/3/tv/${tvShow!!.id}/similar?api_key=$api_key&language=en-US&page=${page.random()}"
        settingTvViews(tvShow)
        getRecommended(BASE_URL)

        return binding.root
    }

    private fun settingTvViews(tvShow: TvShows?) {
        binding.movieLanguage.text = tvShow!!.original_language
        binding.movieRating.text = tvShow.vote_average.toString()
        binding.movieTitle.text = tvShow.name
        binding.movieTitleMain.text = tvShow.original_name
        binding.movieYear.text = tvShow.first_air_date
        binding.movieDesc.text = tvShow.overview
        binding.moviePoster.load(tvShow.backdrop_path){
            crossfade(true)
        }
        binding.recommendationsTT.text = "Related to ${tvShow.name}"
    }

    private fun getRecommended(url : String){
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
                        recommendedTvList.add(tvshows)
                    }
                    adapter = RecommendedTvAdapter(requireContext(),recommendedTvList,this@tvPageFragment)
                    binding.recommendationsRV.adapter = adapter
                    binding.recommendationsRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
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

    override fun onPosterClick(tvShow: TvShows) {
        val bundle = Bundle()
        bundle.putParcelable("tvshows",tvShow)
        findNavController().navigate(R.id.action_tvPageFragment_self,bundle)
    }


}