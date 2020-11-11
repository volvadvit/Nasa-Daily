package com.example.nasadaily

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
    private final val KEY: String = "N5FP2Nt951oviCWEJktUX1dZtYPDnxdFD0O8drnR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendServerRequest()
    }

    private fun sendServerRequest() {
        retrofitImpl.getRequest().getNasaDaily(KEY).enqueue(object  :
            Callback<DataModel> {

            override fun onResponse(
                    call: Call<DataModel>,
                    response: Response<DataModel>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    renderData(response.body(), null)
                } else {
                    renderData(null, Throwable("Ответ от сервера пустой"))
                }
            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                renderData(null, t)
            }
        })
    }

    private fun renderData(dataModel: DataModel?, error: Throwable?) {
        if (dataModel == null || error != null) {
            Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show() //Ошибка
        } else {
            val url = dataModel.url
            if (url.isNullOrEmpty()) {
                Toast.makeText(this, "Ссылка на фото пусткая", Toast.LENGTH_LONG).show()
            } else {
                if (url.contains("www.youtube.com")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } else {
                    image_view.load(url)
                    val explanation = dataModel.explanation
                    if (explanation.isNullOrEmpty()) {
                        Toast.makeText(this, "Описание пустое", Toast.LENGTH_LONG).show()
                    } else {
                        text_view.text = explanation
                    }
                }
            }

        }
    }
}

data class DataModel (
    val explanation: String?,
    val url: String?
)

interface NasaDailyAPI {

    @GET("planetary/apod")
    fun getNasaDaily(@Query("api_key") apiKey: String): Call<DataModel>
}

class RetrofitImpl {

    fun getRequest(): NasaDailyAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
        return podRetrofit.create(NasaDailyAPI::class.java)
    }
}