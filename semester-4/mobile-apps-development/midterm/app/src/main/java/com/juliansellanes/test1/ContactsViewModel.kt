package com.juliansellanes.test1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {

    private val _contacts = mutableStateListOf<Contact>()
    val contacts: List<Contact> = _contacts

    fun addContact(contact: Contact) {
        _contacts.add(contact)
    }
}