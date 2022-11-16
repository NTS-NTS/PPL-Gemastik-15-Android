package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.utils.callApi

class ReadRepository(private val apiService: ApiService) {
    fun getNews(limit: Int?) = callApi {
        apiService.getNews(limit ?: 200).data
    }

    fun getArticles(limit: Int?) = callApi {
        apiService.getArticles(limit ?: 200).data
    }

    fun getGuidelines(limit: Int?) = callApi {
        apiService.getGuidelines(limit ?: 200).data
    }
}