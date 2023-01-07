package com.android.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.presentation.R
import com.android.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        setupNavController(navController)
    }

    private fun setupNavController(navController: NavController?) {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        navController?.let {
            binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        }
    }
}
