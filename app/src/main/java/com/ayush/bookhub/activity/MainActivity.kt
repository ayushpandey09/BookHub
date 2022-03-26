package com.ayush.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.ayush.bookhub.R
import com.ayush.bookhub.fragment.DashboardFragment
import com.ayush.bookhub.fragment.FavouriteFragment
import com.ayush.bookhub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private  lateinit var frameLayout: FrameLayout
    private  lateinit var toolbar: Toolbar
    private  lateinit var coordinatorLayout: CoordinatorLayout
    private  lateinit var navigationView: NavigationView

    private var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        frameLayout = findViewById(R.id.FrameLayout)
        toolbar = findViewById(R.id.toolbar)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        navigationView = findViewById(R.id.NavigationBar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        setupToolBar()
        openDashboard()

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isChecked=true
            it.isCheckable=true
            previousMenuItem=it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.FrameLayout,
                        FavouriteFragment()
                    )
                    .commit()
                    supportActionBar?.title="Favourite"
                drawerLayout.closeDrawers()
            }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.FrameLayout,ProfileFragment()
                    ).commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openDashboard(){

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.FrameLayout,
                DashboardFragment()
            )
            .commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.FrameLayout)){
            !is DashboardFragment ->openDashboard()
            else ->super.onBackPressed()
        }
    }
}