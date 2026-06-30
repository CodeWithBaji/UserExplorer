package com.userexplorer.feature.users.presentation.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.userexplorer.core.domain.model.User
import com.userexplorer.feature.users.R
import com.userexplorer.feature.users.presentation.common.LoadingScreen
import com.userexplorer.feature.users.presentation.list.contract.UsersContract
import com.userexplorer.feature.users.presentation.list.viewmodel.UsersViewModel

/**
 * Main screen that displays a list of users.
 * This screen handles four states:
 * 1. Loading - Shows a loading indicator
 * 2. Success - Displays the list of users
 * 3. Success (empty) - Shows a message when no users are available
 * 4. Error - Shows an error message with a retry button
 *
 * @param viewModel The ViewModel that manages the user data and state
 * @param onUserClick Callback invoked when a user card is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onUserClick: (User) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.users_screen_title)) }
            )
        }
    ) { padding ->
        when (uiState) {
            is UsersContract.State.Loading -> {
                LoadingScreen(modifier = Modifier.padding(padding))
            }
            is UsersContract.State.Success -> {
                val users = (uiState as UsersContract.State.Success).users
                if (users.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_users_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    UserList(
                        users = users,
                        onUserClick = onUserClick,
                        modifier = Modifier.padding(padding),
                    )
                }
            }
            is UsersContract.State.Error -> {
                val message = (uiState as UsersContract.State.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                    ) {
                        Text(text = message)
                        Button(onClick = { viewModel.handleIntent(UsersContract.Intent.Retry) }) {
                            Text(stringResource(R.string.retry_button))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays a scrollable list of users.
 *
 * @param users List of users to display
 * @param onUserClick Callback invoked when a user card is clicked
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
private fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        items(
            items = users,
            key = { user -> user.id },
        ) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) }
            )
        }
    }
}

/**
 * A card component that displays user information.
 * Shows the user's profile photo, name, email, and company in a structured layout.
 *
 * @param user The user data to display
 * @param onClick Callback invoked when the card is clicked
 * @param modifier Optional modifier for customizing the layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCard(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.spacing_large))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photo)
                    .build(),
                contentDescription = stringResource(
                    R.string.profile_photo_content_description,
                    user.name
                ),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.profile_photo_size_small))
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    PlaceholderIcon()
                },
                error = {
                    PlaceholderIcon()
                }
            )

            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                Text(
                    text = user.company,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * A placeholder icon displayed while the user's profile photo is loading
 * or if there's an error loading the photo.
 */
@Composable
private fun PlaceholderIcon() {
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null,
        modifier = Modifier.size(dimensionResource(R.dimen.profile_photo_size_small)),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}