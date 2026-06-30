package com.userexplorer.feature.users.presentation.list.contract

import com.userexplorer.core.domain.model.User

/**
 * Contract class containing all the UI states and intents for the Users feature.
 * This helps in maintaining a clear separation between the UI state and user actions.
 */
object UsersContract {
    sealed class State {
        /**
         * Represents the loading state.
         */
        data object Loading : State()
        
        /**
         * Represents the success state with loaded users.
         * @property users The list of successfully loaded users
         */
        data class Success(val users: List<User>) : State()
        
        /**
         * Represents the error state.
         * @property message The error message to display
         */
        data class Error(val message: String) : State()
    }

    /**
     * Sealed class representing the different user intents/actions that can be performed.
     */
    sealed class Intent {
        /**
         * Intent to load or refresh the users list
         */
        data object LoadUsers : Intent()

        /**
         * Intent to retry after an error
         */
        data object Retry : Intent()
    }
} 