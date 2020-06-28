package com.thiha.criminalintent.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.thiha.criminalintent.R

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.id_toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.id_NavHostFragment) as NavHostFragment

        val navController = host.navController

        appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = setOf(R.id.listFrag), drawerLayout = null)

        setUpWithToolbar(navController, appBarConfiguration)

    }

    private fun setUpWithToolbar(navControl: NavController, appConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navControl, appConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.id_NavHostFragment).navigateUp(appBarConfiguration)
    }
}