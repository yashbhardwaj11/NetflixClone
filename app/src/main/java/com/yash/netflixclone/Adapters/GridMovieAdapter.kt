package com.yash.netflixclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yash.netflixclone.Models.PopularMovies
import com.yash.netflixclone.Models.TvShows
import com.yash.netflixclone.R


class GridMovieAdapter(val context : Context, private val mlist : ArrayList<TvShows>, private val listener : IGridMovieAdapter)
    : RecyclerView.Adapter<GridMovieAdapter.GridMovieViewHolder>() {
    inner class GridMovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val moviesImage : ImageView = view.findViewById(R.id.gridMovieIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridMovieViewHolder {
        return GridMovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_movies_item,parent,false))
    }

    override fun onBindViewHolder(holder: GridMovieViewHolder, position: Int) {
        val current = mlist[position]
        holder.moviesImage.load(current.poster_path){
            crossfade(true)
        }
        holder.moviesImage.setOnClickListener{
            listener.onMoviePosterClick(current)
        }
    }

    override fun getItemCount(): Int = mlist.size


}

interface IGridMovieAdapter{
    fun onMoviePosterClick(tvShow : TvShows)
}