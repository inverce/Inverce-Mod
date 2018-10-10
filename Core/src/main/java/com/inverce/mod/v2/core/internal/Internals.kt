package com.inverce.mod.v2.core.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.support.annotation.RestrictTo
import com.inverce.mod.v2.core.IMInitializer
import java.lang.ref.WeakReference

/**
 * Base initialization provider all init providers should use it as base (it will make sure to initialize core first)
 */
abstract class InitializationProvider: ContentProvider() {
    override fun onCreate(): Boolean {
        IMInitializer.initialize(this.context)
        return initialize()
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0
    abstract fun initialize() : Boolean
}

/**
 * This is basically so we can say we initialized im core ^^
 */
class InitializationContentProvider : InitializationProvider() {
    override fun initialize(): Boolean = true
}

@SuppressLint("StaticFieldLeak")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal object IMInternal {
    var context: Context? = null
        internal set
    var activity = WeakReference<Activity>(null)
    var isInEdit: Boolean = false
        internal set
}