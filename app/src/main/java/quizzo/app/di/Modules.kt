package quizzo.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import quizzo.app.data.database.QuizzoDatabase
import quizzo.app.data.network.NetworkDataSource
import quizzo.app.data.network.NetworkDataSourceImpl
import quizzo.app.data.network.service.QuizzoApiService
import quizzo.app.data.network.socket.MatchMakingSocket
import quizzo.app.data.network.socket.MatchSocket
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.data.repository.QuizzoRepositoryImpl
import quizzo.app.ui.auth.AuthViewModel
import quizzo.app.ui.category.CategoryPageViewModel
import quizzo.app.ui.history.HistoryViewModel
import quizzo.app.ui.home.HomeViewModel
import quizzo.app.ui.multiplayer.match.MultiplayerViewModel
import quizzo.app.ui.multiplayer.matchmaking.MatchMakingViewModel
import quizzo.app.ui.multiplayer.result.MultiplayerResultViewModel
import quizzo.app.ui.singleplayer.SinglePlayerViewModel
import quizzo.app.ui.singleplayer.result.SinglePlayerResultViewModel

val databaseModule = module {
    single { QuizzoDatabase(get()) }
    factory { get<QuizzoDatabase>().historyDao() }
    factory { get<QuizzoDatabase>().userDao() }
}

val networkModule = module {
    single { QuizzoApiService(get()) }
    single { MatchMakingSocket() }
    single { MatchSocket() }
    single<NetworkDataSource> { NetworkDataSourceImpl(get(), get(), get()) }
}

val repositoryModule = module {
    single<QuizzoRepository> { QuizzoRepositoryImpl(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SinglePlayerViewModel(get()) }
    viewModel { SinglePlayerResultViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { CategoryPageViewModel(get()) }
    viewModel { MatchMakingViewModel(get()) }
    viewModel { MultiplayerViewModel(get()) }
    viewModel { MultiplayerResultViewModel(get()) }
}
