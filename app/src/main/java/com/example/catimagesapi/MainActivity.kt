package com.example.catimagesapi

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.catimagesapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var permissionGiven: Boolean = false

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUp()

        binding.checkInternetBtn.setOnClickListener {
            showToast(permissionBoolean().toString())
        }
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
            val imageUrlString = retrofitService.getProperties().get(0).url
            printImageWithGlide(imageUrlString)
            viewModelJob.cancel()
        }

        //suspend fun getProperties() = retrofitService.getProperties().get(0).url



        /*retrofitService.getProperties().enqueue(object : Callback<List<CatObjectApi>> {
            override fun onResponse(call: Call<List<CatObjectApi>>, response: Response<List<CatObjectApi>>) {
                val imageUrlString = response.body()?.get(0)?.url
                printImageWithGlide(imageUrlString)

            }

            override fun onFailure(call: Call<List<CatObjectApi>>, t: Throwable) {
                printImageWithGlide(R.drawable.error_cat)
            }
        })*/
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