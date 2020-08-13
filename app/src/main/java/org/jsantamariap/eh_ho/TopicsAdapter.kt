package org.jsantamariap.eh_ho

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TopicsAdapter : RecyclerView.Adapter<TopicsAdapter.TopicHolder>() {

    private val topics = mutableListOf<Topic>()

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        //return TopicsRepo.createDummyTopics().count()
        return TopicHolder(parent)
    }

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
    }

    // los objetos TopicHolder viven dentro de TopicsHolder y comparten el context
    inner class TopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}