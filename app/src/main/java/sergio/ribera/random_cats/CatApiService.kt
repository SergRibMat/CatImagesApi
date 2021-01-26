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

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


const val ROOT_URL = "https://api.thecatapi.com/v1/images/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(6, TimeUnit.SECONDS)//time-out applied to create a TCP connection to the HTTP server.
    .readTimeout(10, TimeUnit.SECONDS)//time passed between u connected and recieved the response
    .writeTimeout(10, TimeUnit.SECONDS)//fail to write in time. Its for IO operations
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(ROOT_URL)
    .client(okHttpClient)
    .build()




interface CatApiService {
    @GET("search")
    suspend fun getProperties():
            List<CatObjectApi>
}

//object CatApi {
    var retrofitService: CatApiService =
        retrofit.create(CatApiService::class.java)

//}


