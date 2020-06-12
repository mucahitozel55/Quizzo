package quizzo.app.ui.multiplayer.match

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.multiplayer_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.util.custom_dialogs.customAlertDialog
import quizzo.app.util.util_interface.BackPressed

class MultiplayerFragment : Fragment(), BackPressed {

    companion object {
        fun newInstance() = MultiplayerFragment()
    }

    private val viewModel: MultiplayerViewModel by inject()
    private lateinit var navController: NavController
    private lateinit var questionAdapter: QuestionAdapter
    private val handler = Handler()
    private var correctAnswers: Int = 0
    private lateinit var customDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.multiplayer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        customDialog = customAlertDialog(
            requireContext(),
            "Are you sure you want to quit?",
            {
                val direction =
                    MultiplayerFragmentDirections.actionMultiplayerFragmentToMultiplayerResultFragment(
                        correctAnswers
                    )
                if (viewModel.opponentPlayer.score == -1)
                    viewModel.matchComplete(correctAnswers)
                navController.navigate(direction)
            },
            "YES",
            {},
            "NO",
            false
        )
        setupRecyclerView()
        setupObservers()
        viewModel.initMatch()
    }

    private fun quizOptionClicked(position: Int, optionSelected: Int) {
        try {
            if (rv_question_list == null)
                return
            if (optionSelected == questionAdapter.currentList[position].correct)
                correctAnswers++
            if (position != questionAdapter.currentList.size - 1) {
                handler.postDelayed({
                    rv_question_list.scrollToPosition(position + 1)
                }, 300)
            } else {
                val direction =
                    MultiplayerFragmentDirections.actionMultiplayerFragmentToMultiplayerResultFragment(
                        correctAnswers
                    )
                if (customDialog.isShowing)
                    customDialog.cancel()
                if (viewModel.opponentPlayer.score == -1)
                    viewModel.matchComplete(correctAnswers)
                navController.navigate(direction)
            }
        } catch (e: Exception) {

        }
    }

    private fun setupRecyclerView() {
        rv_question_list.setHasFixedSize(true)
        rv_question_list.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }
        questionAdapter = QuestionAdapter(
            { position: Int, optionSelected: Int ->
                quizOptionClicked(position, optionSelected)
            }, requireContext(),
            viewModel.opponentPlayer,
            viewModel.user.value!!.imageURL
        )
        rv_question_list.adapter = questionAdapter
    }

    private fun setupObservers() {
        viewModel.questionList.observe(viewLifecycleOwner, Observer { questionList ->
            if (questionList != null && questionList.isNotEmpty()) {
                questionAdapter.submitList(questionList)
                rv_question_list.visibility = View.VISIBLE
            }
        })
    }

    override fun backPressed() {
        customDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}
