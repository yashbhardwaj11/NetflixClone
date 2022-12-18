package com.yash.netflixclone.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.navigation.NavigationBarView
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater,container,false)

        replaceFragment(AllMoviesFragment())
        bottomNavigation()

        binding.tvShowsBT.setBackgroundColor(Color.TRANSPARENT)



        binding.tvShowsBT.setOnClickListener {
            binding.tvShowsBT.setBackgroundColor(Color.RED)
            binding.moviesBT.setBackgroundColor(Color.TRANSPARENT)
            replaceFragment(TvShowsFragment())
        }

        binding.moviesBT.setOnClickListener {
            binding.moviesBT.setBackgroundColor(Color.RED)
            binding.tvShowsBT.setBackgroundColor(Color.TRANSPARENT)
            replaceFragment(MovieFragment())
        }


        return binding.root
    }

    private fun bottomNavigation() {
        binding.bottomNavMain.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(AllMoviesFragment())
                R.id.menu -> replaceFragment(ListFragment())
                R.id.coming_soon -> replaceFragment(ComingSoonFragment())
                R.id.search -> replaceFragment(SearchMovieFragment())

                else ->{

                }
            }
            true
        })
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.mainChangingFL , fragment)
        fragmentTransaction?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}