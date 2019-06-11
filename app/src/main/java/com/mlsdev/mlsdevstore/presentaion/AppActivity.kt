package com.mlsdev.mlsdevstore.presentaion

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.databinding.ActivityAppBinding

class AppActivity : BaseActivity() {

    lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val navController = Navigation.findNavController(this, R.id.app_nav_host)
        NavigationUI.setupWithNavController(binding.navigation, navController)
        binding.navigation.setOnNavigationItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it, navController)
            true
        }
    }

}