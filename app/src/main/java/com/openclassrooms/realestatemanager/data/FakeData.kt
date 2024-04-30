package com.openclassrooms.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.PropertyType
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import java.sql.Timestamp
import java.util.Arrays
import java.util.Date

object FakeData {

    private val estateList = listOf(
        Estate(
            1, PropertyType.APARTMENT, 7000000, 750,
            1, 1,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
            listOf(
                Photo(1,"https://source.unsplash.com/random/?Place&1","Photo 1"),
                Photo(2,"https://source.unsplash.com/random/?Place&2","Photo 2"),
                Photo(3,"https://source.unsplash.com/random/?Place&3","Photo 3")
            ),
            "740 Park Avenue", "Apt6/7A","New York",
            "NY 10021", "United States",null, true,
            Timestamp(Date().time), null, RealEstateAgent(1, "Durant Michel")
        ),
        Estate(
            2, PropertyType.HOUSE, 15000000, 1750,
            2, 4,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
            listOf(
                Photo(4,"https://source.unsplash.com/random/?Place&4","Photo 4")
            ), "760 Park Avenue", null,"New York",
            "NY 10021", "United States", null, true,
            Timestamp(Date().time), null, RealEstateAgent(1, "Lapage Melodie")
        ),
        Estate(
            3, PropertyType.MANOR, 28000000, 2100,
            4, 6,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, eros tincidunt molestie viverra, nulla nunc vulputate magna, eget volutpat lorem purus molestie leo. Ut vestibulum pharetra metus, blandit tristique sem lacinia at. Fusce scelerisque sed nulla cursus elementum. Sed id euismod urna. Cras rutrum quis risus et faucibus. Suspendisse nec mi iaculis, dignissim neque sit amet, pellentesque purus. Aenean ultrices rutrum felis eu aliquam. Cras tincidunt blandit erat. Nulla facilisi.",
            listOf(
                Photo(5,"https://source.unsplash.com/random/?Place&5","Photo 5")
            ),
            "610 Park Avenue", null,"New York",
            "NY 10021", "United States", null, true,
            Timestamp(Date().time), null, RealEstateAgent(1, "Durant Michel")
        )
    );

    fun getFakeEstates():  LiveData<List<Estate>> {
        return MutableLiveData(estateList)
    }

    fun getFakeEstateById(estateId: Long): LiveData<Estate?> {
        val estateLiveData = MutableLiveData<Estate?>()
        estateLiveData.value = estateList.find { it.id == estateId }
        return estateLiveData
    }

}
