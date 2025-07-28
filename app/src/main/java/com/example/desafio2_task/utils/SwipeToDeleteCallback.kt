package com.example.desafio2_task.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2_task.R

class SwipeToDeleteCallback(
    context: Context,                       // ← ① recibe un Context
    private val onDelete: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val bg = ColorDrawable(         // ② usa ese Context
        ContextCompat.getColor(context, R.color.red)
    )

    override fun onMove(
        rv: RecyclerView, vh: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) =
        onDelete(vh.bindingAdapterPosition)

    override fun onChildDraw(
        c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder,
        dX: Float, dY: Float, state: Int, active: Boolean
    ) {
        bg.setBounds(
            if (dX > 0) vh.itemView.left else vh.itemView.right + dX.toInt(),
            vh.itemView.top,
            if (dX > 0) vh.itemView.left + dX.toInt() else vh.itemView.right,
            vh.itemView.bottom
        )
        bg.draw(c)
        super.onChildDraw(c, rv, vh, dX, dY, state, active)
    }
}

