package dev.amyfoxie

import mu.KotlinLogging
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL45.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.ByteBuffer

class Window(
    val width: Int,
    val height: Int,
    debug: Boolean = false
) : AutoCloseable {
    val channels = 4
    private val window: Long
    private val logger = KotlinLogging.logger {}

    init {
        GLFWErrorCallback.createPrint(System.err).set()
        //glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_NULL)
        if (!glfwInit())
            error("Failed to initialise GLFW")

        //glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_OSMESA_CONTEXT_API)
        glfwWindowHint(GLFW_SAMPLES, 8)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, if (debug) GLFW_TRUE else GLFW_TRUE)
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE)
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        window = glfwCreateWindow(width, height, "Hi", NULL, NULL)
        if (window == NULL)
            error("Failed to create GLFW window")

        glfwMakeContextCurrent(window)
        createCapabilities()

        if (debug) {
            if (glGetInteger(GL_CONTEXT_FLAGS) and GL_CONTEXT_FLAG_DEBUG_BIT != GL_CONTEXT_FLAG_DEBUG_BIT) {
                logger.warn("OpenGL debug output requested but GL context is not a debug context")
            } else {
                glEnable(GL_DEBUG_OUTPUT)
                glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
                glDebugMessageCallback(DebugCallback, NULL)
                glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, null as IntArray?, true)
            }
        }

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
    }

    fun setClearColor(red: Float, green: Float, blue: Float, alpha: Float) = glClearColor(red, green, blue, alpha)

    fun clearBuffer() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    fun swapBuffers() = glfwSwapBuffers(window)

    fun readBuffer(): ByteBuffer {
        val buffer = BufferUtils.createByteBuffer(channels * width * height)
        glPixelStorei(GL_PACK_ALIGNMENT, 1)
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        return buffer
    }

    override fun close() {
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
}