package spiral.bit.dev.dailymood.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ActivityMainBinding
import spiral.bit.dev.dailymood.databinding.DrawerHeaderBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var authClient: FirebaseAuth

    private val mainBinding: ActivityMainBinding by viewBinding()
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            with(mainBinding.tvProVersion) {
                isVisible = when (destination.id) {
                    R.id.mainFragment -> true
                    else -> false
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()
        setUpViews()
    }

//    private fun setUpDrawer() = with(headerBinding) {
//        appNameCloseDrawerTv.setOnClickListener {
//            mainBinding.drawerLayout.closeDrawer(Gravity.RIGHT)
//        }
//        myNameTv.setOnClickListener {
//            authClient.currentUser?.email
//        }
//    }

    private fun setUpViews() = with(mainBinding) {
        tvProVersion.setOnClickListener {
            navController.navigate(R.id.proFragment)
        }
    }

    private fun setUpNavigation() = with(mainBinding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frag_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(
            setOf(R.id.mainFragment),
            drawerLayout
        ).also {
            toolbar.setupWithNavController(navController, it)
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }
}