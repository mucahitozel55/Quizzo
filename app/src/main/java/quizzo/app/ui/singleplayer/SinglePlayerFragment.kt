package quizzo.app.ui.singleplayer

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
import kotlinx.android.synthetic.main.single_player_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R
import quizzo.app.data.database.entity.History
import quizzo.app.util.custom_dialogs.customAlertDialog
import quizzo.app.util.enums.MatchStatus
import quizzo.app.util.enums.MatchType
import quizzo.app.util.loading.LoadingState
import quizzo.app.util.util_interface.BackPressed
import java.util.*


class SinglePlayerFragment : Fragment(),
    BackPressed {

    companion object {
        fun newInstance() = SinglePlayerFragment()
    }

    private val viewModel: SinglePlayerViewModel by inject()
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var navController: NavController
    private val handler = Handler()
    private var correctAnswers: Int = 0
    private lateinit var customDialog: AlertDialog
    private lateinit var categoryName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.single_player_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        categoryName = SinglePlayerFragmentArgs.fromBundle(requireArguments()).categoryName
        customDialog = customAlertDialog(
            requireContext(),
            "Are you sure you want to quit?",
            {
                val direction =
                    SinglePlayerFragmentDirections.actionSinglePlayerFragmentToSinglePlayerResultFragment(
                        correctAnswers,
                        categoryName
                    )
                viewModel.addMatchToHistory(
                    History(
                        opponent = "",
                        category = categoryName,
                        status = MatchStatus.NONE,
                        type = MatchType.SINGLE_PLAYER,
                        date = Date(),
                        score = correctAnswers
                    )
                )
                navController.navigate(direction)
            },
            "YES",
            {},
            "NO",
            false
        )
        setupRecyclerView()
        setupObservers()

        if (categoryName == "random")
            viewModel.getAllQuestions()
        else
            viewModel.getQuestionsByCategory(categoryName)
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
                    SinglePlayerFragmentDirections.actionSinglePlayerFragmentToSinglePlayerResultFragment(
                        correctAnswers,
                        categoryName
                    )
                viewModel.addMatchToHistory(
                    History(
                        opponent = "",
                        status = MatchStatus.NONE,
                        category = categoryName,
                        type = MatchType.SINGLE_PLAYER,
                        date = Date(),
                        score = correctAnswers
                    )
                )
                if (customDialog.isShowing)
                    customDialog.cancel()
                navController.navigate(direction)
            }
        } catch (e: Exception) {

        }
    }

    private fun btnQuitClicked() {
        customDialog.show()
    }

    override fun backPressed() {
        customDialog.show()
    }

    private fun setupRecyclerView() {
        rv_question_list.setHasFixedSize(true)
        rv_question_list.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }
        questionAdapter =
            QuestionAdapter({ position: Int, optionSelected: Int ->
                quizOptionClicked(position, optionSelected)
            }, {
                btnQuitClicked()
            }, requireContext())
        rv_question_list.adapter = questionAdapter
    }

    private fun setupObservers() {
        viewModel.questionList.observe(viewLifecycleOwner, Observer { questionList ->
            if (questionList != null && questionList.isNotEmpty()) {
                questionAdapter.submitList(questionList)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading.loadingID == 1) {
                when (loading.loadingState) {
                    LoadingState.COMPLETED -> {
                        lay_loading.visibility = View.GONE
                        viewModel.resetLoading()
                        rv_question_list.visibility = View.VISIBLE
                    }
                    LoadingState.LOADING -> {
                        lay_loading.visibility = View.VISIBLE
                        rv_question_list.visibility = View.GONE
                    }
                    LoadingState.IDLE -> {
                    }
                    LoadingState.ERROR -> {
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
