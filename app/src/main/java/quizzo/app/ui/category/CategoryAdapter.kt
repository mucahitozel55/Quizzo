package quizzo.app.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_category_layout.view.*
import quizzo.app.R
import quizzo.app.util.capitalizeWords
import quizzo.app.util.category.Category
import java.util.*

class CategoryAdapter(
    private val itemClicked: (category: String) -> Unit,
    private val context: Context
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CALLBACK) {

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_category_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.tvCategory.text = category.name.capitalizeWords()
        holder.ivCategoryImage.setImageDrawable(context.getDrawable(category.imageId))
        holder.layoutCategory.setOnClickListener {
            itemClicked(category.name)
        }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory = itemView.tv_category!!
        val ivCategoryImage = itemView.iv_categoryImage!!
        val layoutCategory = itemView.layout_category!!
    }
}