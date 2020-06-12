package quizzo.app.util.category

import quizzo.app.R

fun getAllCategories(): List<Category> {
    val categoryList = ArrayList<Category>()
    categoryList.add(Category("random", R.drawable.ic_random))
    categoryList.add(Category("general knowledge", R.drawable.ic_gk))
    categoryList.add(Category("music", R.drawable.ic_music))
    categoryList.add(Category("movie", R.drawable.ic_movie))
    categoryList.add(Category("games", R.drawable.ic_games))
    categoryList.add(Category("science", R.drawable.ic_science))
    categoryList.add(Category("computer", R.drawable.ic_computer))
    categoryList.add(Category("sports", R.drawable.ic_sports))
    categoryList.add(Category("anime", R.drawable.ic_anime))
    categoryList.add(Category("books", R.drawable.ic_books))
    return categoryList
}
