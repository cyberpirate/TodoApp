package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import db.DB
import db.ToDoEntry

class ToDoAdderState(private val db: DB) {
    var text: String by mutableStateOf("")

    fun addToDo() {
        db.addEntry(ToDoEntry(text))
        text = ""
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToDoAdder(state: ToDoAdderState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = state.text,
            modifier = Modifier
                .weight(weight = 1F)
                .onKeyUp(key = Key.Enter, action = { state.addToDo() }),
            onValueChange = { state.text = it },
            label = { Text(text = "Add a todo") }
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { state.addToDo() }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}