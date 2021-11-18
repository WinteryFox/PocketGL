package dev.amyfoxie

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
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

        val program = shaderProgram {
            shader {
                source(DiscordGL::class.java.getResource("/default.vert")!!.readText())
                type(ShaderType.VERTEX)
            }

            shader {
                source(DiscordGL::class.java.getResource("/default.frag")!!.readText())
                type(ShaderType.FRAGMENT)
            }

            uniform(0, 3 * 16 * Float.SIZE_BYTES)
        }

        val obj = Matrix4f().rotateZ(90.0f).scale(0.2f)
        val camera = Camera()
        //val camera = Camera(rotation = Quaternionf().rotateZ(toRadians(90.0f)))
        program.use {
            program.run {
                val mvp = BufferUtils.createFloatBuffer(3 * 16)
                //Matrix4f().get(mvp)
                obj.get(0, mvp)
                camera.view.get(16, mvp)
                camera.project(1.0f).get(2 * 16, mvp)

                program.writeUniform(0, mvp)

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
