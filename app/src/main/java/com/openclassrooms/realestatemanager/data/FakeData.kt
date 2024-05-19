package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

object FakeData {

    private val estateList = listOf(
        EstateWithPhotosEntity(
            EstateEntity(
                1, PropertyType.APARTMENT, 7000000, 750,
                1, 1,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
                "740 Park Avenue", "Apt6/7A", "New York",
                "NY 10021", "United States", 39.58369307623072,
                -104.88624551681441, null, true,
                Date().time, null, 1
            ), listOf(
                PhotoEntity(1, "https://source.unsplash.com/random/?RealEstate&1", "Photo 1", 1),
                PhotoEntity(2, "https://source.unsplash.com/random/?RealEstate&2", "Photo 2", 1),
                PhotoEntity(3, "https://source.unsplash.com/random/?RealEstate&3", "Photo 3", 1)
            ),
            RealEstateAgentEntity(1, "Agent 1")
        ),
        EstateWithPhotosEntity(
            EstateEntity(
                2, PropertyType.HOUSE, 15000000, 1750,
                2, 4,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
                "760 Park Avenue", null, "New York",
                "NY 10021", "United States", 39.733922539377986,
                -105.03180154232744, null, true,
                Date().time, null, 2
            ), listOf(
                PhotoEntity(4, "https://source.unsplash.com/random/?RealEstate&4", "Photo 4", 2)
            ),
            RealEstateAgentEntity(1, "Agent 2")
        ),
        EstateWithPhotosEntity(
            EstateEntity(
                3, PropertyType.MANOR, 28000000, 2100,
                4, 6,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
                "610 Park Avenue", null, "New York",
                "NY 10021", "United States", 39.753348594578085,
                -105.02557896690276, null, true,
                Date().time, null, 1
            ), listOf(
                PhotoEntity(5, "https://source.unsplash.com/random/?RealEstate&5", "Photo 5", 3)
            ),
            RealEstateAgentEntity(1, "Agent 1")
        )
    )

    fun getFakeEstates(): Flow<List<EstateWithPhotosEntity>> {
        return flow { estateList }
    }

    fun getFakeEstateById(estateId: Long): Flow<EstateWithPhotosEntity?> {
        return flow { estateList.find { it.estate.id == estateId } }
    }

}
