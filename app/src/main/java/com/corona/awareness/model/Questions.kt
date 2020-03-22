package com.corona.awareness.model

data class Questions(
    val id: Int,
    val text: String,
    val answer: MutableList<Answers>
)