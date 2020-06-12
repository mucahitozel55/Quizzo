package quizzo.app.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by inject()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupObservers()
        setupButtons()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user == null && !viewModel.isAnonymousSignIn)
                navController.navigate(R.id.action_homeFragment_to_authFragment)
            if (user != null) {
                val arr = user.username.split(' ')
                tv_username.text = arr[0] + "\n" + arr[arr.size - 1]
            }
        })
    }

    private fun setupButtons() {
        btn_singleplayer.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_categoryPage)
        }
        btn_history.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_historyFragment)
        }
        btn_logout.visibility = if (viewModel.isAnonymousSignIn) View.INVISIBLE else View.VISIBLE
        btn_logout.setOnClickListener {
            viewModel.logout()
        }
        btn_multiplayer.setOnClickListener {
            if (viewModel.isAnonymousSignIn) {
                Toast.makeText(requireContext(), "You need to be signed in!!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                navController.navigate(R.id.action_homeFragment_to_matchMakingFragment)
            }
        }
        btn_leaderboard.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon !!", Toast.LENGTH_SHORT).show()
        }
    }
}
