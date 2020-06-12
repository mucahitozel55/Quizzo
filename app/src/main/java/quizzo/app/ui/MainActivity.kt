package quizzo.app.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.data.database.QuizzoDatabase
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.util.util_interface.BackPressed

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val database: QuizzoDatabase by inject()
    private val repo: QuizzoRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // Set status bar color
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.authFragment -> {
                    window.statusBarColor = this.getColor(R.color.darkRed1)
                }
                R.id.singlePlayerResultFragment -> {
                    window.statusBarColor = this.getColor(R.color.darkPink)
                }
                else -> {
                    window.statusBarColor = this.getColor(R.color.darkBlue1)
                }
            }
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination!!.id) {
            R.id.singlePlayerFragment, R.id.matchMakingFragment, R.id.multiplayerFragment, R.id.multiplayerResultFragment -> {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                val fragment = navHostFragment!!.childFragmentManager.fragments[0]
                (fragment as BackPressed).backPressed()
            }
            R.id.homeFragment -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}
