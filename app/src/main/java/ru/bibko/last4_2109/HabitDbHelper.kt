package ru.bibko.last4_2109

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HabitDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "habits.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_HABITS = "habits"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CREATED_AT = "createdAt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_HABITS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_CREATED_AT TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HABITS")
        onCreate(db)
    }

    fun insertHabit(title: String, description: String, createdAt: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_CREATED_AT, createdAt)
        }
        return db.insert(TABLE_HABITS, null, values)
    }

    fun getAllHabitsSortedByTitle(): List<String> {
        val habits = mutableListOf<String>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_HABITS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TITLE COLLATE NOCASE ASC"
        )

        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val createdAt = getString(getColumnIndexOrThrow(COLUMN_CREATED_AT))

                habits.add("Название: $title\nОписание: $description\nДата: $createdAt")
            }
            close()
        }

        return habits
    }

    fun deleteAllHabits() {
        val db = writableDatabase
        db.delete(TABLE_HABITS, null, null)
    }
}