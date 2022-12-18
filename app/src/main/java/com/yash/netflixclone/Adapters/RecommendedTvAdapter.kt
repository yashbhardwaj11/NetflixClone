package com.yash.netflixclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yash.netflixclone.Models.TvShows
import com.yash.netflixclone.R
import com.yash.netflixclone.fragments.tvPageFragment

class RecommendedTvAdapter(
    val context: Context,
    val mlist: ArrayList<TvShows>,
    val listener: IRecommendedTvAdapter
) : RecyclerView.Adapter<RecommendedTvAdapter.RecommendedViewHolder>() {
    inner class RecommendedViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val posterImage = view.findViewById<ImageView>(R.id.movieImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        return RecommendedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movies_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val current = mlist[position]
        holder.posterImage.load(current.poster_path){
            crossfade(true)
        }
        holder.posterImage.setOnClickListener{
            listener.onPosterClick(current)
        }
    }

    override fun getItemCount(): Int = mlist.size
}
interface IRecommendedTvAdapter{
    fun onPosterClick(tvShow : TvShows)
}