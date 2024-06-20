package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import com.openclassrooms.realestatemanager.provider.RealEstateAgentContentProvider
import com.openclassrooms.realestatemanager.utils.TestUtils
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest {

    private lateinit var contentUriEstate: Uri
    private lateinit var contentUriAgent: Uri
    private lateinit var contentResolver: ContentResolver
    private lateinit var database: RealEstateManagerDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,RealEstateManagerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        contentResolver = context.contentResolver
        contentUriEstate = EstateContentProvider.CONTENT_URI
        contentUriAgent = RealEstateAgentContentProvider.CONTENT_URI
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun testQueryNoItemInserted() {

        val estateId = 8L
        val uri = ContentUris.withAppendedId(contentUriEstate, estateId)
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        assertNotNull(cursor)
        assertEquals(0,cursor!!.count)
        cursor.close()
    }

    @Test
    fun testQueryWithInsert() {

        val estateId = 8L
        val agentId = 4L
        contentResolver.insert(contentUriAgent, TestUtils.generateAgentEntity(agentId))
        contentResolver.insert(contentUriEstate,TestUtils.generateEstateEntity(estateId,agentId))

        val uri = ContentUris.withAppendedId(contentUriEstate, estateId)
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        assertNotNull(cursor)
        assertEquals(1,cursor!!.count)
        assertTrue(cursor.moveToFirst())
        assertEquals(estateId, cursor.getLong(cursor.getColumnIndexOrThrow("id")))
        cursor.close()
    }

}
