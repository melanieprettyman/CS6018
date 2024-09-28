package com.example.makart

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import androidx.room.Update



class LineTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLinesList(lines: List<Line>): String {
        return gson.toJson(lines)
    }

    @TypeConverter
    fun toLinesList(linesString: String): List<Line> {
        val listType = object : TypeToken<List<Line>>() {}.type
        return gson.fromJson(linesString, listType)
    }
}

@Entity(tableName = "drawings")
@TypeConverters(LineTypeConverter::class) // Add a converter to store list of lines
data class DrawingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val lines: List<Line> // Store the lines as a list
)

@Dao
interface DrawingDao {
    @Query("SELECT * FROM drawings ORDER BY id DESC")
    fun getAllDrawings(): Flow<List<DrawingEntity>>

    @Query("SELECT * FROM drawings WHERE id = :drawingId")
    suspend fun getDrawingById(drawingId: Int): DrawingEntity?

    @Insert
    suspend fun insertDrawing(drawing: DrawingEntity)

    @Update
    suspend fun updateDrawing(drawing: DrawingEntity)
}

@Database(entities = [DrawingEntity::class], version = 1, exportSchema = false)
abstract class DrawingDatabase : RoomDatabase() {

    abstract fun drawingDao(): DrawingDao

    companion object {
        @Volatile
        private var INSTANCE: DrawingDatabase? = null

        fun getDatabase(context: Context): DrawingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use application context to avoid leaking an activity context
                    DrawingDatabase::class.java,
                    "drawing_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}