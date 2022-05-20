package mjy.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import mjy.calculator.model.History
import mjy.calculator.dao.HistoryDao

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}