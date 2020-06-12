package quizzo.app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.category_page_fragment.*
import org.koin.android.ext.android.inject
import quizzo.app.R

class CategoryPage : Fragment() {

    companion object {
        fun newInstance() = CategoryPage()
    }

    private val viewModel: CategoryPageViewModel by inject()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = CategoryAdapter({ category ->
            val directions =
                CategoryPageDirections.actionCategoryPageToSinglePlayerFragment(category)
            navController.navigate(directions)
        }, requireContext())
        rv_categoryList.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_categoryList.adapter = adapter
        adapter.submitList(viewModel.categoryList)
    }

}
