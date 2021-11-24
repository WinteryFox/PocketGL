package dev.amyfoxie

import org.lwjgl.opengl.GL45.*

class Shader(
    type: ShaderType,
    source: String
) : AutoCloseable {
    internal val shader: Int = glCreateShader(type.type)

    init {
        glShaderSource(shader, source)
        glCompileShader(shader)
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE)
            throw ShaderCompilationException("Failed to compile $type shader; ${glGetShaderInfoLog(shader)}")
    }

    override fun close() = glDeleteShader(shader)
}

enum class ShaderType(val type: Int) {
    VERTEX(GL_VERTEX_SHADER),
    GEOMETRY(GL_GEOMETRY_SHADER),
    COMPUTE(GL_COMPUTE_SHADER),
    TESSELATION_CONTROL(GL_TESS_CONTROL_SHADER),
    TESSELATION_EVALUATION(GL_TESS_EVALUATION_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER)
}

class ShaderBuilder(
    private val type: ShaderType
) {
    private lateinit var source: String

    fun source(source: String) {
        this.source = source
    }

    fun build(): Shader {
        return Shader(type, source)
    }
}
