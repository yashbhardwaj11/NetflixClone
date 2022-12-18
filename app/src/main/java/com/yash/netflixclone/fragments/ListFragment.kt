package com.yash.netflixclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yash.netflixclone.Adapters.IMyListAdaoter
import com.yash.netflixclone.Adapters.MyListAdapter
import com.yash.netflixclone.Daos.UserDao
import com.yash.netflixclone.Models.NetflixUser
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentListBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ListFragment : Fragment(), IMyListAdaoter {
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : String
    private lateinit var movies : ArrayList<String>
    private lateinit var adapter : MyListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(layoutInflater,container,false)
        movies = arrayListOf()

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid

        getData()

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUser).await().toObject(NetflixUser::class.java)
            val userCollection = userDao.userCollectionN
            for (i in 0 until user!!.myListMovies.size){
                movies.add(user.myListMovies.get(i))
            }
            withContext(Dispatchers.Main){
                adapter = MyListAdapter(requireContext(),movies,this@ListFragment)
                binding.listViewRV.adapter = adapter
                binding.listViewRV.layoutManager = LinearLayoutManager(requireContext())
                binding.listViewRV.setHasFixedSize(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNameClick(movieName: String) {
        val bundle = Bundle()
        bundle.putString("movieName",movieName)
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment,bundle)
    }
}