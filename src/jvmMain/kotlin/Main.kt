// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import db.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.ToDoAdder
import ui.ToDoAdderState
import ui.ToDoList

fun main() = runBlocking {
    application {
        val db = DB()

        val dbJob = launch(Dispatchers.IO) {
            db.startWriteJob()
        }

        Window(
            onCloseRequest = {
                db.endWrite()
                runBlocking { dbJob.join() }
                exitApplication()
            },
            title = "TodoApp Lite",
            state = rememberWindowState(
                width = 250.dp, height = 400.dp,
                position = WindowPosition(alignment = Alignment.Center),
            ),
        ) {
            MaterialTheme(colors = darkColors()) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Box(modifier = Modifier.weight(1.0f)) {
                            ToDoList(db)
                        }
                        ToDoAdder(ToDoAdderState(db))
                    }
                }

            }
        }
    }
}
