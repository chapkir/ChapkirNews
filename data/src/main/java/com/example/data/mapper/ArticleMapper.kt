package com.example.data.mapper

import com.example.data.model.ArticleDto
import com.example.domain.model.Article
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatIsoDate(isoDate: String?): String {
    return try {
        val zdtUtc = ZonedDateTime.parse(isoDate)
        val moscowTime = zdtUtc.withZoneSameInstant(ZoneId.of("Europe/Moscow"))
        val formatter = DateTimeFormatter.ofPattern("HH:mm / dd.MM.yyyy", Locale.getDefault())
        moscowTime.format(formatter)
    } catch (e: Exception) {
        "Без даты"
    }
}

fun ArticleDto.toDomain(): Article {
    return Article(
        sourceName = this.source?.name.orEmpty(),
        author = this.author.orEmpty(),
        title = this.title.orEmpty(),
        description = this.description.orEmpty(),
        url = this.url.orEmpty(),
        imageUrl = this.urlToImage.orEmpty(),
        publishedAt = formatIsoDate(this.publishedAt),
        content = this.content.orEmpty()
    )
}