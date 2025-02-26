package com.bazaroff_alexey.newroutes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteAdapter(private val routes: List<Route>, private val onCommentClick: (Route) -> Unit) :
    RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routeTitle: TextView = view.findViewById(R.id.tvRouteTitle)
        val commentButton: Button = view.findViewById(R.id.btnAddComment)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]
        holder.routeTitle.text = route.name
        holder.ratingBar.rating = route.rating.toFloat()

        holder.commentButton.setOnClickListener {
            onCommentClick(route)
        }

        // Обработка изменения рейтинга
        holder.ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
            updateRouteRating(route.id, newRating.toInt())
        }
    }

    private fun updateRouteRating(routeId: Int, newRating: Int) {
        val request = RatingRequest(routeId, newRating)
        val call = RetrofitAPI.instance.updateRouteRating(request)

        call.enqueue(object : Callback<RatingResponse> {
            override fun onResponse(
                call: Call<RatingResponse>,
                response: Response<RatingResponse>
            ) {
                if (response.isSuccessful) {
                    println("Рейтинг обновлен успешно!")
                } else {
                    println("Ошибка обновления рейтинга")
                }
            }

            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                println("Ошибка сети: ${t.message}")
            }
        })
    }


    override fun getItemCount(): Int = routes.size
}
