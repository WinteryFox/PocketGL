package dev.amyfoxie

import org.lwjgl.opengl.GL45.*
import java.nio.FloatBuffer

class ShaderProgram(
    shaders: List<Shader>,
    uniforms: Map<Int, Int>
) : AutoCloseable, Bindable() {
    private val program: Int = glCreateProgram()
    private val uniformBuffer: Int
    private val uniformBufferSize: Long = uniforms.values.reduce { a, b -> a + b }.toLong()

    init {
        if (uniforms.isNotEmpty()) {
            uniformBuffer = glGenBuffers()
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer)
            glBufferData(GL_UNIFORM_BUFFER, uniformBufferSize, GL_DYNAMIC_DRAW)
            glBindBuffer(GL_UNIFORM_BUFFER, 0)

            glBindBufferBase(GL_UNIFORM_BUFFER, 0, uniformBuffer)
        } else {
            uniformBuffer = 0
        }

        shaders.forEach { // TODO: Potentially a bit dodgy
            glAttachShader(program, it.shader)
        }
        glLinkProgram(program)
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE)
            error(glGetProgramInfoLog(program))
    }

    fun writeUniform(offset: Long, data: FloatBuffer) {
        require(offset >= 0)
        require(data.remaining() > 0)
        require(offset + data.remaining() <= uniformBufferSize)

        glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer)
        glBufferSubData(GL_UNIFORM_BUFFER, offset, data)
        glBindBuffer(GL_UNIFORM_BUFFER, 0)
    }

    override fun close() {
        glDeleteBuffers(uniformBuffer)
        glDeleteProgram(program)
    }

    override fun delegateBind() = glUseProgram(program)

    override fun delegateUnbind() = glUseProgram(0)
}

fun shaderProgram(func: ShaderProgramBuilder.() -> Unit): ShaderProgram =
    ShaderProgramBuilder().apply(func).build()

class ShaderProgramBuilder {
    private val shaders = mutableListOf<Shader>()
    private val uniforms = mutableMapOf<Int, Int>()

    fun shader(shader: Shader) = shaders.add(shader)

    fun shader(shader: ShaderBuilder.() -> Unit) = shaders.add(ShaderBuilder().apply(shader).build())

    fun uniform(binding: Int, size: Int) {
        uniforms[binding] = size
    }

    fun build() = ShaderProgram(shaders, uniforms)
}
