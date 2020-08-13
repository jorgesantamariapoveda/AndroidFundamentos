package org.jsantamariap.eh_ho

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TopicsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        // es más limpio el uso de debugger
        Log.d(TopicsActivity::class.simpleName, TopicsRepo.topics.toString())
    }
}