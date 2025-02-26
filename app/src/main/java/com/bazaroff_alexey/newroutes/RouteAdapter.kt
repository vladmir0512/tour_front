package com.bazaroff_alexey.newroutes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            showCommentDialog(holder.itemView.context, route)
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

    private fun showCommentDialog(context: Context, route: Route) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Добавить комментарий")

        val input = EditText(context)
        input.hint = "Введите ваш комментарий"
        builder.setView(input)

        builder.setPositiveButton("Добавить") { _, _ ->
            val commentText = input.text.toString()
            if (commentText.isNotEmpty()) {
                addComment(route.id, commentText)
            } else {
                Toast.makeText(context, "Комментарий не может быть пустым", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun addComment(routeId: Int, comment: String) {
        val request = CommentRequest(routeId, comment)
        val call = RetrofitAPI.instance.addComment(request)

        call.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("HistoryRouteActivity", "Комментарий добавлен!")
                } else {
                    Log.d("HistoryRouteActivity", "Ошибка отправки комментария")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.d("HistoryRouteActivity", "Ошибка сети: ${t.message}")
            }
        })
    }

    override fun getItemCount(): Int = routes.size
}
