package com.example.desafio2_task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2_task.databinding.ItemTareaBinding
import com.example.desafio2_task.model.Tarea

class TareaAdapter(
    private val tareas: MutableList<Tarea>,
    private val onChecked: (Tarea) -> Unit,
    private val onEdit: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    inner class ViewHolder(private val bind: ItemTareaBinding) :
        RecyclerView.ViewHolder(bind.root) {

        fun bind(tarea: Tarea) = with(bind) {
            tvTitulo.text = tarea.titulo
            cbDone.isChecked = tarea.completada

            cbDone.setOnCheckedChangeListener(null)
            cbDone.setOnCheckedChangeListener { _, _ ->
                tarea.completada = cbDone.isChecked
                onChecked(tarea)
            }

            root.setOnLongClickListener {
                onEdit(tarea)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTareaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(tareas[position])

    override fun getItemCount() = tareas.size
}
