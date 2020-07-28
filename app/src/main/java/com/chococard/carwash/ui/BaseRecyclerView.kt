package com.chococard.carwash.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<T : Any> : RecyclerView.Adapter<BaseRecyclerView<T>.BaseHolder>() {

    private val list by lazy { mutableListOf<T>() }
    var onClick: ((T) -> Unit)? = null
    var onLongClick: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder =
        BaseHolder(
            LayoutInflater.from(parent.context)
                .inflate(getLayout(), parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView onBindViewHolder list[position]
    }

    @LayoutRes
    abstract fun getLayout(): Int

    abstract infix fun View.onBindViewHolder(data: T)

    fun setList(list: List<T>?) {
        if (list != null) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val entity = list[adapterPosition]
                onClick?.invoke(entity)
            }

            itemView.setOnLongClickListener {
                val entity = list[adapterPosition]
                onLongClick?.invoke(entity)
                true
            }
        }

    }

}
