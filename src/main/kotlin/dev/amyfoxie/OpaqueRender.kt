package dev.amyfoxie

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*

class OpaqueRender : Render {
    override fun render(scene: Scene, camera: Camera, projection: Render.Projection) {
        scene.entities.forEach { model ->
            model.meshes.forEach { mesh ->
                mesh.shader.bind()
                mesh.vao.bind()

                val mvp = BufferUtils.createFloatBuffer(3 * 16)
                model.matrix.get(mvp)
                camera.matrix.get(16, mvp)

                when (projection) {
                    Render.Projection.PERSPECTIVE -> camera.perspective(90f, 0.1f, 1000f, 1f).get(2 * 16, mvp)
                    Render.Projection.ORTHOGONAL -> camera.orthogonal().get(2 * 16, mvp)
                }

                mesh.shader.writeUniform(0, 0, mvp)

                glDrawElements(GL_TRIANGLES, mesh.indexCount, GL_UNSIGNED_INT, 0)
            }
        }
    }
}
