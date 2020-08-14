package org.jsantamariap.eh_ho

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        // creación adapter y asignacion a listTopics
        val adapter = TopicsAdapter {
            Log.d(TopicsActivity::class.java.canonicalName, it.title)

            // Pasando datos entre actividades
            goToPosts(it)
        }
        adapter.setTopics(TopicsRepo.topics)

        listTopics.adapter = adapter
    }

    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        // pasar datos entre actividades
        // lástima que no se puedan pasar las referencias u objetos sino
        // que son datos primitivos
        // primera forma, hardcoreado, nada recomendado
        //intent.putExtra("TOPIC_ID", topic.id)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        startActivity(intent)
    }
}