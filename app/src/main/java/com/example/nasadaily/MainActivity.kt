package com.example.nasadaily

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val retrofitHandler: RetrofitHandler = RetrofitHandler()
    private lateinit var responseData: Pair<ResponseModel?, Throwable?>

    companion object {
        private const val KEY: String = "N5FP2Nt951oviCWEJktUX1dZtYPDnxdFD0O8drnR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        responseData = retrofitHandler.sendServerRequest(Companion.KEY)
        renderData(responseData.first, responseData.second)
    }

    private fun renderData(responseModel: ResponseModel?, error: Throwable?) {
        if (responseModel == null || error != null) {
            Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
        } else {
            val url = responseModel.url
            if (url.isNullOrEmpty()) {
                Toast.makeText(this, "Photo url is empty", Toast.LENGTH_LONG).show()
            } else {
                if (url.contains("www.youtube.com")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } else {
                    image_view.load(url)
                    val explanation = responseModel.explanation
                    if (explanation.isNullOrEmpty()) {
                        Toast.makeText(this, "Empty description", Toast.LENGTH_LONG).show()
                    } else {
                        text_view.text = explanation
                    }
                }
            }
        }
    }
}