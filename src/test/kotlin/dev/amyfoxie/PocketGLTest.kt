package dev.amyfoxie

import org.junit.Test
import org.lwjgl.stb.STBImageWrite.stbi_flip_vertically_on_write
import org.lwjgl.stb.STBImageWrite.stbi_write_png

class PocketGLTest {
    @Test
    fun test() {
        Window(300, 300, true).use { window ->
            window.setClearColor(1f, 1f, 1f, 1f)
            window.clearBuffer()

            /*val triangle = Mesh(
                floatArrayOf(
                    1.0f, 1.0f, 0.0f, // 0: top right
                    1.0f, -1.0f, 0.0f, // 1: bottom right
                    -1.0f, -1.0f, 0.0f, // 2: bottom left
                    -1.0f, 1.0f, 0.0f // 3: top left
                ).get(),
                intArrayOf(
                    0, 2, 1,
                    2, 0, 3
                ).get(),
                shaderProgram {
                    vertex {
                        source(PocketGLTest::class.java.getResource("/default.vert")!!.readText())
                    }
                    fragment {
                        source(PocketGLTest::class.java.getResource("/default.frag")!!.readText())
                    }

                    uniform(0, (3 * 16 * Float.SIZE_BYTES).toLong())
                }
            )
            val model = Model(listOf(triangle))
            model.translate(0f, 0f, -1f)
            model.scale(1.0f)*/

            val kindred = Model.from(PocketGLTest::class.java.getResourceAsStream("/Crystal.fbx")!!)

            val camera = Camera()

            val render = OpaqueRender()
            render.render(Scene(kindred), camera, Render.Projection.PERSPECTIVE)

            val buffer = window.readBuffer()
            window.swapBuffers()
            stbi_flip_vertically_on_write(true)
            stbi_write_png(
                "test.png",
                window.width,
                window.height,
                window.channels,
                buffer,
                window.channels * window.width
            )

            //triangle.close()
            kindred.meshes.forEach { it.close() }
        }
    }
}