package com.userexplorer.feature.users.presentation.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.repository.UserRepository
import com.userexplorer.core.domain.util.Result
import com.userexplorer.feature.users.presentation.list.contract.UsersContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the users list screen state and business logic.
 * Handles loading users from the repository and exposing the UI state.
 *
 * @property repository The repository used to fetch user data
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersContract.State>(UsersContract.State.Loading)
    
    /**
     * Exposes the current UI state as an immutable StateFlow.
     * The UI state can be one of:
     * - Loading: Initial state or when refreshing data
     * - Success: When users are successfully loaded
     * - Error: When there's an error loading users
     */
    val uiState: StateFlow<UsersContract.State> = _uiState.asStateFlow()

    private var loadUsersJob: Job? = null

    init {
        loadUsers()
    }

    /**
     * Handles user intents and triggers appropriate actions
     * @param intent The user intent to handle
     */
    fun handleIntent(intent: UsersContract.Intent) {
        when (intent) {
            is UsersContract.Intent.LoadUsers -> loadUsers()
            is UsersContract.Intent.Retry -> loadUsers()
        }
    }

    /**
     * Finds a user by id from the currently loaded users list.
     */
    fun findUser(userId: Int): User? {
        val state = _uiState.value
        return if (state is UsersContract.State.Success) {
            state.users.find { it.id == userId }
        } else {
            null
        }
    }

    /**
     * Loads users from the repository.
     * Updates the UI state based on the result:
     * - Sets Loading state while fetching
     * - Sets Success state with users if successful
     * - Sets Error state with message if failed
     */
    private fun loadUsers() {
        loadUsersJob?.cancel()
        loadUsersJob = viewModelScope.launch {
            _uiState.value = UsersContract.State.Loading

            when (val result = repository.getUsers()) {
                is Result.Success -> {
                    ensureActive()
                    _uiState.value = UsersContract.State.Success(result.data)
                }
                is Result.Error -> {
                    ensureActive()
                    _uiState.value = UsersContract.State.Error(result.message)
                }
                is Result.Loading -> {
                    ensureActive()
                    _uiState.value = UsersContract.State.Loading
                }
            }
        }
    }
}