package at.petrak.hexcasting.common.casting.actions.eval

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpForEach : Action {
    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
        val stack = image.stack.toMutableList()

        if (stack.size < 2)
            throw MishapNotEnoughArgs(2, stack.size)

        val newStack = stack.takeLast(2)

        val instrs: SpellList
        val datums: SpellList

        try {
            instrs = newStack.getList(newStack.lastIndex - 1)
        } catch (m: MishapInvalidIota) {
            throw  MishapInvalidIota.ofType(m.perpetrator, 1, "list")
        }

        try {
            datums = newStack.getList(newStack.lastIndex)
        } catch (m: MishapInvalidIota) {
            throw  MishapInvalidIota.ofType(m.perpetrator, 0, "list")
        }

        stack.removeLastOrNull()
        stack.removeLastOrNull()

        val frame = FrameForEach(datums, instrs, null, mutableListOf())
        val image2 = image.withUsedOp().copy(stack = stack)
        return OperationResult(image2, listOf(), continuation.pushFrame(frame), HexEvalSounds.THOTH)
    }
}
