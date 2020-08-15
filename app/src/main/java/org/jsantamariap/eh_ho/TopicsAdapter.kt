package org.jsantamariap.eh_ho

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import org.jsantamariap.eh_ho.login.inflate

class TopicsAdapter(val topicClickListener: ((Topic) -> Unit)? = null) :
    RecyclerView.Adapter<TopicsAdapter.TopicHolder>() {

    private val topics = mutableListOf<Topic>()

    // View representa la vista sobre la que se le hace el click
    private val listener: ((View) -> Unit) = {
        val topic: Topic = it.tag as Topic

        // me quito responsabilidad, se ejecut aun código que se pasa
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
        // val context = parent.context
        // primera aproximación
        //val view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false)
        // o directamente
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        // mejora con el uso de extension
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

        // también le podemos asignar un click
        /*
        holder.itemView.setOnClickListener {
            Log.d(this::class.java.canonicalName, "OnClick")
            // la responsabilidad de que hacer cuando se pulsa se la vamos
            // a pasar al activity (es como un controlador) y por lo tanto
            // en este caso el TopicsActivity
            // para ello se crea una propiedad privada en esta misma clase TopicsAdapter
            // que será un listener con una lambda
        }
         */

        // asignación listener privado
        // además se pasa como parámetro de tipo propiedad a la clase TopicsAdapter
        // la función lamda
        holder.itemView.setOnClickListener(listener)
    }

    // función que informará al adapter cuando hay cambios en el conjunto de datos a mostrar
    fun setTopics(topics: List<Topic>) {
        this.topics.clear()
        this.topics.addAll(topics)

        // de momento se ve esta forma que es manual y además hace un refresh general
        // hay sistemas automáticos que de momento no se ve
        notifyDataSetChanged() // provoca que el adaptar recargue y se vuelva a llamar
        // getiItemCount, onCreateViewHolder y onBindViewHolder
    }

    // los objetos TopicHolder viven dentro de TopicsAdapter y comparten el context
    inner class TopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var topic: Topic? = null
            set(value) {
                field = value
                // antes de hacer el import kotlinx.android.synthetic.main.item_topic.view.*
                // itemView.findViewById<TextView>(R.id.label_topic).setText(field?.title)
                // gracias al import
                itemView.labelTopic.text = field?.title

                // esto es para el tema del onClick mediante el listener
                // en la propiedad tag es Any! con la ventaja que ello conlleva
                itemView.tag = field
            }
    }
}