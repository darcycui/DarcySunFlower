package com.darcy.message.lib_ui.paging.repoisitory

import android.os.Build
import androidx.annotation.RequiresApi
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_ui.paging.entity.Article
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()

/**
 * Repository class that mimics fetching [Article] instances from an asynchronous source.
 */
class ArticleRepository {
    /**
     * Exposed singular stream of [Article] instances
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getArticleStream(): Flow<List<Article>> = flowOf(
        (0..500).map { number ->
            Article(
                id = number,
                title = "Article $number",
                description = "This describes article $number",
                created = firstArticleCreatedTime.minusDays(number.toLong())
            )
        }
    )


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRemoteArticles(range: IntRange): List<Article> {
        delay(1_000)
        logI(message = "getRemoteArticles: $range")
        return range.map { number ->
            Article(
                // Generate consecutive increasing numbers as the article id
                id = number,
                title = "Article $number",
                description = "This describes article $number",
                created = firstArticleCreatedTime.minusDays(
                    number.toLong()
                )
            )
        }
    }
}