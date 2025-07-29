package com.example.desafio2_task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2_task.databinding.ItemTareaBinding
import com.example.desafio2_task.model.Tarea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Aquí empieza la magia del Adapter. Este es el intermediario entre tu lista de Tareas y el RecyclerView.
class TareaAdapter(
    // Lista de tareas (puede crecer, encogerse, o caerse de sueño)
    private val tareas: MutableList<Tarea>,
    // Función para cuando marcas/desmarcas la tarea (ej: para actualizar el estado)
    private val onChecked: (Tarea) -> Unit,
    // Función para cuando quieres editar una tarea (mantén apretado el item)
    private val onEdit: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    // El ViewHolder: un héroe que agarra cada item y lo pone bonito.
    inner class ViewHolder(private val bind: ItemTareaBinding) :
        RecyclerView.ViewHolder(bind.root) {

        // El método para ligar los datos con el diseño visual (layout)
        fun bind(tarea: Tarea) = with(bind) {
            tvTitulo.text = tarea.titulo
            cbDone.isChecked = tarea.completada

            cbDone.setOnCheckedChangeListener(null)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                tarea.completada = isChecked
                onChecked(tarea)
                // Mostrar Toast SOLO al clickear el checkbox
                val mensaje = if (isChecked) "Tarea completada" else "Tarea pendiente"
                Toast.makeText(
                    root.context,
                    mensaje,
                    Toast.LENGTH_SHORT
                ).show()
            }


            // Marca el checkbox si la tarea está completada, lo desmarca si no
            cbDone.isChecked = tarea.completada

            // Creamos un formato de fecha elegante (día/mes/año hora:minutos)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            // Mostramos la fecha de creación de la tarea ya formateada (no pongas solo “Fecha” o te retamos)
            tvFecha.text = sdf.format(Date(tarea.fechaCreacion))

            // Previene bugs raros al reciclar items (si no entiendes, no te preocupes: así funciona bien)
            cbDone.setOnCheckedChangeListener(null)
            cbDone.setOnCheckedChangeListener { _, _ ->
                tarea.completada = cbDone.isChecked
                onChecked(tarea)
            }

            // Si mantienes apretado, se dispara la edición (útil para renombrar tareas o ponerle emojis)
            root.setOnLongClickListener {
                onEdit(tarea)
                true
            }
        }
    }

    // Crea el ViewHolder (inflador oficial de layouts). Aquí no toques mucho.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTareaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    // Cada vez que hay que mostrar un item, este método se encarga de pasarle la tarea correcta al ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(tareas[position])

    // Devuelve cuántas tareas hay. ¡No pongas un número fijo o explota!
    override fun getItemCount() = tareas.size
}