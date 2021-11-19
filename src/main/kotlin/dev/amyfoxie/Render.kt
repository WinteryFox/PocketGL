package dev.amyfoxie

interface Render {
    enum class Projection {
        PERSPECTIVE,
        ORTHOGONAL
    }

    fun render(scene: Scene, camera: Camera, projection: Projection = Projection.PERSPECTIVE)
}
