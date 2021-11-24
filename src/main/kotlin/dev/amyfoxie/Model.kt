package dev.amyfoxie

import org.lwjgl.BufferUtils
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.lang.IllegalStateException
import java.nio.IntBuffer

data class Model(
    val meshes: List<Mesh>
) : Entity() {
    constructor(vararg meshes: Mesh) : this(meshes.toList())

    companion object {
        fun from(stream: InputStream): Model {
            stream.use {
                val bytes = it.readAllBytes()
                val contentsBuffer = MemoryUtil.memAlloc(bytes.size)
                contentsBuffer.put(bytes)
                contentsBuffer.flip()

                try {
                    aiImportFileFromMemory( // TODO: YES
                        contentsBuffer,
                        aiProcessPreset_TargetRealtime_MaxQuality,
                        ""
                    )
                } finally {
                    MemoryUtil.memFree(contentsBuffer)
                }.use { scene ->
                    val root = scene?.mRootNode()
                    if (scene == null || scene.mFlags() and AI_SCENE_FLAGS_INCOMPLETE != 0 || root == null)
                        throw IllegalStateException(
                            "Failed to import from file",
                            IllegalStateException(aiGetErrorString())
                        )

                    // TODO: Handle different carriage returns
                    //val directory = file.substring(0, file.indexOfLast { c -> c == '/' })
                    val meshes = mutableListOf<Mesh>()
                    println(scene.mName().dataString())

                    fun processMesh(mesh: AIMesh): Mesh {
                        val vertices = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3)
                        val indices = BufferUtils.createIntBuffer(mesh.mNumFaces() * 3)

                        val aiVertices = mesh.mVertices()
                        while (aiVertices.remaining() > 0) {
                            val vertex = aiVertices.get()
                            vertices.put(vertex.x())
                            vertices.put(vertex.y())
                            vertices.put(vertex.z())
                        }

                        val aiFaces = mesh.mFaces()
                        while (aiFaces.remaining() > 0) {
                            val aiFace = aiFaces.get()
                            val aiIndices = aiFace.mIndices()
                            indices.put(aiIndices[0])
                            indices.put(aiIndices[1])
                            indices.put(aiIndices[2])
                        }

                        vertices.flip()
                        indices.flip()
                        return Mesh(
                            vertices,
                            indices,
                            shaderProgram {
                                vertex {
                                    source(Model::class.java.getResource("/default.vert")!!.readText())
                                }
                                fragment {
                                    source(Model::class.java.getResource("/default.frag")!!.readText())
                                }

                                uniform(0, (3 * 16 * Float.SIZE_BYTES).toLong())
                            }
                        )
                    }

                    fun processNode(node: AINode) {
                        if (node.mNumMeshes() > 0) {
                            val aiMeshes = node.mMeshes()!!
                            while (aiMeshes.remaining() > 0)
                                meshes.add(processMesh(AIMesh.create(scene.mMeshes()!![aiMeshes.get()])))
                        }
                        /*if (node.mNumMeshes() > 0 && aiMeshes != null)
                            for (i in 0 until node.mNumMeshes())
                                meshes.add(processMesh(AIMesh.create(aiMeshes.get(i))))*/

                        if (node.mNumChildren() > 0) {
                            val aiChildren = node.mChildren()!!
                            while (aiChildren.remaining() > 0)
                                processNode(AINode.create(aiChildren.get()))
                        }
                        /*if (node.mNumChildren() > 0)
                            for (i in 0 until node.mNumChildren())
                                processNode(AINode.create(node.mChildren()!!.get(i)))*/
                    }

                    processNode(root)

                    return Model(meshes)
                }
            }
        }
    }
}
