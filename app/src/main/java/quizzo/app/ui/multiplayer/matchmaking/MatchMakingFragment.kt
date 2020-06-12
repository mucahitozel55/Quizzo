package quizzo.app.ui.multiplayer.matchmaking

import android.animation.AnimatorSet
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.match_making_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.util.Animations
import quizzo.app.util.util_interface.BackPressed

class MatchMakingFragment : Fragment(), BackPressed {

    companion object {
        fun newInstance() = MatchMakingFragment()
    }

    private val viewModel: MatchMakingViewModel by inject()
    private lateinit var navController: NavController
    private val handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.match_making_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupObservers()
        setupButtons()
        viewModel.findMatch()
        handler.postDelayed({
            viewModel.findMatchBot()
        }, 15000)
    }

    private fun startAnimation() {
        val animatorSet = AnimatorSet()
        val ivUserAnimationM = Animations.moveLeftRight(iv_user, -100f, 700)
        val ivUserAnimationF = Animations.fadeIn(iv_user, 700)
        val ivOpponentAnimationM = Animations.moveLeftRight(iv_opponent, 100f, 700)
        val ivOpponentAnimationF = Animations.fadeIn(iv_opponent, 700)

        val tvUser = Animations.fadeIn(tv_user, 700)
        val tvOpponent = Animations.fadeIn(tv_opponent, 700)

        val pbMatchMaking = Animations.fadeOut(pb_matchMaking, 700)
        val tvMatchMaking = Animations.fadeOut(tv_matchMaking, 700)

        animatorSet.play(ivUserAnimationM)
            .with(ivUserAnimationF)
            .with(tvUser)
            .with(ivOpponentAnimationM)
            .with(ivOpponentAnimationF)
            .with(tvOpponent)
            .with(pbMatchMaking)
            .with(tvMatchMaking)
        animatorSet.play(Animations.delayAnimator(1000)).after(tvMatchMaking)

        animatorSet.start()
        animatorSet.doOnEnd {
            navController.navigate(R.id.action_matchMakingFragment_to_multiplayerFragment)
        }
    }

    private fun setupObservers() {
        viewModel.match.observe(viewLifecycleOwner, Observer { match ->
            if (match != null) {
                val opponentPlayer =
                    if (match.player1.isOpponent!!) match.player1 else match.player2
                Glide.with(requireContext()).load(opponentPlayer.imageURL)
                    .placeholder(R.drawable.ic_anime)
                    .into(iv_opponent)
                Glide.with(requireContext()).load(viewModel.user.value!!.imageURL)
                    .placeholder(R.drawable.ic_anime)
                    .into(iv_user)
                tv_user.text = viewModel.user.value!!.username
                tv_opponent.text = opponentPlayer.name
                btn_cancel.visibility = View.INVISIBLE
                btn_cancel.isClickable = false
                startAnimation()
            }
        })
    }

    private fun setupButtons() {
        btn_cancel.setOnClickListener {
            viewModel.destroyMatchMakingSocket()
            navController.navigateUp()
            handler.removeCallbacksAndMessages(null)
        }
    }

    override fun backPressed() {
        viewModel.destroyMatchMakingSocket()
        navController.navigateUp()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
