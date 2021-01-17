package com.mw.todo_mvvm_jetpack_reactive_sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.ActivityMainBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.NavigationDispatcher
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigation()
        } // else wait for onRestoreInstanceState

        setSupportActionBar(binding.customToolbar)

        navigationDispatcher.navigationCommands.observe(this) { command ->
            Navigation.findNavController(this, R.id.nav_host_fragment).command(this)
            // or: command.invoke(Navigation.findNavController(this, R.id.nav_host_fragment), this)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Timber.d("Destination changed: ${controller.graph}, dest: $destination")
        when (destination.id) {
            R.id.newTaskFragment -> {
                binding.bottomNav.isVisible = false
                supportActionBar?.show()
            }
            else -> {
                binding.bottomNav.isVisible = true
                supportActionBar?.hide()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
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
            setupActionBarWithNavController(navController)
            currentNavController?.value?.removeOnDestinationChangedListener(this)
            currentNavController = controller
            currentNavController?.value?.addOnDestinationChangedListener(this)
        }
    }
}
