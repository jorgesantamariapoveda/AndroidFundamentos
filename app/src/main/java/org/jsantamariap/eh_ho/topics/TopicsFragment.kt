package org.jsantamariap.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_topics.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.Topic
import org.jsantamariap.eh_ho.TopicsRepo
import org.jsantamariap.eh_ho.login.inflate
import java.lang.IllegalArgumentException

class TopicsFragment : Fragment() {

    private var topicsInteractionListener: TopicsInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TopicsInteractionListener)
            topicsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${TopicsInteractionListener::class.java.canonicalName}")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return container?.inflate(R.layout.fragment_topics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // creación adapter y asignacion a listTopics
        val adapter = TopicsAdapter {
            Log.d(
                TopicsActivity::class.java.canonicalName,
                it.title
            )

            // Pasando datos entre actividades
            //goToPosts(it)
            this.topicsInteractionListener?.onShowPosts(it)
        }
        adapter.setTopics(TopicsRepo.topics)

        listTopics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // esto añade una linea tras cada topic
        listTopics.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        listTopics.adapter = adapter

        buttonCreateTopic.setOnClickListener {
            this.topicsInteractionListener?.onCreateTopic()
        }
    }

    override fun onDetach() {
        super.onDetach()

        topicsInteractionListener = null
    }

    interface TopicsInteractionListener {
        fun onCreateTopic()
        fun onShowPosts(topic: Topic)
    }
}