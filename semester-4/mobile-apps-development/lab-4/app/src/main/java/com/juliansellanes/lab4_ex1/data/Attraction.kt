package com.juliansellanes.lab4_ex1.data

import com.google.android.gms.maps.model.LatLng

data class AttractionCategory(
    val id: String,
    val title: String,
    val subtitle: String
)

data class Attraction(
    val id: String,
    val categoryId: String,
    val name: String,
    val address: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) {
    val latLng: LatLng
        get() = LatLng(latitude, longitude)
}