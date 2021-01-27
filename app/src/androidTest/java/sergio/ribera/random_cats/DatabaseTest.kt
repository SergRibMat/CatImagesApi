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

package sergio.ribera.random_cats

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sergio.ribera.random_cats.database.CatCacheDatabase
import sergio.ribera.random_cats.database.CatCacheDatabaseDao
import sergio.ribera.random_cats.database.DatabaseCatObject
import sergio.ribera.random_cats.database.DatabaseUtilityClass
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var catCacheDatabaseDao: CatCacheDatabaseDao
    private lateinit var db: CatCacheDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, CatCacheDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        catCacheDatabaseDao = db.catCacheDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    fun insertCat(){
        catCacheDatabaseDao.insertSingleCat(
            DatabaseCatObject(
                0,
                "JsonId",
                "url"
            )
        )
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCat() {
        insertCat()
        val cat = catCacheDatabaseDao.getSingleCat(1)
        Assert.assertEquals(cat.url, "url")
    }

    fun insertSeveralCats(){
        catCacheDatabaseDao.insertAllCats(
            DatabaseCatObject(0, "jsonId", "FirstUrl"),
            DatabaseCatObject(0, "jsonId", "SecondUrl"),
            DatabaseCatObject(0, "jsonId", "ThirdUrl")
        )
    }

    fun updateWithInsertCat(){
        catCacheDatabaseDao.insertSingleCat(
            DatabaseCatObject(
                1,
                "JsonId",
                "newUrl"
            )
        )
    }

    @Test
    fun insertUpdateAndGetCat(){
        insertCat()
        updateWithInsertCat()
        val cat = catCacheDatabaseDao.getSingleCat(1)
        Assert.assertEquals(cat.url, "newUrl")
    }

    @Test
    fun insertAndGetSeveralCats(){
        insertSeveralCats()
        val catList = catCacheDatabaseDao.getAllCats()
        Assert.assertEquals(3, catList.size)
    }

    fun updateSeveralCats(){
        catCacheDatabaseDao.insertAllCats(
            DatabaseCatObject(1, "jsonId2", "FirstUrl"),
            DatabaseCatObject(2, "jsonId2", "SecondUrl"),
            DatabaseCatObject(3, "jsonId2", "ThirdUrl")
        )
    }

    @Test
    fun insertUpdateAndGetSeveralCats(){
        insertSeveralCats()
        updateSeveralCats()
        val catList = catCacheDatabaseDao.getAllCats()
        catList.forEach{databaseCatObject ->
            Assert.assertEquals(databaseCatObject.jsonIdString, "jsonId2")
        }
    }

    @Test
    fun deleteSingleCat(){
        insertSeveralCats()
        catCacheDatabaseDao.deleteCat(
            DatabaseCatObject(1, "jsonId", "FirstUrl")
        )
        val catList = catCacheDatabaseDao.getAllCats()
        Assert.assertEquals(2, catList.size)
    }

    @Test
    fun deleteSeveralCats(){
        insertSeveralCats()
        catCacheDatabaseDao.deleteSeveralCats(
            DatabaseCatObject(2, "jsonId2", "SecondUrl"),
            DatabaseCatObject(3, "jsonId2", "ThirdUrl")
        )
        val catList = catCacheDatabaseDao.getAllCats()
        Assert.assertEquals(1, catList.size)
    }

    @Test
    fun getFirstCat(){
        insertSeveralCats()
        val cat = catCacheDatabaseDao.getFirstCat()
        Assert.assertEquals(1, cat.id)
    }

    @Test
    fun getLastCat(){
        insertSeveralCats()
        val cat = catCacheDatabaseDao.getLastCat()
        Assert.assertEquals(3, cat.id)
    }

    @Test
    fun insertAndGetUtilityClass(){
        catCacheDatabaseDao.insertUtilityClass(DatabaseUtilityClass(1, true))
        val utilityClass = catCacheDatabaseDao.getUtilityClass()
        Assert.assertEquals(1, utilityClass.imageId)
    }

    @Test
    fun updateAndGetImageId(){
        catCacheDatabaseDao.insertImagePosition(DatabaseUtilityClass(5, false))
        val utilityClass = catCacheDatabaseDao.getUtilityClass()
        Assert.assertEquals(5, utilityClass.imageId)
    }
}