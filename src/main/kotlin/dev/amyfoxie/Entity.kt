package dev.amyfoxie

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

sealed class Entity {
    val position = Vector3f()
    val rotation = Quaternionf()
    val scale = Vector3f(1f, 1f, 1f)

    val matrix: Matrix4f get() = Matrix4f().translationRotateScale(position, rotation, scale)

    fun setPosition(x: Float, y: Float, z: Float) {
        position.set(x, y, z)
    }

    fun setPosition(new: Vector3f) {
        position.set(new)
    }

    fun translate(x: Float, y: Float, z: Float) {
        position.add(x, y, z)
    }

    fun translate(translation: Vector3f) {
        position.add(translation)
    }

    fun rotateRadians(x: Float, y: Float, z: Float) {
        rotation.rotateXYZ(x, y, z)
    }

    fun rotateDegrees(x: Float, y: Float, z: Float) {
        rotation.rotateXYZ(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z))
    }
}
