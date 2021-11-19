package dev.amyfoxie

import org.joml.Matrix4f

class Camera : Entity() {
    fun perspective(fov: Float, nearPlane: Float, farPlane: Float, aspect: Float): Matrix4f =
        Matrix4f().perspective(fov, aspect, nearPlane, farPlane)

    fun orthogonal(): Matrix4f = Matrix4f() //.ortho2D(150f, -150f, -150f, 150f) // TODO: I forgot how to use this :(
}
