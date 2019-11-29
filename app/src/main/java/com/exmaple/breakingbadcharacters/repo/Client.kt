package com.exmaple.breakingbadcharacters.repo

import android.content.Context
import com.exmaple.breakingbadcharacters.utils.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient

object Client {

    fun getClient(context: Context): OkHttpClient {
        val cacheSize = (25 * 1024 * 1024).toLong() // 25 MB
        val myCache = Cache(context.cacheDir, cacheSize)
        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context))
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 3600).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .build()
    }
}