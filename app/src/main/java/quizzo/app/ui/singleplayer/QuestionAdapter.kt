package quizzo.app.ui.singleplayer

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.question_single_layout.view.*
import quizzo.app.R
import quizzo.app.data.network.response.question.Question

class QuestionAdapter(
    private val itemClicked: (position: Int, optionSelected: Int) -> Unit,
    private val quitClicked: () -> Unit,
    private val context: Context
) : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(CALLBACK) {

    private lateinit var animation: ObjectAnimator

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.question == newItem.question
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.question_single_layout,
                parent,
                false
            )
        )
    }

    private fun optionClicked(
        tvOption: TextView,
        tvOptionName: TextView,
        optionCard: CardView,
        selectedOption: Int,
        correctAnswer: Int,
        position: Int
    ) {
        optionCard.isEnabled = false
        optionCard.isClickable = false
        tvOption.setTextColor(context.getColor(R.color.white))
        tvOptionName.setTextColor(context.getColor(R.color.white))
        if (correctAnswer == selectedOption)
            optionCard.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.lightGreen))
        else
            optionCard.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.darkRed1))
        itemClicked(position, selectedOption)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val question = getItem(position)

        //Progress Bar
        animation = ObjectAnimator.ofInt(holder.pbTimer, "progress", 0, 500);
        animation.duration = 20000
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                itemClicked(position, -1)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animation.start()

        //Question Number
        holder.questionNoList[position].setBackgroundResource(R.drawable.circle_solid)
        holder.questionNoList[position].backgroundTintList =
            ColorStateList.valueOf(context.getColor(R.color.yellow))
        holder.questionNoList[position].setTextColor(context.getColor(R.color.white))

        //Question Category
        holder.tvCategoryName.text=question.category

        //Question
        holder.tvQuestion.text = question.question

        //Question Image
        if (question.image.equals("null", true)) {
            holder.ivQuestionImage.visibility = View.GONE
        } else {
            holder.ivQuestionImage.visibility = View.VISIBLE
            Glide.with(context).load(question.image).placeholder(R.drawable.placeholder)
                .into(holder.ivQuestionImage)
        }

        //Options
        holder.tvOption1.text = question.answers!![0]
        holder.tvOption2.text = question.answers[1]
        holder.tvOption3.text = question.answers[2]
        holder.tvOption4.text = question.answers[3]

        holder.tvOption1.setOnClickListener {
            optionClicked(
                holder.tvOption1,
                holder.tvA,
                holder.cardOption1,
                0,
                question.correct,
                position
            )
            animation.cancel()
        }
        holder.tvOption2.setOnClickListener {
            optionClicked(
                holder.tvOption2,
                holder.tvB,
                holder.cardOption2,
                1,
                question.correct,
                position
            )
            animation.cancel()
        }
        holder.tvOption3.setOnClickListener {
            optionClicked(
                holder.tvOption3,
                holder.tvC,
                holder.cardOption3,
                2,
                question.correct,
                position
            )
            animation.cancel()
        }
        holder.tvOption4.setOnClickListener {
            optionClicked(
                holder.tvOption4,
                holder.tvD,
                holder.cardOption4,
                3,
                question.correct,
                position
            )
            animation.cancel()
        }

        //Quit Button
        holder.btnQuit.setOnClickListener {
            quitClicked()
        }


    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pbTimer = itemView.pbTimer!!
        val tvCategoryName=itemView.tv_categoryName!!
        val tvQuestion = itemView.tvQuestion!!
        val ivQuestionImage = itemView.ivQuestionImage!!
        val cardOption1: CardView = itemView.card_option1!!
        val cardOption2: CardView = itemView.card_option2!!
        val cardOption3: CardView = itemView.card_option3!!
        val cardOption4: CardView = itemView.card_option4!!
        val tvA = itemView.tv_a!!
        val tvB = itemView.tv_b!!
        val tvC = itemView.tv_c!!
        val tvD = itemView.tv_d!!
        val tvOption1 = itemView.tv_option1!!
        val tvOption2 = itemView.tv_option2!!
        val tvOption3 = itemView.tv_option3!!
        val tvOption4 = itemView.tv_option4!!
        val btnQuit = itemView.btn_quit!!
        val questionNoList = listOf<TextView>(
            itemView.tv_i1!!,
            itemView.tv_i2!!,
            itemView.tv_i3!!,
            itemView.tv_i4!!,
            itemView.tv_i5!!,
            itemView.tv_i6!!,
            itemView.tv_i7!!,
            itemView.tv_i8!!
        )
    }
}