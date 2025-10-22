package com.example.androidarticlessample.domain.util

/**
 * Represents the time period for fetching most viewed articles.
 */
enum class Period(val days: Int) {
    ONE_DAY(1),
    SEVEN_DAYS(7),
    THIRTY_DAYS(30);

    companion object {
        fun fromDays(days: Int): Period? =
            entries.firstOrNull { it.days == days }
    }
}