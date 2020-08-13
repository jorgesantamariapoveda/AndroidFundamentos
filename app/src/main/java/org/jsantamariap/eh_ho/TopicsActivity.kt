package org.jsantamariap.eh_ho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
// con el siguiente import se evita el uso de findViewById
import kotlinx.android.synthetic.main.activity_topics.*

class TopicsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        // es más limpio el uso de debugger
        // Log.d(TopicsActivity::class.simpleName, TopicsRepo.topics.toString())

        // modo antiguo findViewById, incómodo x.ej un formulario
        // muchas llamadas
        /*
        val list: RecyclerView = findViewById(R.id.list_topics)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
         */
        listTopics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }
}