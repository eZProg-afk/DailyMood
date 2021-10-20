package spiral.bit.dev.dailymood.ui.feature.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authClient: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    private fun setUpDrawer() = with(headerBinding) {
//        appNameCloseDrawerTv.setOnClickListener {
//            mainBinding.drawerLayout.closeDrawer(Gravity.RIGHT)
//        }
//        myNameTv.setOnClickListener {
//            authClient.currentUser?.email
//        }
//    }
}