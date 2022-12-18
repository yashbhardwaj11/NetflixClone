package com.yash.netflixclone.fragments

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yash.netflixclone.Daos.UserDao
import com.yash.netflixclone.Models.User
import com.yash.netflixclone.R
import com.yash.netflixclone.databinding.FragmentSignInBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SignInFragment : Fragment() {
    private var _binding : FragmentSignInBinding?= null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var googlgeSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater,container , false)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        googlgeSignInClient = GoogleSignIn.getClient(requireContext(),gso)

        binding.signInBT.isEnabled = false

        binding.emailEt.addTextChangedListener(textWatcher)
        binding.passwordEt.addTextChangedListener(textWatcher)

        binding.signInBT.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                binding.signInBT.isEnabled = true
                binding.signInBT.setBackgroundColor(Color.RED)
            }
            else{
                binding.signInBT.isEnabled = false
                binding.signInBT.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googlgeSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount = task.result
            if (account!=null){
                firebaseAuthWithGoogle(account)
            }
        }
        else{
            Toast.makeText(requireContext(), "Failed to Sign In", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        binding.progressBar.visibility = View.VISIBLE
        binding.signInBT.visibility = View.INVISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser!=null){
            val user = User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
            val userDao = UserDao()
            userDao.addUser(user)

            val onBoardingScreen = this.requireActivity().getSharedPreferences("onBoardingScreen", Activity.MODE_PRIVATE);
            val isFirstTime : Boolean = onBoardingScreen.getBoolean("firstTime", true);

            if (isFirstTime){

                val editor : SharedPreferences.Editor = onBoardingScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
                findNavController().navigate(R.id.action_signInFragment_to_kycFragment2)
            }
            else {
               findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
            }


        }
        else{
            binding.progressBar.visibility = View.GONE
            binding.signInBT.visibility = View.VISIBLE
        }

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        updateUI(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}