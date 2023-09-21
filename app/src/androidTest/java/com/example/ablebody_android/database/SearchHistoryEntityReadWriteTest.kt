package com.example.ablebody_android.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ablebody_android.database.di.DaoModule
import com.example.ablebody_android.database.di.DatabaseModule
import com.example.ablebody_android.database.model.SearchHistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SearchHistoryEntityReadWriteTest {
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var db = DatabaseModule.provideDatabase(context)

    private var searchHistoryDao = DaoModule.providesSearchHistoryDao(db)

    private lateinit var job: Job

    @Before
    fun getAll() {
        job = CoroutineScope(Dispatchers.IO).launch {
            searchHistoryDao.getAll().collectLatest {
                Log.d("SearchHistoryEntityReadWriteTest", it.toString())
                cancel()
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        runBlocking { job.join() }
        db.close()
    }

    @Test
    fun insert() {
        searchHistoryDao.insert(
            searchHistoryEntity = SearchHistoryEntity(
                query = "나이키",
                queriedTime = System.currentTimeMillis()
            )
        )
    }

    @Test
    fun deleteAll() {
        searchHistoryDao.deleteAll()
    }

}