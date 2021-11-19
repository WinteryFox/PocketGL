package dev.amyfoxie

import org.joml.Vector3f

data class Material(
    val shader: Shader,
    val color: Vector3f
)
