package com.yash.netflixclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.yash.netflixclone.Daos.UserDao
import com.yash.netflixclone.Models.User
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentKycBinding
import com.yash.netflixclone.databinding.FragmentSignInBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class kycFragment : Fragment() {
    private lateinit var auth : FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kyc, container, false)
        val bt : Button = view.findViewById(R.id.checkBT)
        auth = FirebaseAuth.getInstance()

        val radiogroup = view.findViewById<RadioGroup>(R.id.groupradio)
        radiogroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = view.findViewById(checkedId)
            })

        bt.setOnClickListener{

            var id: Int = radiogroup.checkedRadioButtonId
            if (id!=-1){
                val radio:RadioButton = view.findViewById(id)
                GlobalScope.launch(Dispatchers.IO) {
                    val userDao = UserDao()
                    userDao.completeprofile(auth.currentUser!!.uid,radio.text.toString())
                    withContext(Dispatchers.Main){
                        findNavController().navigate(R.id.action_kycFragment_to_mainFragment2)
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Please Select a plan",
                    Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }


}