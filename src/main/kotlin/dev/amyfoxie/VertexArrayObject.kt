package dev.amyfoxie

import org.lwjgl.opengl.GL30.*

class VertexArrayObject : AutoCloseable, Bindable() {
    private val vao = glGenVertexArrays()
    private val buffers = mutableMapOf<Int, Int>()

    override fun delegateBind() {
        glBindVertexArray(vao)
    }

    override fun delegateUnbind() {
        glBindVertexArray(0)
    }

    fun bufferData(target: Int, data: IntArray, usage: Int) {
        if (!isBound)
            throw NotBoundException()

        val buffer = buffers.computeIfAbsent(target) { glGenBuffers() }
        glBindBuffer(target, buffer)
        glBufferData(target, data, usage)
    }

    fun bufferData(target: Int, data: FloatArray, usage: Int) {
        if (!isBound)
            throw NotBoundException()

        val buffer = buffers.computeIfAbsent(target) { glGenBuffers() }
        glBindBuffer(target, buffer)
        glBufferData(target, data, usage)
    }

    override fun close() = glDeleteVertexArrays(vao)
}
