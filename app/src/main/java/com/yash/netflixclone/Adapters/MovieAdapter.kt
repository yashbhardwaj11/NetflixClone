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

class MovieAdapter(val context : Context,val mlist : ArrayList<PopularMovies>,private val listener : IMoviesAdapter) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val moviesImage : ImageView = view.findViewById(R.id.movieImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movies_item,parent,false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
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

interface IMoviesAdapter{
    fun onMoviePosterClick(movie : PopularMovies)
}