package dev.amyfoxie

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

data class Camera(
    var position: Vector3f = Vector3f(),
    var rotation: Quaternionf = Quaternionf(),
    var nearPlane: Float = 0.1f,
    var farPlane: Float = 1000.0f,
    var fov: Float = 90.0f
) {
    val view: Matrix4f get() = Matrix4f().rotate(rotation).translate(position)

    fun project(aspect: Float): Matrix4f = Matrix4f().perspective(fov, aspect, nearPlane, farPlane)
    //val projection: Matrix4f get() = Matrix4f().perspective(fov, 300.0f / 300.0f, nearPlane, farPlane)
}
