package com.example.androidarticlessample.presentation.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidarticlessample.presentation.model.ArticlePreview
import com.example.androidarticlessample.R
import com.example.androidarticlessample.presentation.theme.ArticlesTheme

@Composable
fun ArticleItem(
    articlePreview: ArticlePreview,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .testTag("ArticleItem")
            .height(130.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        if (!articlePreview.thumbnailUrl.isNullOrBlank()) {
            AsyncImage(
                model = articlePreview.thumbnailUrl,
                contentDescription = null, //TODO content description
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null, //TODO content description
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = articlePreview.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = articlePreview.byline ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(5.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null, //TODO content description
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(14.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = articlePreview.publishedDate,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null, //TODO content description
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ArticleItemPreview() {
    ArticlesTheme {
        ArticleItem(
            ArticlePreview(
                id = 0,
                title = stringResource(R.string.lorem_ipsum),
                byline = stringResource(R.string.lorem_ipsum),
                thumbnailUrl = "",
                publishedDate = "2017-06-28"
            )
        )
    }
}