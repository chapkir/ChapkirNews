package com.example.chapkirnews.presentation.screens.news_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chapkirnews.R
import com.example.chapkirnews.domain.model.Article

@Composable
fun NewsDetailScreen(
    article: Article,
    onClose: () -> Unit
) {

    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 14.dp, bottom = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = article.publishedAt,
                color = Color.Gray,
                fontSize = 14.sp
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .padding(end = 14.dp)
                    .size(32.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id =
                        if (false) R.drawable.ic_bookmark_filled
                        else R.drawable.ic_bookmark
                    ),
                    contentDescription = "like",
                    modifier = Modifier.size(25.dp),
                    tint =
                    if (false) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Text(
                    text = article.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.imageUrl)
                        .crossfade(300)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .aspectRatio(16 / 9f)
                )
            }
            item {
                Text(
                    text = article.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
            }
            item {
                Text(
                    text = "Перейти к статье",
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { uriHandler.openUri(article.url) },
                    color = Color(0xFF1E88E5),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline
                )
            }
            item {
                Text(
                    text = "Автор статьи: ${article.author}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            item {
                HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Похожие новости:",
                    color = Color.Gray,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}