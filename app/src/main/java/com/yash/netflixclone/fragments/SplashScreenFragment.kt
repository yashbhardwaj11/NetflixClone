package com.yash.netflixclone.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private var _binding : FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater,container,false)

//        val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.change_text_transform)
//        binding.textView.startAnimation(animation)



        Handler().postDelayed({
                              findNavController().navigate(R.id.action_splashScreenFragment_to_signInFragment)
        },600)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}