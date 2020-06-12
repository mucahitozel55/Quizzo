package quizzo.app.ui.multiplayer.result

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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.multiplayer_result_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.data.database.entity.History
import quizzo.app.data.network.response.multiplayer.Player
import quizzo.app.util.enums.MatchStatus
import quizzo.app.util.enums.MatchType
import quizzo.app.util.util_interface.BackPressed
import java.util.*

class MultiplayerResultFragment : Fragment(), BackPressed {

    companion object {
        fun newInstance() = MultiplayerResultFragment()
    }

    private lateinit var navController: NavController
    private val viewModel: MultiplayerResultViewModel by inject()
    private var correctAnswers: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.multiplayer_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        correctAnswers = MultiplayerResultFragmentArgs.fromBundle(requireArguments()).correctAnswers
        setupObservers()
        setupUI()
        setupButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.match.observe(viewLifecycleOwner, Observer { match ->
            val opponent = if (match.player1.isOpponent!!) match.player1 else match.player2
            if (opponent.score > -1) {
                when (getMatchStatus(opponent)) {
                    MatchStatus.WIN -> {
                        iv_matchIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_trophy))
                        tv_matchStatus.text = "WINNER!!"
                        chip_opponent.text = "${opponent.score} correct"
                    }
                    MatchStatus.LOSE -> {
                        iv_matchIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_loser))
                        tv_matchStatus.text = "Better luck next time!!"
                        chip_opponent.text = "${opponent.score} correct"
                    }
                    MatchStatus.DRAW -> {
                        iv_matchIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_draw))
                        tv_matchStatus.text = "Worthy Opponent!!"
                        chip_opponent.text = "${opponent.score} correct"
                    }
                    else -> {
                    }
                }
                iv_matchIcon.visibility = View.VISIBLE
                tv_matchStatus.visibility = View.VISIBLE
                iv_opponent.visibility = View.VISIBLE
                chip_opponent.visibility = View.VISIBLE
                btn_continue.visibility = View.VISIBLE
                pb_status.visibility = View.GONE
                tv_subText.visibility = View.GONE
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        val opponent =
            if (viewModel.match.value!!.player1.isOpponent!!)
                viewModel.match.value!!.player1
            else
                viewModel.match.value!!.player2
        val user = viewModel.user.value!!
        tv_subText.text = "Waiting for ${opponent.name.split(' ')[0]} to finish"
        Glide.with(requireContext()).load(user.imageURL).placeholder(R.drawable.ic_profile)
            .into(iv_user)
        Glide.with(requireContext()).load(opponent.imageURL).placeholder(R.drawable.ic_profile)
            .into(iv_opponent)
        chip_user.text = "$correctAnswers correct"
    }


    private fun getMatchStatus(opponent: Player): MatchStatus {

        return when {
            correctAnswers > opponent.score -> MatchStatus.WIN
            correctAnswers < opponent.score -> MatchStatus.LOSE
            else -> MatchStatus.DRAW
        }
    }

    private fun setupButton() {
        btn_continue.setOnClickListener {
            viewModel.destroyMatchSocket()
            val opponent =
                if (viewModel.match.value!!.player1.isOpponent!!)
                    viewModel.match.value!!.player1
                else
                    viewModel.match.value!!.player2
            viewModel.addMatchToHistory(
                History(
                    category = "random",
                    status = getMatchStatus(opponent),
                    opponent = opponent.name,
                    type = MatchType.MULTI_PLAYER,
                    date = Date(),
                    score = correctAnswers,
                    opponentScore = opponent.score,
                    opponentImageURL = opponent.imageURL
                )
            )
            navController.navigateUp()
        }
    }

    override fun backPressed() {
        val opponent =
            if (viewModel.match.value!!.player1.isOpponent!!)
                viewModel.match.value!!.player1
            else
                viewModel.match.value!!.player2
        if (opponent.score != -1) {
            viewModel.destroyMatchSocket()
            viewModel.addMatchToHistory(
                History(
                    category = "random",
                    status = getMatchStatus(opponent),
                    opponent = opponent.name,
                    type = MatchType.MULTI_PLAYER,
                    date = Date(),
                    score = correctAnswers,
                    opponentScore = opponent.score,
                    opponentImageURL = opponent.imageURL
                )
            )
            navController.navigateUp()
        } else {
            Toast.makeText(requireContext(), "Waiting for opponent!!", Toast.LENGTH_SHORT).show()
        }
    }
}
