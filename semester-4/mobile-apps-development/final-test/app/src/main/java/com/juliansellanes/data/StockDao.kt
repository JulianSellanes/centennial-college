package com.juliansellanes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stockInfo: StockInfo): Long

    @Query("SELECT * FROM stock_info ORDER BY stockSymbol ASC")
    fun getAllStocks(): Flow<List<StockInfo>>

    @Query("SELECT * FROM stock_info WHERE stockSymbol = :symbol LIMIT 1")
    fun getStockBySymbol(symbol: String): Flow<StockInfo?>
}