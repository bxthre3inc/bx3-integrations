package com.bxthre3.design.patterns.virtualization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * BX3VirtualizedList — High-performance list with windowing
 * 
 * Only renders visible items + buffer, maintaining 60fps
 * with large datasets (10,000+ items).
 * 
 * @param items Complete dataset
 * @param visibleItemHeight Fixed height for estimation
 * @param bufferSize Items to render above/below viewport
 * @param renderItem Composable for each item
 * @param onLoadMore Callback when reaching end (pagination)
 */
@Composable
fun <T> BX3VirtualizedList(
    items: List<T>,
    key: (T) -> Any,
    visibleItemHeight: Int,
    bufferSize: Int = 5,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    onLoadMore: (() -> Unit)? = null,
    renderItem: @Composable (item: T, index: Int, isVisible: Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    val visibleRange = remember { mutableStateOf(IntRange(0, 0)) }
    val isLoadingMore = remember { mutableStateOf(false) }
    
    // Track visible items for performance optimization
    LaunchedEffect(listState) {
        snapshotFlow { 
            listState.firstVisibleItemIndex..listState.lastVisibleItemIndex 
        }
        .distinctUntilChanged()
        .collect { range ->
            // Expand by buffer
            visibleRange.value = IntRange(
                (range.first - bufferSize).coerceAtLeast(0),
                (range.last + bufferSize).coerceAtMost(items.size - 1)
            )
        }
    }
    
    // Trigger load more
    LaunchedEffect(items, onLoadMore) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .filter { index ->
                // Trigger when within buffer of end
                index + (visibleRange.value.last - visibleRange.value.first) >= 
                    items.size - bufferSize
            }
            .collect {
                if (!isLoadingMore.value && onLoadMore != null) {
                    isLoadingMore.value = true
                    onLoadMore()
                    isLoadingMore.value = false
                }
            }
    }
    
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(
            items = items,
            key = { index, item -> key(item) }
        ) { index, item ->
            val isVisible = index in visibleRange.value
            
            // Render actual content only if visible (or near visible)
            // Otherwise render placeholder for height estimation
            if (isVisible) {
                renderItem(item, index, true)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(visibleItemHeight.dp)
                )
            }
        }
        
        // Loading indicator at bottom
        if (isLoadingMore.value) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}