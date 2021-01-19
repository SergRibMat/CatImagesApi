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

package com.example.catimagesapi

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.catimagesapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    var permissionGiven: Boolean = false

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUp()

    }
    fun setUp(){
            catApiMethod()

            binding.nextImageBtn.setOnClickListener {
                catApiMethod()
            }
    }


    fun catApiMethod(){
        permissionGiven = permissionBoolean()
        if(permissionGiven) {
            showInternetAvailableLayout(true)
            showNoInternetLayout(false)

            internetResponse()
        }else{
            showInternetAvailableLayout(false)
            showNoInternetLayout(true)
        }
    }

    fun internetResponse(){
        var viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

        uiScope.launch {
            val imageUrlString = retrofitService.getProperties()[0].url
            printImageWithGlide(imageUrlString)
            viewModelJob.cancel()
        }
    }


    fun makeLayoutItVisible(bool: Boolean): Int = if (bool) View.VISIBLE else View.GONE

    fun showInternetAvailableLayout(bool: Boolean){
        binding.internetAvailableLayout.visibility = makeLayoutItVisible(bool)
    }

    fun showNoInternetLayout(bool: Boolean){
        binding.noInternetLayout.visibility = makeLayoutItVisible(bool)
    }

    private fun printImageWithGlide(data: Any?) {
        Glide.with(this)
            .load(data)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(findViewById(R.id.show_picture_imageview))
    }

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    private fun permissionBoolean(): Boolean{
        //this variable here holds, with am int, if the permission is given or not
        var permissionNumber: Int = checkSelfPermission(Manifest.permission.INTERNET)
        return permissionNumber == 0 && isNetworkAvailable()
    }

    override fun checkSelfPermission(permission: String): Int {
        return super.checkSelfPermission(permission)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}