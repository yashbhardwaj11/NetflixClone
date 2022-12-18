package com.yash.netflixclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.R

class GridAdapter(
    val context: Context,
    val mlist: ArrayList<PopularMovies>,
    val listener: IGridAdapter
):RecyclerView.Adapter<GridAdapter.GridViewHolder>() {
    inner class GridViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val poster : ImageView = view.findViewById(R.id.gridMovieIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_movies_item,parent,false))
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val current = mlist[position]
        holder.poster.load(current.poster_path){
            crossfade(true)
        }
        holder.poster.setOnClickListener{
            listener.onPosterClicked(current)
        }
    }

    override fun getItemCount(): Int = mlist.size
}

interface IGridAdapter{
    fun onPosterClicked(movie : PopularMovies)
}