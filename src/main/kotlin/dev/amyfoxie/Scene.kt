package dev.amyfoxie

data class Scene(
    val entities: List<Model>
) {
    constructor(vararg entities: Model) : this(entities.toList())
}
