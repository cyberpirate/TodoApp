package db

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

@Serializable
data class ToDoEntry(
    val name: String,
    val checked: Boolean = false,
    val done: Boolean = false,
    val time: Long = System.currentTimeMillis()
)

class DB {
    private val home = File(System.getProperty("user.home"))
    private val dbFile = File(home, ".todo.json")
    private val ex = Executors.newSingleThreadExecutor()

    companion object {
        private fun read(dbFile: File): List<ToDoEntry> {
            return try {
                Json.decodeFromString(dbFile.readText())
            } catch(_: IOException) {
                emptyList()
            } catch(_: SerializationException) {
                emptyList()
            }
        }

        private fun write(dbFile: File, data: List<ToDoEntry>) {
            try {
                dbFile.writeText(Json.encodeToString(data))
            } catch(_: IOException) { } catch(_: SerializationException) { }
        }
    }

    private val listUpdate = Channel<List<ToDoEntry>>(CONFLATED)

    suspend fun startWriteJob() {
        var running = true
        while(running) {
            try {
                val list = listUpdate.receive()
                write(dbFile, list)
            } catch(_: ClosedReceiveChannelException) {
                running = false
            }
        }
    }

    fun endWrite() {
        listUpdate.close()
    }

    var todoList: List<ToDoEntry> by mutableStateOf(read(dbFile))
        private set

    fun update(block: MutableList<ToDoEntry>.() -> Unit) {
        val newList = todoList.toMutableList().apply(block)
        todoList = newList

//        ex.submit({
//            write(dbFile, newList)
//        })
        runBlocking {
            listUpdate.send(newList)
        }
    }

    fun addEntry(e: ToDoEntry) {
        update({
            add(0, e)
        })
    }

    fun removeEntry(i: Int) {
        update({
            removeAt(i)
        })
    }

    fun editEntry(i: Int, block: ToDoEntry.() -> ToDoEntry) {
        update({
            this[i] = this[i].block()
        })
    }
}