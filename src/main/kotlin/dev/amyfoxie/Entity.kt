package dev.amyfoxie

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

sealed class Entity {
    private val position = Vector3f()
    private val rotation = Quaternionf()
    private val scale = Vector3f(1f, 1f, 1f)

    val matrix: Matrix4f get() = Matrix4f().translationRotateScale(position, rotation, scale)

    fun setPosition(x: Float, y: Float, z: Float) {
        setPosition(Vector3f(x, y, z))
    }

    fun setPosition(new: Vector3f) {
        position.set(new)
    }

    fun translate(x: Float, y: Float, z: Float) {
        translate(Vector3f(x, y, z))
    }

    fun translate(translation: Vector3f) {
        position.add(translation)
    }

    fun rotateDegrees(x: Float, y: Float, z: Float) {
        rotateRadians(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z))
    }

    fun rotateRadians(x: Float, y: Float, z: Float) {
        rotation.rotateXYZ(x, y, z)
    }

    fun setScale(value: Float) {
        setScale(value, value, value)
    }

    fun setScale(x: Float, y: Float, z: Float) {
        scale.set(x, y, z)
    }

    fun scale(factor: Float) {
        scale(factor, factor, factor)
    }

    fun scale(factorX: Float, factorY: Float, factorZ: Float) {
        scale.mul(factorX, factorY, factorZ)
    }
}
