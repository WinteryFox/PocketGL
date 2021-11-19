package dev.amyfoxie

import org.lwjgl.opengl.GL45.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(
    vertices: FloatBuffer,
    indices: IntBuffer,
    internal val shader: ShaderProgram,
) : AutoCloseable, Bindable() {
    private val vao: Int = glGenVertexArrays()
    private val vertexBuffer: Int = glGenBuffers()
    private val indexBuffer: Int = glGenBuffers()
    internal val vertexCount: Int = vertices.remaining()
    internal val indexCount: Int = indices.remaining()

    init {
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, NULL)
        glEnableVertexAttribArray(0)

        glBindVertexArray(0)
    }

    override fun delegateBind() {
        glBindVertexArray(vao)
    }

    override fun delegateUnbind() {
        glBindVertexArray(0)
    }

    override fun close() {
        glDeleteBuffers(intArrayOf(vertexBuffer, indexBuffer))
        glDeleteVertexArrays(vao)
    }
}
