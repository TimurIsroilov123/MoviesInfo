package com.bignerdranch.android.androidacademy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), OnMovieItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onItemClickShowDetail(movie: Movie) {
        val fragmentMoviesList = FragmentMoviesDetails()
        val bundle = Bundle()
        bundle.putParcelable("currentMovie", movie)
        fragmentMoviesList.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentMoviesList)
            .addToBackStack("FragmentMoviesDetails")
            .commit()
    }

    override fun onItemClickShowList() {
        supportFragmentManager.popBackStack()
    }
}