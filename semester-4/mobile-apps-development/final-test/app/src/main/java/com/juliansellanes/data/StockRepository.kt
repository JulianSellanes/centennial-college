package com.juliansellanes.data

import kotlinx.coroutines.flow.Flow

class StockRepository(private val stockDao: StockDao) {

    val allStocks: Flow<List<StockInfo>> = stockDao.getAllStocks()

    suspend fun insertStock(stockInfo: StockInfo): Long {
        return stockDao.insert(stockInfo)
    }

    fun getStockBySymbol(symbol: String): Flow<StockInfo?> {
        return stockDao.getStockBySymbol(symbol)
    }
}