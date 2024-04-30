package com.openclassrooms.realestatemanager.data.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.data.FakeData
import com.openclassrooms.realestatemanager.data.models.Estate

class EstateRepository {

    fun getEstates(): LiveData<List<Estate>> {
        return FakeData.getFakeEstates()
    }

    fun getEstateById(estateId: Long): LiveData<Estate?> {
        return FakeData.getFakeEstateById(estateId)
    }
}
