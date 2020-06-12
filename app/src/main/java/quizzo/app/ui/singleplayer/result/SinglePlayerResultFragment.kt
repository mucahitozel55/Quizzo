package quizzo.app.ui.singleplayer.result

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.single_player_result_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R

class SinglePlayerResultFragment : Fragment() {

    companion object {
        fun newInstance() =
            SinglePlayerResultFragment()
    }

    private val viewModel: SinglePlayerResultViewModel by inject()
    private lateinit var navController: NavController
    private var score: Int = 0
    private var categoryName = "RANDOM"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.single_player_result_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        score = SinglePlayerResultFragmentArgs.fromBundle(requireArguments()).correctAnswers
        categoryName =
            SinglePlayerResultFragmentArgs.fromBundle(requireArguments()).categoryName
        setupUI()
        setupButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        if (Build.VERSION.SDK_INT > 24) {
            val s = "Quiz Topic: <b>${categoryName.toUpperCase()}</b>"
            tv_subText.text = Html.fromHtml(s, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        } else {
            tv_subText.text = "Quiz Topic: ${categoryName.toUpperCase()}"
        }
        chip_user.text = "$score correct"
        chip_opponent.text = "${8 - score} incorrect"

    }

    private fun setupButton() {
        btn_continue.setOnClickListener {
            navController.navigate(R.id.action_singlePlayerResultFragment_to_homeFragment)
        }
        btn_playAgain.setOnClickListener {
            navController.navigate(R.id.action_singlePlayerResultFragment_to_categoryPage)
        }
    }
}
