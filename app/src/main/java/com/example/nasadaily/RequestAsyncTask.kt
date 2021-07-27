package com.example.nasadaily

import android.os.AsyncTask
import retrofit2.Call

class RequestAsyncTask: AsyncTask<Call<ResponseModel>, Void, Pair<ResponseModel?, Throwable?>>() {

    override fun doInBackground(vararg params: Call<ResponseModel>): Pair<ResponseModel?, Throwable?> {
        val response = params[0].execute()
        return if (response.isSuccessful) {
            if (response.isSuccessful && response.body() != null) {
                response.body() to null
            } else {
                null to Throwable("Server response is null")
            }
        } else {
            null to Throwable(response.errorBody().toString())
        }
    }
}