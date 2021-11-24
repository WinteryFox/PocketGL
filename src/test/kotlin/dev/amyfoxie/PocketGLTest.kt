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

            val kindred = Model.from("./models/kindred/scene.gltf")
            kindred.scale(1f)
            kindred.rotateDegrees(0f, 0f, 0f)

            val camera = Camera()
            camera.setPosition(0f, 5f, 0f)
            camera.rotateDegrees(0f, 0f, 0f)

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

            kindred.meshes.forEach { it.close() }
        }
    }
}