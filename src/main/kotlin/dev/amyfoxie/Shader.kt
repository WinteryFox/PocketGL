package dev.amyfoxie

import org.lwjgl.opengl.GL45.*

class Shader(
    type: Int,
    source: String
) : AutoCloseable {
    internal val shader: Int = glCreateShader(type)

    init {
        glShaderSource(shader, source)
        glCompileShader(shader)
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
            error(glGetShaderInfoLog(shader))
    }

    override fun close() = glDeleteShader(shader)
}
