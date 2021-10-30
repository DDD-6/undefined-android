package com.editor.appcha.ui.feed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.editor.appcha.ui.component.AppText

@Composable
fun FeedScreen() {
    val viewModel = hiltViewModel<FeedViewModel>()

    // TODO: UI 구현
    val items = List(100) { 0 }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(items) { idx, item ->
            AppText(text = "$idx")
        }
    }
}