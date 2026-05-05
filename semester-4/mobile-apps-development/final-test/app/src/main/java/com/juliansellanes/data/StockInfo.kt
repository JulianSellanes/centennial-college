package com.juliansellanes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_info")
data class StockInfo(
    @PrimaryKey val stockSymbol: String,
    val companyName: String,
    val stockQuote: Double,
    val sharesSold: Int
)