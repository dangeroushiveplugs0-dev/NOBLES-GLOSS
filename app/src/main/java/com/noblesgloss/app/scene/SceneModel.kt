package com.noblesgloss.app.scene

import java.util.UUID

enum class SceneObjectKind { CUBE, SPHERE, PLANE, MESH, CAMERA, LIGHT }

data class Transform3D(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val rotationX: Float = 0f,
    val rotationY: Float = 0f,
    val rotationZ: Float = 0f,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val scaleZ: Float = 1f
) {
    fun hasFiniteValues(): Boolean = listOf(
        x, y, z, rotationX, rotationY, rotationZ, scaleX, scaleY, scaleZ
    ).all { it.isFinite() }
}

data class SceneObject(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val kind: SceneObjectKind,
    val transform: Transform3D = Transform3D(),
    val visible: Boolean = true
)

data class SceneDocument(
    val name: String = "Untitled Scene",
    val objects: List<SceneObject> = emptyList(),
    val selectedObjectId: String? = null
) {
    fun addObject(obj: SceneObject): SceneDocument {
        require(objects.none { it.id == obj.id }) { "Duplicate scene object id: ${obj.id}" }
        return copy(objects = objects + obj, selectedObjectId = obj.id)
    }

    fun removeObject(id: String): SceneDocument {
        val remaining = objects.filterNot { it.id == id }
        val nextSelection = if (selectedObjectId == id) {
            remaining.lastOrNull()?.id
        } else {
            selectedObjectId.takeIf { selected -> remaining.any { it.id == selected } }
        }
        return copy(objects = remaining, selectedObjectId = nextSelection)
    }

    fun updateTransform(id: String, transform: Transform3D): SceneDocument {
        require(transform.hasFiniteValues()) { "Transform values must be finite" }
        require(objects.any { it.id == id }) { "Scene object not found: $id" }
        return copy(objects = objects.map { if (it.id == id) it.copy(transform = transform) else it })
    }

    fun select(id: String?): SceneDocument {
        require(id == null || objects.any { it.id == id }) { "Cannot select missing scene object" }
        return copy(selectedObjectId = id)
    }
}
