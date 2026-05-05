package com.juliansellanes.test1

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val contactType: ContactType,
    val favorite: FavoriteLevel
)

enum class ContactType { Business, Work }
enum class FavoriteLevel { Preferred, Important, Normal }