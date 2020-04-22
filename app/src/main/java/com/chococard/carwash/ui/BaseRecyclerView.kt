package com.chococard.carwash.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<E>(
    private val layout: Int,
    private val bindView: (view: View, entity: E) -> Unit
) : RecyclerView.Adapter<BaseRecyclerView<E>.BaseHolder>() {

    private var list = ArrayList<E>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder =
        BaseHolder(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) =
        bindView.invoke(holder.itemView, list[position])

    fun setList(list: List<E>) {
        this.list = list as ArrayList<E>
        notifyDataSetChanged()
    }

    inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
