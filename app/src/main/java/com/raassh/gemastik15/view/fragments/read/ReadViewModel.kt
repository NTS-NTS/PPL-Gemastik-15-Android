package com.raassh.gemastik15.view.fragments.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReadRepository

class ReadViewModel(private val readRepository: ReadRepository) : ViewModel() {
    val articles = readRepository.getArticles(200).asLiveData()
    val news = readRepository.getNews(200).asLiveData()
    val guidelines = readRepository.getGuidelines(200).asLiveData()
}