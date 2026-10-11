package com.noblesgloss.app.materials

enum class SocketDirection { INPUT, OUTPUT }
enum class MaterialValueType { FLOAT, COLOR, VECTOR3, TEXTURE, BOOLEAN }
enum class MaterialNodeKind {
    PRINCIPLED_SURFACE, OUTPUT, CONSTANT_FLOAT, CONSTANT_COLOR, NOISE,
    VORONOI, MATH, MIX, NORMAL_MAP, FRESNEL, UV_COORDINATES, IMAGE_TEXTURE
}

data class MaterialSocket(
    val id: String,
    val name: String,
    val direction: SocketDirection,
    val valueType: MaterialValueType
)

data class MaterialNode(
    val id: String,
    val kind: MaterialNodeKind,
    val title: String,
    val inputs: List<MaterialSocket> = emptyList(),
    val outputs: List<MaterialSocket> = emptyList(),
    val parameters: Map<String, String> = emptyMap()
)

data class MaterialConnection(
    val fromNodeId: String,
    val fromSocketId: String,
    val toNodeId: String,
    val toSocketId: String
)

data class MaterialGraph(
    val nodes: List<MaterialNode> = emptyList(),
    val connections: List<MaterialConnection> = emptyList()
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        val nodeIds = nodes.map { it.id }.toSet()
        if (nodeIds.size != nodes.size) errors += "Node IDs must be unique"

        nodes.forEach { node ->
            val allSockets = node.inputs + node.outputs
            if (allSockets.map { it.id }.toSet().size != allSockets.size) {
                errors += "Socket IDs must be unique within node ${node.id}"
            }
            if (node.inputs.any { it.direction != SocketDirection.INPUT }) {
                errors += "Input sockets must use INPUT direction on node ${node.id}"
            }
            if (node.outputs.any { it.direction != SocketDirection.OUTPUT }) {
                errors += "Output sockets must use OUTPUT direction on node ${node.id}"
            }
        }

        connections.forEach { edge ->
            val sourceNode = nodes.find { it.id == edge.fromNodeId }
            val targetNode = nodes.find { it.id == edge.toNodeId }
            val source = sourceNode?.outputs?.find { it.id == edge.fromSocketId }
            val target = targetNode?.inputs?.find { it.id == edge.toSocketId }

            if (source == null) {
                errors += "Connection source output not found: ${edge.fromNodeId}/${edge.fromSocketId}"
            }
            if (target == null) {
                errors += "Connection target input not found: ${edge.toNodeId}/${edge.toSocketId}"
            }
            if (source != null && target != null && source.valueType != target.valueType) {
                errors += "Socket type mismatch: ${source.valueType} -> ${target.valueType}"
            }
            if (edge.fromNodeId == edge.toNodeId) {
                errors += "Self-connections are not allowed"
            }
        }
        return errors.distinct()
    }
}
