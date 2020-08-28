package org.jsantamariap.eh_ho.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_posts.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.Topic
import org.jsantamariap.eh_ho.data.TopicsRepo

const val EXTRA_TOPIC_ID = "TOPIC_ID"

// Todas las activity tienen la propiedad intent

class PostsActivity : AppCompatActivity() {

    // MARK: - Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        // ?: significa que si es null, devuelve lo que hay a su derecha
        val topicId: String = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
        val topic: Topic? = TopicsRepo.getTopic(topicId)

        TopicsRepo.getPosts(topicId, this.applicationContext)

        topic?.let {
            labelTitle.text = it.title
        }
    }
}