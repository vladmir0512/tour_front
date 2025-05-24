package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RouteUserAdapter(private val routes: List<Route>, param: (Any) -> Unit) :
    RecyclerView.Adapter<RouteUserAdapter.RouteViewHolder>() {
    class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routeTitle: TextView = view.findViewById(R.id.tvRouteUserTitle)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingUsersBar)
        val commentText: TextView =
            view.findViewById(R.id.tvUserComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]
        holder.routeTitle.text = route.name
        holder.ratingBar.rating = route.rating.toFloat()
        holder.ratingBar.isEnabled = false
        if (route.comment.isNotEmpty()) {
            holder.commentText.text = "Комментарий: ${route.comment}"
            holder.commentText.visibility = View.VISIBLE
        } else {
            holder.commentText.visibility = View.GONE
        }
        holder.routeTitle.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MakeRouteActivity::class.java).apply {
                Log.d("RouteUserAdapter", "Coords: ${route.coords}")
                val coords = route.coords.split(",", limit = 4)
                val loc1 = "${coords[0]},${coords[1]}"
                val loc2 = "${coords[2]},${coords[3]}"
                Log.d("RouteUserAdapter", "loc1: ${loc1}, loc2: ${loc2}")
                putExtra("selfLocation", loc1)
                putExtra("finLocation", loc2)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = routes.size
}
