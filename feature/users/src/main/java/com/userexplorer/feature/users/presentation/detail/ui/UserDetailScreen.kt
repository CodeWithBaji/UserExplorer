package com.userexplorer.feature.users.presentation.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.userexplorer.core.domain.model.User
import com.userexplorer.feature.users.R

/**
 * Detailed view of a user's information.
 * Displays a user's profile photo and detailed information in a scrollable layout.
 * The screen is divided into sections for personal information and location details.
 *
 * @param user The user whose details are being displayed
 * @param onBackClick Callback invoked when the back button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    user: User,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_details_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Photo
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photo)
                    .build(),
                contentDescription = stringResource(
                    R.string.profile_photo_content_description,
                    user.name
                ),
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.spacing_xlarge))
                    .size(dimensionResource(R.dimen.profile_photo_size_large))
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    PlaceholderIcon()
                },
                error = {
                    PlaceholderIcon()
                }
            )
            
            UserDetailContent(
                user = user,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_large))
            )
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
        modifier = Modifier.size(dimensionResource(R.dimen.profile_photo_size_large)),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * Displays detailed user information organized in sections.
 * Contains two main sections:
 * 1. Personal Information - Name, username, email, phone, and company
 * 2. Location - Address, ZIP code, state, and country
 *
 * @param user The user whose details are being displayed
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
private fun UserDetailContent(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
    ) {
        DetailSection(title = stringResource(R.string.personal_information_section)) {
            DetailItem(label = stringResource(R.string.name_label), value = user.name)
            DetailItem(label = stringResource(R.string.username_label), value = user.username)
            DetailItem(label = stringResource(R.string.email_label), value = user.email)
            DetailItem(label = stringResource(R.string.phone_label), value = user.phone)
            DetailItem(label = stringResource(R.string.company_label), value = user.company)
        }

        DetailSection(title = stringResource(R.string.location_section)) {
            DetailItem(label = stringResource(R.string.address_label), value = user.address)
            DetailItem(label = stringResource(R.string.zip_label), value = user.zip)
            DetailItem(label = stringResource(R.string.state_label), value = user.state)
            DetailItem(label = stringResource(R.string.country_label), value = user.country)
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
    }
}

/**
 * A section component that groups related information under a title.
 * The content is displayed in a card with proper spacing and styling.
 *
 * @param title The title of the section
 * @param content The composable content to display within the section
 */
@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                content()
            }
        }
    }
}

/**
 * A component that displays a label-value pair.
 * Used for showing individual pieces of user information.
 *
 * @param label The label describing the type of information
 * @param value The actual value to display
 */
@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 