package org.jsantamariap.eh_ho.posts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.Post
import org.jsantamariap.eh_ho.inflate

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostHolder>() {

    // MARK: - Properties

    private val posts = mutableListOf<Post>()

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = parent.inflate(R.layout.item_post)
        return PostHolder(view)
    }

    // ahora toca conectar el modelo de datos con el ViewHolder
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.post = post
    }

    // MARK: - Public functions

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)

        // de momento se ve esta forma que es manual y además hace un refresh general
        // hay sistemas automáticos que de momento no se ve
        // provoca que el adaptar recargue y se vuelva a llamar
        // getiItemCount, onCreateViewHolder y onBindViewHolder
        notifyDataSetChanged()
    }

    // MARK: - inner class

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var post: Post? = null
            set(value) {
                field = value

                field?.let {
                    itemView.labelAuthor.text = field?.author
                    itemView.labelContent.text = it.content
                    itemView.labelDate.text = it.date.toString()
                }
            }
        }
}