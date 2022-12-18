package com.yash.netflixclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.R

class MyListAdapter(val context : Context,val mlist : ArrayList<String>,val listener : IMyListAdaoter ) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {
    inner class MyListViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val movieName : TextView = view.findViewById(R.id.movies_name)
        val moviesNameView : CardView = view.findViewById(R.id.moviesNameView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        return MyListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movies_name_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val current = mlist[position]
        holder.movieName.text = current
        holder.moviesNameView.setOnClickListener {
            listener.onNameClick(current)
        }
    }

    override fun getItemCount(): Int = mlist.size


}



interface IMyListAdaoter{
    fun onNameClick(movieName: String)
}