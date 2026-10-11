package com.noblesgloss.app.scene

/**
 * Small immutable history layer. Commands return new scene snapshots, so failed edits
 * cannot partially mutate the current document.
 */
interface SceneCommand {
    fun apply(document: SceneDocument): SceneDocument
    fun revert(document: SceneDocument): SceneDocument
}

data class AddSceneObjectCommand(val objectToAdd: SceneObject) : SceneCommand {
    override fun apply(document: SceneDocument) = document.addObject(objectToAdd)
    override fun revert(document: SceneDocument) = document.removeObject(objectToAdd.id)
}

data class TransformSceneObjectCommand(
    val objectId: String,
    val before: Transform3D,
    val after: Transform3D
) : SceneCommand {
    override fun apply(document: SceneDocument) = document.updateTransform(objectId, after)
    override fun revert(document: SceneDocument) = document.updateTransform(objectId, before)
}

data class SceneHistory(
    val current: SceneDocument = SceneDocument(),
    private val undoStack: List<SceneCommand> = emptyList(),
    private val redoStack: List<SceneCommand> = emptyList()
) {
    fun execute(command: SceneCommand): SceneHistory {
        val next = command.apply(current)
        return copy(current = next, undoStack = undoStack + command, redoStack = emptyList())
    }

    fun undo(): SceneHistory {
        val command = undoStack.lastOrNull() ?: return this
        return copy(
            current = command.revert(current),
            undoStack = undoStack.dropLast(1),
            redoStack = redoStack + command
        )
    }

    fun redo(): SceneHistory {
        val command = redoStack.lastOrNull() ?: return this
        return copy(
            current = command.apply(current),
            undoStack = undoStack + command,
            redoStack = redoStack.dropLast(1)
        )
    }

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()
}


data class RemoveSceneObjectCommand(
    val objectToRemove: SceneObject,
    val previouslySelectedObjectId: String?
) : SceneCommand {
    override fun apply(document: SceneDocument): SceneDocument =
        document.removeObject(objectToRemove.id)

    override fun revert(document: SceneDocument): SceneDocument {
        val restored = document.addObject(objectToRemove)
        return if (previouslySelectedObjectId != null &&
            restored.objects.any { it.id == previouslySelectedObjectId }
        ) {
            restored.select(previouslySelectedObjectId)
        } else {
            restored
        }
    }
}
