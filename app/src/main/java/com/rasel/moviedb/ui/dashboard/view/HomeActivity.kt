package com.rasel.moviedb.ui.dashboard.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.databinding.ActivityHomeBinding
import com.rasel.moviedb.ui.dashboard.view.fragment.GraphFragment
import com.rasel.moviedb.ui.dashboard.view.fragment.MovieListFragment
import com.rasel.moviedb.ui.dashboard.view.fragment.TVShowListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var appPrefsManager: AppPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val movieListFragment = MovieListFragment()
        val tvShowListFragment = TVShowListFragment()
        val graphFragment = GraphFragment()

        setCurrentFragment(movieListFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.actionMovie -> setCurrentFragment(movieListFragment)
                R.id.actionTVShow -> setCurrentFragment(tvShowListFragment)
                R.id.actionGraph -> setCurrentFragment(graphFragment)

            }
            true
        }

    }


    override fun onBackPressed() {
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}