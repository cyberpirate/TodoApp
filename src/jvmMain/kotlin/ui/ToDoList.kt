package ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import db.DB

@Composable
fun ToDoList(db: DB) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberLazyListState()

        LazyColumn(Modifier.fillMaxSize(), state) {
            items(db.todoList.size) { i ->
                ToDoItem(ToDoItemState(db, i))
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}