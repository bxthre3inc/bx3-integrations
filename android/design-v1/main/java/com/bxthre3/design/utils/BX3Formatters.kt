package com.bxthre3.design.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

object BX3Formatters {
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    private val percentFormatter = NumberFormat.getPercentInstance()
    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.US)
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.US)
    
    fun currency(amount: Double): String = currencyFormatter.format(amount)
    fun percent(value: Double): String = percentFormatter.format(value)
    fun date(timestamp: Long): String = dateFormatter.format(Date(timestamp))
    fun time(timestamp: Long): String = timeFormatter.format(Date(timestamp))
    fun relativeTime(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        return when {
            diff < 60000 -> "just now"
            diff < 3600000 -> "${diff.milliseconds.inWholeMinutes}m ago"
            diff < 86400000 -> "${diff.milliseconds.inWholeHours}h ago"
            else -> date(timestamp)
        }
    }
}
