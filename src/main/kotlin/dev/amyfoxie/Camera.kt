package dev.amyfoxie

import org.joml.Matrix4f

class Camera(
    var nearPlane: Float = 0.1f,
    var farPlane: Float = 1000f,
    var fov: Float = 90.0f
) : Entity() {
    fun perspective(aspect: Float): Matrix4f = Matrix4f().perspective(fov, aspect, nearPlane, farPlane)
}
