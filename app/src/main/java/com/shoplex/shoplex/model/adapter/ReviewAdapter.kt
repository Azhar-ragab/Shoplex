
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.Review


class ReviewAdapter (private val review: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

   // var chatHead = listOf<ChatHead>()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image : ImageView
        val customerName : TextView
        var rating : RatingBar
        val date :TextView
        val comment : TextView
init{
  image = view.findViewById(R.id.imgHead)
    customerName  = view.findViewById(R.id.tv_customer_name)
    rating  = view.findViewById(R.id.ratingBar)
    date  = view.findViewById(R.id.tv_date)
    comment = view.findViewById(R.id.tv_comment)
}
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reveiw_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = review[position]
        Glide.with(viewHolder.itemView.context).load(item.customerImageUrl).into(viewHolder.image)
        viewHolder.customerName.text = item.customerName
        viewHolder.rating = item.rate
        viewHolder.date.text = item.date.toString()
        viewHolder.comment.text = item.comment.toString()

    }

    override fun getItemCount() = review.size


}