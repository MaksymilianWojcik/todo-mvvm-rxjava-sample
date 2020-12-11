package com.mw.todo_mvvm_jetpack_reactive_sample.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.ActivityMainBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Timber.d("Destination changed: ${controller.graph}, dest: $destination")
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: super.onSupportNavigateUp()
    }

    private fun setupBottomNavigation() {
        val navGraphIds = listOf(
            R.navigation.tasks_graph,
            R.navigation.settings_graph
        )
        val controller = binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )
        controller.observe(this) { navController ->
            // TODO: Add custom toolbar
            currentNavController?.value?.removeOnDestinationChangedListener(this)
            currentNavController = controller
            currentNavController?.value?.addOnDestinationChangedListener(this)
        }
    }
}
