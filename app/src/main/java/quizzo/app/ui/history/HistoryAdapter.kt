package quizzo.app.ui.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.single_history_layout_mp.view.*
import kotlinx.android.synthetic.main.single_history_layout_sp.view.*
import kotlinx.android.synthetic.main.single_history_layout_sp.view.tv_date
import kotlinx.android.synthetic.main.single_history_layout_sp.view.tv_score
import quizzo.app.R
import quizzo.app.data.database.entity.History
import quizzo.app.util.category.Category
import quizzo.app.util.enums.MatchStatus
import quizzo.app.util.enums.MatchType
import java.text.SimpleDateFormat

class HistoryAdapter(
    private val context: Context,
    private val catList: List<Category>,
    private val userImage: String? = null
) :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(CALLBACK) {

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.date.toString() == newItem.date.toString()
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.date.toString() == newItem.date.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val history = getItem(position)
        return if (history.type == MatchType.SINGLE_PLAYER) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context!!)
                .inflate(
                    if (viewType == 1) R.layout.single_history_layout_sp else R.layout.single_history_layout_mp,
                    parent,
                    false
                )
        )
    }

    private fun getCategoryImage(categoryName: String): Int {
        return catList.find { category ->
            category.name == categoryName
        }!!.imageId
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        val sdf = SimpleDateFormat("EE, dd MMM - hh:mm a")
        holder.tvDate.text = sdf.format(history.date)

        if (getItemViewType(position) == 1) {   //SINGLE PLAYER
            holder.tvScore.text = "${history.score}/8"
            holder.ivCategoryImage.setImageDrawable(context.getDrawable(getCategoryImage(history.category)))
        } else {   //MULTI PLAYER
            Glide.with(context).load(userImage!!).placeholder(R.drawable.ic_anime)
                .into(holder.ivUser)
            Glide.with(context).load(history.opponentImageURL).placeholder(R.drawable.ic_anime)
                .into(holder.ivOpponent)
            holder.tvOpponent.text = history.opponent!!.split(' ')[0]
            holder.tvScore.text = "${history.score} - ${history.opponentScore}"
            holder.tvScore.setTextColor(
                context.getColor(
                    when (history.status) {
                        MatchStatus.WIN -> R.color.lightGreen
                        MatchStatus.LOSE -> R.color.darkRed1
                        MatchStatus.DRAW -> R.color.lightGray
                        MatchStatus.NONE -> R.color.lightGray
                    }
                )
            )
        }
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoryImage = itemView.iv_categoryImage
        val tvDate = itemView.tv_date!!
        val tvScore = itemView.tv_score!!
        val ivUser = itemView.iv_user
        val ivOpponent = itemView.iv_opponent
        val tvOpponent = itemView.tv_opponent

    }
}