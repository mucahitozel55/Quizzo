package quizzo.app.ui.auth

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.auth_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.util.loading.LoadingState

class AuthFragment : Fragment() {

    companion object {
        fun newInstance() = AuthFragment()
    }

    private val viewModel: AuthViewModel by inject()
    private lateinit var navController: NavController
    private lateinit var signInOptions: GoogleSignInOptions
    private lateinit var signInClient: GoogleSignInClient
    private val GOOGLE_SIGN_IN = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auth_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(requireContext(), signInOptions)
        setupObservers()
        setupButtons()
        viewModel.getUser()
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                navController.navigate(R.id.action_authFragment_to_homeFragment)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading.loadingID == 3) {
                when (loading.loadingState) {
                    LoadingState.COMPLETED -> {
                        loadingUtil(false)
                        viewModel.resetLoading()
                    }
                    LoadingState.LOADING -> {
                        loadingUtil(true)
                    }
                    LoadingState.IDLE -> {
                    }
                    LoadingState.ERROR -> {
                    }
                }
            }
        })
    }

    private fun loadingUtil(isLoading: Boolean) {  //Util function for setting loading
        if (isLoading) {
            pb_login.visibility = View.VISIBLE
            btn_login.visibility = View.INVISIBLE
            btn_continue.isEnabled = false
        } else {
            pb_login.visibility = View.INVISIBLE
            btn_login.visibility = View.VISIBLE
            btn_continue.isEnabled = true
        }
    }

    private fun setupButtons() {
        btn_continue.setOnClickListener {
            viewModel.signInAnonymously()
            navController.navigate(R.id.action_authFragment_to_homeFragment)
        }
        btn_login.setOnClickListener {
            startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN)
            loadingUtil(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account == null) {
                    loadingUtil(false)
                }
                viewModel.signInUser(account!!)
            } catch (e: Exception) {
                Log.d("TAG", e.message!!)
                loadingUtil(false)
            }
        } else {
            loadingUtil(false)
        }
    }
}
