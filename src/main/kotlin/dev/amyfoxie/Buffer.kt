package dev.amyfoxie

import org.lwjgl.opengl.GL45.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Buffer : AutoCloseable {
    private val buffer: Int = glGenBuffers()

    fun bufferData(target: Int, data: FloatBuffer, usage: Int) {
        glBufferData(target, data, usage)
    }

    fun bufferData(target: Int, data: IntBuffer, usage: Int) {
        glBufferData(target, data, usage)
    }

    fun bind(target: Int) {
        glBindBuffer(target, buffer)
    }

    override fun close() {
        glDeleteBuffers(buffer)
    }
}
