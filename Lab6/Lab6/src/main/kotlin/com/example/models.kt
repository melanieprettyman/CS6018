package com.example

import org.jetbrains.exposed.dao.id.IntIdTable

// Post table schema using Exposed
object Post : IntIdTable() {
    val content = text("content")
    val time = long("time")
}