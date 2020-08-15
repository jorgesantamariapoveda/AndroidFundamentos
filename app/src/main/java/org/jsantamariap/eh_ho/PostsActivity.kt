package org.jsantamariap.eh_ho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_posts.*

const val EXTRA_TOPIC_ID = "TOPIC_ID"

class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        // todas las activitys tienen la propiedad intent
        // this.intent
        Log.d(this::class.java.canonicalName, intent.getStringExtra(EXTRA_TOPIC_ID))

        // operador elvis (por lo del tupé ;)
        val topicId: String = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
        val topic: Topic? = TopicsRepo.getTopic(topicId)

        // opcion1
        // labelTitle.text = topic?.title

        // opcion2
        topic?.let {
            labelTitle.text = it.title
        }
    }
}