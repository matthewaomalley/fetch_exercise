package com.fetch.matthewomalley

// data class to convert raw JSON into instance of kotlin class
data class ListItem(
    val id: Int,
    val listId: Int,
    val name: String? // nullable in case name is null
)
