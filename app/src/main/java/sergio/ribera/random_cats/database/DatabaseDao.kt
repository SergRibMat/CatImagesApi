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

import androidx.room.*

@Dao
interface CatCacheDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)//OK
    fun insertAllCats(vararg databaseCatObject: DatabaseCatObject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)//OK
    fun insertSingleCat(databaseCatObject: DatabaseCatObject)

    @Delete
    fun deleteSeveralCats(vararg databaseCatObject: DatabaseCatObject)//OK

    @Delete
    fun deleteCat(databaseCatObject: DatabaseCatObject)//OK

    @Query("SELECT * from cat_table_cache")//OK
    fun getAllCats(): List<DatabaseCatObject>

    @Query("SELECT * from cat_table_cache WHERE id = :id")//OK
    fun getSingleCat(id: Long): DatabaseCatObject


    @Query("SELECT * from cat_table_cache LIMIT 1")//OK
    fun getFirstCat(): DatabaseCatObject


    @Query("SELECT * from cat_table_cache ORDER BY id DESC LIMIT 1")//OK
    fun getLastCat(): DatabaseCatObject


    @Insert(onConflict = OnConflictStrategy.REPLACE)//OK
    fun insertUtilityClass(databaseUtilityClass: DatabaseUtilityClass)

    //use False in the boolean property, because if you used this method it means the
    //first cache has been created
    @Insert(onConflict = OnConflictStrategy.REPLACE)//OK
    fun insertImagePosition(databaseUtilityClass: DatabaseUtilityClass)

    @Query("SELECT * from utility_table_cache LIMIT 1")//OK
    fun getUtilityClass(): DatabaseUtilityClass

}