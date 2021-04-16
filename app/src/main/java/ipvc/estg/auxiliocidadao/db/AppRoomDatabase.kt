package ipvc.estg.auxiliocidadao.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipvc.estg.auxiliocidadao.dao.NotaDao
import ipvc.estg.auxiliocidadao.entity.Nota

@Database(
    entities = [Nota::class],
    version = 2,
    exportSchema = true
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getDatabase(context: Context): ipvc.estg.auxiliocidadao.db.AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance as ipvc.estg.auxiliocidadao.db.AppRoomDatabase
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "notas_database"
                ).fallbackToDestructiveMigration()          // TODO: migration
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}