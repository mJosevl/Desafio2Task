package com.example.desafio2_task

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2_task.adapter.TareaAdapter
import com.example.desafio2_task.databinding.ActivityMainBinding
import com.example.desafio2_task.model.Tarea
import com.example.desafio2_task.utils.SwipeToDeleteCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    // ---------- ViewBinding ----------
    private lateinit var binding: ActivityMainBinding

    // ---------- Estado ----------
    private val listaTareas = mutableListOf<Tarea>()
    private lateinit var adapter: TareaAdapter

    // ---------- Persistencia ----------
    private val prefs by lazy { getSharedPreferences("todo_prefs", MODE_PRIVATE) }
    private val gson = Gson()
    private val listType = object : TypeToken<MutableList<Tarea>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarTareas()                       // ← recupera si había datos

        // --- Adapter con callbacks ---
        adapter = TareaAdapter(
            listaTareas,
            onChecked = {
                actualizarContador()
                guardarTareas()
            },
            onEdit = ::mostrarDialogoEdicion
        )

        // --- RecyclerView ---
        binding.rvTareas.layoutManager = LinearLayoutManager(this)
        binding.rvTareas.adapter = adapter

        // --- Swipe-to-delete ---
        ItemTouchHelper(
            SwipeToDeleteCallback(this) { pos ->
                listaTareas.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                actualizarContador()
                guardarTareas()
            }
        ).attachToRecyclerView(binding.rvTareas)

        // --- Botón Agregar ---
        binding.btnAgregar.setOnClickListener {
            val titulo = binding.etNuevaTarea.text.toString().trim()
            if (titulo.isNotEmpty()) {
                listaTareas.add(Tarea(titulo = titulo))
                adapter.notifyItemInserted(listaTareas.lastIndex)
                binding.etNuevaTarea.text?.clear()
                actualizarContador()
                guardarTareas()
            }
        }

        actualizarContador()                 // pinta el contador al inicio
    }

    // ---------- Contador ----------
    private fun actualizarContador() {
        val pendientes = listaTareas.count { !it.completada }
        binding.tvPendientes.text = "$pendientes pendientes"
    }

    // ---------- Diálogo de edición ----------
    private fun mostrarDialogoEdicion(tarea: Tarea) {
        val input = EditText(this).apply {
            setText(tarea.titulo)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            setSelection(text.length)
        }
        MaterialAlertDialogBuilder(this)
            .setTitle("Editar tarea")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                tarea.titulo = input.text.toString()
                adapter.notifyDataSetChanged()
                guardarTareas()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ---------- Guardar / Cargar ----------
    private fun guardarTareas() {
        val json = gson.toJson(listaTareas)
        prefs.edit().putString("data", json).apply()
    }

    private fun cargarTareas() {
        val json = prefs.getString("data", null) ?: return
        listaTareas.addAll(gson.fromJson(json, listType))
    }


}
