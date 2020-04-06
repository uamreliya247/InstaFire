package udit.com.instafireapp.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.post_layout.view.*
import udit.com.instafireapp.R
import udit.com.instafireapp.models.Posts

class PostAdapter(val context: Context, private val posts: List<Posts>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(posts: Posts) {
            itemView.tvUserName.text = posts.user?.username
            itemView.tvDescription.text = posts.description
            Glide.with(context)
                .load(posts.image)
                .into(itemView.ivPostImage)

            itemView.tvCreationTime.text = DateUtils.getRelativeTimeSpanString(posts.creationTime)
        }
    }
}