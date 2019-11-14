package com.ydhnwb.moviecatalogue.databases

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri


open class DatabaseHelper(var context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "ydhnwb_movie.db"
        const val DATABASE_VERSION = 1
        const val TABLE_MOVIE = "MOVIES"
        const val COLUMN_ID = "_ID"
        const val COLUMN_MOVIE_ID = "MOVIE_ID"
        const val COLUMN_MOVIE_TITLE = "MOVIE_TITLE"
        const val COLUMN_MOVIE_OVERVIEW = "MOVIE_OVERVIEW"
        const val COLUMN_MOVIE_BACKDROP = "MOVIE_BACKDROP"
        const val COLUMN_MOVIE_POSTER = "MOVIE_POSTER"
        const val COLUMN_MOVIE_RATING = "MOVIE_RATING"
        const val COLUMN_MOVIE_RELEASE_DATE = "MOVIE_DATE"
        const val COLUMN_MOVIE_VOTE_COUNT = "MOVIE_VOTE_COUNT"

        const val CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_MOVIE ($COLUMN_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $COLUMN_MOVIE_ID INTEGER NOT NULL, $COLUMN_MOVIE_TITLE TEXT NOT NULL, $COLUMN_MOVIE_OVERVIEW TEXT, " +
    "$COLUMN_MOVIE_BACKDROP, $COLUMN_MOVIE_POSTER TEXT, $COLUMN_MOVIE_RATING FLOAT,$COLUMN_MOVIE_RELEASE_DATE TEXT ,$COLUMN_MOVIE_VOTE_COUNT LONG,UNIQUE (MOVIE_ID) ON CONFLICT REPLACE)"

        const val CONTENT_AUTHORITY = "com.ydhnwb.moviecatalogue"
        var CONTENT_URI = Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build()
    }

    override fun onCreate(p0: SQLiteDatabase?) { p0?.execSQL(CREATE_MOVIE_TABLE) }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        onCreate(p0)
    }
}

class MovieHelper(var context: Context) {
    private var db : SQLiteDatabase
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)

    init {
        db = dbHelper.readableDatabase
    }

    fun close() = db.close()
    fun queryAllProvider() : Cursor {
        return db.query(DatabaseHelper.TABLE_MOVIE,null , null, null, null, null, "${DatabaseHelper.COLUMN_ID} DESC")
    }
    fun queryByIdProvider(movie_id : String) : Cursor = db.query(DatabaseHelper.TABLE_MOVIE, null, DatabaseHelper.COLUMN_MOVIE_ID + " = ?", arrayOf(movie_id), null, null, null, null)
    fun insertProvider(values : ContentValues)  = db.insert(DatabaseHelper.TABLE_MOVIE, null, values)
    fun updateProvider(movie_id : String, values : ContentValues)= db.update(DatabaseHelper.TABLE_MOVIE, values, DatabaseHelper.COLUMN_MOVIE_ID + " = ?", arrayOf(movie_id))
    fun deleteProvider(movie_id : String) = db.delete(DatabaseHelper.TABLE_MOVIE, DatabaseHelper.COLUMN_MOVIE_ID + " = ?", arrayOf(movie_id))

}

class DatabaseProvider : ContentProvider() {
    private lateinit var movieHelper : MovieHelper
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        const val MOVIES = 100
        const val MOVIES_WITH_ID = 101
    }

    private fun init(){
        uriMatcher.addURI(DatabaseHelper.CONTENT_AUTHORITY, DatabaseHelper.TABLE_MOVIE, MOVIES)
        uriMatcher.addURI(DatabaseHelper.CONTENT_AUTHORITY, DatabaseHelper.TABLE_MOVIE + "/#", MOVIES_WITH_ID)
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val added = when(uriMatcher.match(p0)){
            MOVIES -> {
                movieHelper.insertProvider(p1!!)
            }
            else -> 0
        }
        if (added > 0){
            context?.contentResolver?.notifyChange(p0, null)
        }
        return Uri.parse("${DatabaseHelper.CONTENT_URI}/$added")
    }

    override fun query(p0: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sort: String?): Cursor? {
        val match = uriMatcher.match(p0)
        var cursor : Cursor? = null
        when(match){
            MOVIES -> { cursor = movieHelper.queryAllProvider()  }
            MOVIES_WITH_ID -> { cursor = movieHelper.queryByIdProvider(p0.lastPathSegment!!) }
        }
        cursor?.setNotificationUri(context?.contentResolver, p0)
        return cursor
    }

    override fun onCreate(): Boolean {
        init()
        movieHelper = MovieHelper(context!!)
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val updated = when (uriMatcher.match(p0)) {
            MOVIES_WITH_ID -> {
                movieHelper.updateProvider(p0.lastPathSegment!!, p1!!)
            }
            else -> 0
        }
        if (updated > 0) {
            context!!.contentResolver.notifyChange(p0, null)
        }
        return updated
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val movieDeleted = when (uriMatcher.match(p0)) {
            MOVIES_WITH_ID -> {
                movieHelper.deleteProvider(p0.lastPathSegment!!)
            }
            else -> 0
        }
        if (movieDeleted > 0) { context!!.contentResolver.notifyChange(p0, null) }
        return movieDeleted

    }

    override fun getType(p0: Uri): String? = null

}