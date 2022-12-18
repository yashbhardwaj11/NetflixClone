package com.yash.netflixclone.Daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.yash.netflixclone.Models.NetflixUser
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.Models.TvShows
import com.yash.netflixclone.Models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("UsersNetflix")
    val userCollectionN = db.collection("UsersNetflixAccountInfo")

    fun addUser(user : User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.userId!!).set(it)
            }
        }
    }

    fun getUserById(userID : String) : Task<DocumentSnapshot>{
        return userCollectionN.document(userID).get()
    }

    fun completeprofile(uid : String,plan : String){
        GlobalScope.launch(Dispatchers.IO) {
            val netflixUser = NetflixUser(uid,plan)
            userCollectionN.document(uid).set(netflixUser)
        }
    }

    fun addMovieToList(uid : String,movie : String){
        GlobalScope.launch (Dispatchers.IO){
            val user = getUserById(uid).await().toObject(NetflixUser::class.java)
            if (user!!.myListMovies.contains(movie)){
                user.myListMovies.remove(movie)
            }
            else{
                user.myListMovies.add(movie)
            }
            userCollectionN.document(uid).set(user)
        }
    }



}