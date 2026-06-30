package com.userexplorer.core.domain.model

/**
 * Represents a user in the system.
 * Contains all essential user information including personal details,
 * contact information, and location data.
 *
 * @property id Unique identifier for the user
 * @property name Full name of the user
 * @property username User's login name
 * @property email User's email address
 * @property phone User's contact phone number
 * @property company Name of the user's company
 * @property address User's street address
 * @property zip Postal/ZIP code
 * @property state State/Province
 * @property country Country name
 * @property photo URL to the user's profile photo
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val company: String,
    val address: String,
    val zip: String,
    val state: String,
    val country: String,
    val photo: String
)