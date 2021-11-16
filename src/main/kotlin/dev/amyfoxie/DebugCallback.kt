package dev.amyfoxie

import mu.KotlinLogging
import org.lwjgl.opengl.GL45.*
import org.lwjgl.opengl.GLDebugMessageCallback

object DebugCallback : GLDebugMessageCallback() {
    private val logger = KotlinLogging.logger {}

    override fun invoke(source: Int, type: Int, id: Int, severity: Int, length: Int, message: Long, userParam: Long) {
        val builder = StringBuilder()

        builder.append(
            when (source) {
                GL_DEBUG_SOURCE_API -> "[API]"
                GL_DEBUG_SOURCE_WINDOW_SYSTEM -> "[Window System]"
                GL_DEBUG_SOURCE_SHADER_COMPILER -> "[Shader Compiler]"
                GL_DEBUG_SOURCE_THIRD_PARTY -> "[Third Party]"
                GL_DEBUG_SOURCE_APPLICATION -> "[Application]"
                else -> "[Other]"
            }
        )
        builder.append(" ")
        builder.append(
            when (type) {
                GL_DEBUG_TYPE_ERROR -> "[Error]"
                GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR -> "[Deprecated Behaviour]"
                GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR -> "[Undefined Behaviour]"
                GL_DEBUG_TYPE_PORTABILITY -> "[Portability]"
                GL_DEBUG_TYPE_PERFORMANCE -> "[Performance]"
                GL_DEBUG_TYPE_MARKER -> "[Marker]"
                GL_DEBUG_TYPE_PUSH_GROUP -> "[Push Group]"
                GL_DEBUG_TYPE_POP_GROUP -> "[Pop Group]"
                else -> "[Other]"
            }
        )
        builder.append(" ")
        builder.append(getMessage(length, message))

        when (severity) {
            GL_DEBUG_SEVERITY_HIGH -> logger.error(builder.toString())
            GL_DEBUG_SEVERITY_MEDIUM, GL_DEBUG_SEVERITY_LOW -> logger.warn(builder.toString())
            else -> logger.debug(builder.toString())
        }
    }
}
