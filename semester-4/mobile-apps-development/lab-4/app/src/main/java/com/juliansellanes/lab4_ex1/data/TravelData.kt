package com.juliansellanes.lab4_ex1.data

object TravelData {

    val categories = listOf(
        AttractionCategory(
            id = "historic",
            title = "Historic",
            subtitle = "Historic landmarks in Toronto"
        ),
        AttractionCategory(
            id = "parks",
            title = "Parks",
            subtitle = "Relaxing outdoor places"
        ),
        AttractionCategory(
            id = "museums",
            title = "Museums",
            subtitle = "Art, science, and culture"
        ),
        AttractionCategory(
            id = "tourist",
            title = "Tourist",
            subtitle = "Popular iconic attractions"
        )
    )

    private val attractions = listOf(
        Attraction(
            id = "distillery_district",
            categoryId = "historic",
            name = "Distillery District",
            address = "55 Mill St, Toronto",
            description = "Historic pedestrian-only district with Victorian buildings.",
            latitude = 43.6503,
            longitude = -79.3596
        ),
        Attraction(
            id = "casa_loma",
            categoryId = "historic",
            name = "Casa Loma",
            address = "1 Austin Terrace, Toronto",
            description = "Historic castle and garden with city views.",
            latitude = 43.6780,
            longitude = -79.4094
        ),
        Attraction(
            id = "st_lawrence_market",
            categoryId = "historic",
            name = "St. Lawrence Market",
            address = "93 Front St E, Toronto",
            description = "Historic marketplace and one of Toronto’s best-known food spots.",
            latitude = 43.6487,
            longitude = -79.3715
        ),

        Attraction(
            id = "high_park",
            categoryId = "parks",
            name = "High Park",
            address = "1873 Bloor St W, Toronto",
            description = "Large park with trails, gardens, and a zoo.",
            latitude = 43.6465,
            longitude = -79.4637
        ),
        Attraction(
            id = "trinity_bellwoods",
            categoryId = "parks",
            name = "Trinity Bellwoods Park",
            address = "790 Queen St W, Toronto",
            description = "Popular downtown park for walking and relaxing.",
            latitude = 43.6473,
            longitude = -79.4141
        ),
        Attraction(
            id = "riverdale_park",
            categoryId = "parks",
            name = "Riverdale Park East",
            address = "550 Broadview Ave, Toronto",
            description = "Park with one of the best skyline views in the city.",
            latitude = 43.6679,
            longitude = -79.3525
        ),

        Attraction(
            id = "royal_ontario_museum",
            categoryId = "museums",
            name = "Royal Ontario Museum",
            address = "100 Queens Park, Toronto",
            description = "Museum focused on art, culture, and natural history.",
            latitude = 43.6677,
            longitude = -79.3948
        ),
        Attraction(
            id = "art_gallery_ontario",
            categoryId = "museums",
            name = "Art Gallery of Ontario",
            address = "317 Dundas St W, Toronto",
            description = "Large art museum with Canadian and international collections.",
            latitude = 43.6536,
            longitude = -79.3925
        ),
        Attraction(
            id = "ontario_science_centre",
            categoryId = "museums",
            name = "Ontario Science Centre",
            address = "770 Don Mills Rd, Toronto",
            description = "Science museum with interactive exhibits.",
            latitude = 43.7161,
            longitude = -79.3388
        ),

        Attraction(
            id = "cn_tower",
            categoryId = "tourist",
            name = "CN Tower",
            address = "290 Bremner Blvd, Toronto",
            description = "Toronto’s most iconic observation tower.",
            latitude = 43.6426,
            longitude = -79.3871
        ),
        Attraction(
            id = "toronto_islands",
            categoryId = "tourist",
            name = "Toronto Islands",
            address = "9 Queens Quay W, Toronto ferry area",
            description = "Scenic island area with beaches and skyline views.",
            latitude = 43.6205,
            longitude = -79.3781
        ),
        Attraction(
            id = "ripley_aquarium",
            categoryId = "tourist",
            name = "Ripley’s Aquarium of Canada",
            address = "288 Bremner Blvd, Toronto",
            description = "Aquarium near the CN Tower with underwater tunnels.",
            latitude = 43.6424,
            longitude = -79.3860
        )
    )

    fun attractionsForCategory(categoryId: String): List<Attraction> {
        return attractions.filter { it.categoryId == categoryId }
    }

    fun attractionById(attractionId: String): Attraction? {
        return attractions.find { it.id == attractionId }
    }

    fun categoryById(categoryId: String): AttractionCategory? {
        return categories.find { it.id == categoryId }
    }
}