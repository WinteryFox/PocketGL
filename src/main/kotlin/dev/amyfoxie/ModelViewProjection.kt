package dev.amyfoxie

import org.joml.Matrix4f

data class ModelViewProjection(
    val model: Matrix4f,
    val view: Matrix4f,
    val projection: Matrix4f
)
