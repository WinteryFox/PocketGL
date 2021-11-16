package dev.amyfoxie

import org.lwjgl.opengl.GL45.*
import org.lwjgl.stb.STBImageWrite.*
import org.lwjgl.system.MemoryUtil.NULL

class DiscordGL

fun main() {
    Window(300, 300, true).use { window ->
        window.setClearColor(0f, 0f, 0f, 0f)
        window.clearBuffer()

        val vertices = floatArrayOf(
            1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f
        )
        val indices = intArrayOf(
            0, 1, 3,
            1, 2, 3
        )

        val vao = VertexArrayObject()
        vao.run {
            vao.bufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
            vao.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, NULL)
            glEnableVertexAttribArray(0)
        }

        val program =
            Shader(GL_VERTEX_SHADER, DiscordGL::class.java.getResource("/default.vert")!!.readText()).use { v ->
                Shader(GL_FRAGMENT_SHADER, DiscordGL::class.java.getResource("/default.frag")!!.readText()).use { f ->
                    ShaderProgram(listOf(v, f))
                }
            }

        program.run {
            vao.run {
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
            }
        }

        window.swapBuffers()

        val buffer = window.readBuffer()
        stbi_flip_vertically_on_write(true)
        stbi_write_png("test.png", window.width, window.height, window.channels, buffer, window.channels * window.width)
    }
}
