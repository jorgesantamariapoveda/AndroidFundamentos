package org.jsantamariap.eh_ho.topics

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.Topic
import org.jsantamariap.eh_ho.inflate
import java.util.*

class TopicsAdapter(val topicClickListener: ((Topic) -> Unit)? = null) :
    RecyclerView.Adapter<TopicsAdapter.TopicHolder>() {

    // MARK: - Properties

    private val topics = mutableListOf<Topic>()

    // View representa la vista sobre la que se le hace el click
    private val listener: ((View) -> Unit) = {
        val topic: Topic = it.tag as Topic

        // me quito responsabilidad, se ejecuta un código que se pasa
        // desde fuera
        topicClickListener?.invoke(topic)

        // segunda opción en caso de fallar, es muy raro en este caso
        // pero sirve para ver más sintaxis de Kotlin is/as
        /*
        if (it.tag is Topic) {
            topicClickListener?.invoke(it.tag as Topic)
        } else {
            throw IllegalArgumentException("Topic item view has not a Topic Data as a tag")
        }
         */
    }

    override fun getItemCount(): Int {
        Log.d(this::class.java.canonicalName, "fun getItemCount")
        return topics.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        Log.d(this::class.java.canonicalName, "fun onCreateViewHolder")

        // el parent realmente es la lista (el recyclerview) y como vista que es tiene
        // acceso al contexto
        val view = parent.inflate(R.layout.item_topic)

        return TopicHolder(view)
    }

    // ahora toca conectar el modelo de datos con el ViewHolder
    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        Log.d(this::class.java.canonicalName, "fun onBindViewHolder")

        // el holder que se pasa es la plantilla y position es la posición que
        // ocupa, ahora será rellenar esa plantilla con los datos que hay en topics
        // de esa posicion
        val topic = topics[position]
        holder.topic = topic

        holder.itemView.setOnClickListener(listener)
    }

    // MARK: - Public functions

    fun setTopics(topics: List<Topic>) {
        this.topics.clear()
        this.topics.addAll(topics)

        // de momento se ve esta forma que es manual y además hace un refresh general
        // hay sistemas automáticos que de momento no se ve
        // provoca que el adaptar recargue y se vuelva a llamar
        // getiItemCount, onCreateViewHolder y onBindViewHolder
        notifyDataSetChanged()
    }

    // MARK: - inner class

    // los objetos TopicHolder viven dentro de TopicsAdapter y comparten el context
    inner class TopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var topic: Topic? = null
            set(value) {
                field = value
                // antes de hacer el import kotlinx.android.synthetic.main.item_topic.view.*
                // itemView.findViewById<TextView>(R.id.label_topic).setText(field?.title)
                itemView.labelTitle.text = field?.title

                // esto es para el tema del onClick mediante el listener
                // en la propiedad tag es Any! con la ventaja que ello conlleva
                itemView.tag = field

                field?.let {
                    itemView.labelPosts.text = it.posts.toString()
                    itemView.labelViews.text = it.views.toString()
                    setTimeOffSet(it.getTimeOffSet())
                }
            }

        private fun setTimeOffSet(timeOffSet: Topic.TimeOffSet) {
            // la evaluación de un when devuelve algo
            val quantityString = when (timeOffSet.unit) {
                Calendar.YEAR -> R.plurals.years
                Calendar.MONTH -> R.plurals.months
                Calendar.DAY_OF_MONTH -> R.plurals.days
                Calendar.HOUR -> R.plurals.hours
                else -> R.plurals.minutes
            }

            if (timeOffSet.amount == 0) {
                itemView.labelDate.text =
                    itemView.context.resources.getString(R.string.minutes_zero)
            } else {
                itemView.labelDate.text = itemView.context.resources.getQuantityString(
                    quantityString,
                    timeOffSet.amount,
                    timeOffSet.amount
                )
            }
        }
    }
}