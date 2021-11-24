package dev.amyfoxie

import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun Vector4f.get(): FloatBuffer = get(BufferUtils.createFloatBuffer(4))

fun Matrix4f.get(): FloatBuffer = get(BufferUtils.createFloatBuffer(3 * 16))

fun FloatArray.get(): FloatBuffer = BufferUtils.createFloatBuffer(size).put(this).rewind()

fun IntArray.get(): IntBuffer = BufferUtils.createIntBuffer(size).put(this).rewind()
