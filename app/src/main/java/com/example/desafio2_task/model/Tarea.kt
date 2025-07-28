package com.example.desafio2_task.model

import java.util.UUID


data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    var titulo: String,
    var completada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)

