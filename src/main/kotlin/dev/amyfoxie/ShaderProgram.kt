package dev.amyfoxie

import org.lwjgl.opengl.GL45.*
import java.nio.FloatBuffer

class ShaderProgram(
    shaders: List<Shader>,
    uniforms: Map<Int, Long>
) : AutoCloseable {
    private val program: Int = glCreateProgram()
    private val uniformBuffers: Map<Int, ShaderBinding>

    init {
        val bufferMap = mutableMapOf<Int, ShaderBinding>()
        for ((binding, size) in uniforms) {
            val buffer = glGenBuffers() // TODO: Generate all buffers in 1 call
            glBindBuffer(GL_UNIFORM_BUFFER, buffer)
            glBufferData(GL_UNIFORM_BUFFER, size, GL_DYNAMIC_DRAW)
            glBindBuffer(GL_UNIFORM_BUFFER, 0)

            glBindBufferBase(GL_UNIFORM_BUFFER, binding, buffer)
            bufferMap[binding] = ShaderBinding(size, buffer)
        }
        uniformBuffers = bufferMap

        shaders.forEach { // TODO: Potentially a bit dodgy
            glAttachShader(program, it.shader)
        }
        glLinkProgram(program)
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE)
            error(glGetProgramInfoLog(program))
    }

    fun bind() = glUseProgram(program)

    fun writeUniform(binding: Int, offset: Long, data: FloatBuffer) {
        val buffer = uniformBuffers[binding]
        require(buffer != null)
        require(offset >= 0)
        require(data.remaining() > 0)
        require(offset + data.remaining() <= buffer.size)

        glBindBuffer(GL_UNIFORM_BUFFER, buffer.buffer)
        glBufferSubData(GL_UNIFORM_BUFFER, offset, data)
        glBindBuffer(GL_UNIFORM_BUFFER, 0)
    }

    override fun close() = glDeleteProgram(program)
}

fun shaderProgram(builder: ShaderProgramBuilder.() -> Unit): ShaderProgram =
    ShaderProgramBuilder().apply(builder).build()

private data class ShaderBinding(
    val size: Long,
    val buffer: Int
)

class ShaderProgramBuilder {
    private lateinit var vertex: Shader
    private lateinit var fragment: Shader
    private val uniforms = mutableMapOf<Int, Long>()

    // TODO: Add other shader types
    fun vertex(shader: ShaderBuilder.() -> Unit) {
        vertex = ShaderBuilder(ShaderType.VERTEX).apply(shader).build()
    }

    fun fragment(shader: ShaderBuilder.() -> Unit) {
        fragment = ShaderBuilder(ShaderType.FRAGMENT).apply(shader).build()
    }

    fun uniform(binding: Int, size: Long) {
        uniforms[binding] = size
    }

    fun build() = ShaderProgram(listOf(vertex, fragment), uniforms)
}
