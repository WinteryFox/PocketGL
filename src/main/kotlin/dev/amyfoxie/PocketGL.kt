package dev.amyfoxie

import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import org.lwjgl.stb.STBImageWrite.*
import org.lwjgl.system.MemoryUtil.NULL

class PocketGL

fun main() {
    Window(300, 300, true).use { window ->
        window.setClearColor(0f, 0f, 0f, 0f)
        window.clearBuffer()

        val vertices = floatArrayOf(
            1.0f, 1.0f, 0.0f, // 0: top right
            1.0f, -1.0f, 0.0f, // 1: bottom right
            -1.0f, -1.0f, 0.0f, // 2: bottom left
            -1.0f, 1.0f, 0.0f // 3: top left
        )
        val indices = intArrayOf(
            0, 2, 1,
            2, 0, 3
        )

        val vao = VertexArrayObject()
        vao.run {
            vao.bufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
            vao.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, NULL)
            glEnableVertexAttribArray(0)
        }

        val program = shaderProgram {
            shader {
                source(PocketGL::class.java.getResource("/default.vert")!!.readText())
                type(ShaderType.VERTEX)
            }

            shader {
                source(PocketGL::class.java.getResource("/default.frag")!!.readText())
                type(ShaderType.FRAGMENT)
            }

            uniform(0, (3 * 16 * Float.SIZE_BYTES).toLong())
            uniform(1, (4 * Float.SIZE_BYTES).toLong())
        }

        val camera = Camera()
        camera.rotateDegrees(0f, 0f, 45f)
        program.use {
            program.run {
                val mvp = BufferUtils.createFloatBuffer(3 * 16)
                Matrix4f().translate(0f, 0f, -1f).get(0, mvp)
                camera.matrix.get(16, mvp)
                camera.perspective(window.width.toFloat() / window.height.toFloat()).get(2 * 16, mvp)

                program.writeUniform(0, 0, mvp)
                program.writeUniform(1, 0, Vector4f(1f, 0f, 1f, 1f).get())

                vao.use {
                    vao.run {
                        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
                    }
                }
            }
        }

        val buffer = window.readBuffer()
        window.swapBuffers()
        stbi_flip_vertically_on_write(true)
        stbi_write_png("test.png", window.width, window.height, window.channels, buffer, window.channels * window.width)
    }
}
