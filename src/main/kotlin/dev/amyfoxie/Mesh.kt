package dev.amyfoxie

import org.lwjgl.assimp.Assimp.aiImportFile
import org.lwjgl.assimp.Assimp.aiProcessPreset_TargetRealtime_MaxQuality
import org.lwjgl.opengl.GL45.*
import org.lwjgl.system.MemoryUtil.NULL
import java.io.File
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(
    vertices: FloatBuffer,
    indices: IntBuffer,
    internal val shader: ShaderProgram,
) : AutoCloseable {
    internal val vao = VertexArrayObject()
    private val vertexBuffer = Buffer()
    private val indexBuffer = Buffer()
    val vertexCount: Int = vertices.remaining()
    val indexCount: Int = indices.remaining()

    init {
        vao.bind()

        vertexBuffer.bind(GL_ARRAY_BUFFER)
        vertexBuffer.bufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER)
        indexBuffer.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        vao.attributePointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, NULL)
        vao.enableAttributeArray(0)
    }

    override fun close() {
        vao.close()
        vertexBuffer.close()
        indexBuffer.close()
    }
}
