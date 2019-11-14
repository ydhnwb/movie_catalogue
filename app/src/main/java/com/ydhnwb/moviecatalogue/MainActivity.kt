package com.ydhnwb.moviecatalogue

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ydhnwb.moviecatalogue.fragments.FavoriteFragment
import com.ydhnwb.moviecatalogue.fragments.MovieFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object { var nav_index = -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_movie }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            else ->  super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.isNotEmpty()){
                        startActivity(Intent(this@MainActivity, SearchActivity::class.java).apply {
                            putExtra("QUERY_SEARCH", it)
                        })
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return true
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_movie -> {
                nav_index = 0
                applyFragment(MovieFragment())
            }
            R.id.navigation_favorite -> {
                nav_index = 1
                applyFragment(FavoriteFragment())
            }
            else -> {
                nav_index = 0
                applyFragment(MovieFragment())
            }
        }
        true
    }

    private fun applyFragment(fr : Fragment)= supportFragmentManager.apply { beginTransaction().replace(R.id.screen_container, fr).commit() }
}
