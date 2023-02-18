package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import db.DB
import db.ToDoEntry

class ToDoItemState(private val db: DB, private val i: Int) {
    val item get() = db.todoList[i]

    fun toggleChecked() {
        db.editEntry(i, { copy(checked = !checked) })
    }

    fun delete() {
        db.removeEntry(i)
    }
}

@Composable
fun ToDoItem(state: ToDoItemState) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier.height(32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(state.item.checked, onCheckedChange = { state.toggleChecked() })

            Text(
                text = state.item.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.error
            ) {
                IconButton(onClick = { state.delete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    }
}