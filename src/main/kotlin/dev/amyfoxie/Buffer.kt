package dev.amyfoxie

import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

fun Vector4f.get(): FloatBuffer = get(BufferUtils.createFloatBuffer(4))

fun Matrix4f.get(): FloatBuffer = get(BufferUtils.createFloatBuffer(3 * 16))
