package com.example.familydirectory.data.model

data class BibleQuote(
    val malayalamText: String,
    val englishText: String,
    val reference: String,
    val malayalamReference: String,
    val dayOfYear: Int // 1-365
)