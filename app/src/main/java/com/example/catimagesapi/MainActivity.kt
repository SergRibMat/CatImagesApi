package com.example.catimagesapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catApiMethod()

        findViewById<Button>(R.id.next_image_btn).setOnClickListener {
            catApiMethod()
        }

    }

    fun catApiMethod(){
        retrofitService.getProperties().enqueue( object: Callback<List<CatObjectApi>> {
            override fun onResponse(call: Call<List<CatObjectApi>>, response: Response<List<CatObjectApi>>) {
                val imageUrlString = response.body()?.get(0)?.url
                printImageWithGlide(imageUrlString)

            }

            override fun onFailure(call: Call<List<CatObjectApi>>, t: Throwable) {
                val response  = "Failure: " + t.message
            }
        })
    }



    private fun printImageWithGlide(data: String?) {
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
}