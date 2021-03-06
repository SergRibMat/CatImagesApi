/*
 *  Copyright 2020 Sergio Ribera
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package sergio.ribera.random_cats.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseCatObject::class, DatabaseUtilityClass::class], version = 1,  exportSchema = false)
abstract class CatCacheDatabase : RoomDatabase() {

    abstract val catCacheDatabaseDao: CatCacheDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: CatCacheDatabase? = null

        fun getInstance(context: Context): CatCacheDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CatCacheDatabase::class.java,
                        "cache_cat_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance

                }
                return instance
            }
        }
    }

}