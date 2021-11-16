package dev.amyfoxie

import org.lwjgl.opengl.GL45.*

class ShaderProgram(
    shaders: List<Shader>
) : AutoCloseable, Bindable() {
    private val program: Int = glCreateProgram()

    init {
        shaders.forEach { // TODO: Potentially a bit dodgy
            glAttachShader(program, it.shader)
        }
        glLinkProgram(program)
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
            error(glGetProgramInfoLog(program))
    }

    override fun close() = glDeleteProgram(program)

    override fun delegateBind() {
        glUseProgram(program)
    }

    override fun delegateUnbind() {
        glUseProgram(0)
    }
}