package com.yash.netflixclone.Models

data class NetflixUser (val uid : String? = ""
                        ,val plan : String = ""
                        ,val myListMovies : ArrayList<String> = ArrayList(),
                        val myListTvShows : ArrayList<String> = ArrayList()
)