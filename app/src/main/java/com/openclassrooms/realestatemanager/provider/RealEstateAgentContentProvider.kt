package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.MainApplication
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface

/**
 * ContentProvider for accessing real estate agent data
 */
class RealEstateAgentContentProvider : ContentProvider() {

    /**
     * The real estate agent repository
     */
    private lateinit var realEstateAgentRepository: RealEstateAgentRepositoryInterface

    /**
     * Initializes the ContentProvider and retrieves the real estate agent repository
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    override fun onCreate(): Boolean {
        context?.let { context ->
            val application = context.applicationContext as MainApplication
            realEstateAgentRepository = application.realEstateAgentRepository
        } ?: return false
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {

        return null
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    /**
     * Inserts a new row into the real estate agent repository
     *
     * @param uri The content URI
     * @param contentValues The values to insert
     * @return The URI of the newly inserted row
     */
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        val currentContext = context
        if (currentContext != null && contentValues != null) {
            val id = realEstateAgentRepository.insertFromContentValues(contentValues)
            if (id != 0L) {
                currentContext.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        @Suppress("SpellCheckingInspection")
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider.realEstateAgent"
        val TABLE_NAME: String = RealEstateAgentEntity::class.java.simpleName
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }
}
