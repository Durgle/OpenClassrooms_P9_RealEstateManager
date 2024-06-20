package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.MainApplication
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface

class EstateContentProvider : ContentProvider() {

    private lateinit var estateRepository: EstateRepositoryInterface

    override fun onCreate(): Boolean {
        context?.let { context ->
            val application = context.applicationContext as MainApplication
            estateRepository = application.estateRepository
        }?: return false
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {

        val currentContext = context
        if (currentContext != null) {
            val estateId = ContentUris.parseId(uri)

            val cursor = estateRepository.getEstateByIdWithCursor(estateId)

            cursor.setNotificationUri(currentContext.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        val currentContext = context
        if (currentContext != null && contentValues != null) {
           val id = estateRepository.insertFromContentValues(contentValues)
            if (id != 0L){
                currentContext.contentResolver.notifyChange(uri,null)
                return ContentUris.withAppendedId(uri,id)
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
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider.estate"
        val TABLE_NAME: String = EstateEntity::class.java.simpleName
        val CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME)
    }
}
