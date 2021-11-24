package dev.amyfoxie

import org.lwjgl.opengl.GL45.*

class VertexArrayObject : AutoCloseable {
    private val vao = glGenVertexArrays()

    fun attributePointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, pointer: Long) =
        glVertexAttribPointer(index, size, type, normalized, stride, pointer)

    fun enableAttributeArray(index: Int) = glEnableVertexAttribArray(index)

    fun bind() = glBindVertexArray(vao)

    override fun close() = glDeleteVertexArrays(vao)
}
