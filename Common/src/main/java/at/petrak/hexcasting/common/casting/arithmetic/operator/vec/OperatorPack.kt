package at.petrak.hexcasting.common.casting.arithmetic.operator.vec

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.common.casting.arithmetic.operator.castTo
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.world.phys.Vec3
object OperatorPack : Operator(3, IotaMultiPredicate.all(IotaPredicate.ofType(HexIotaTypes.DOUBLE))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        return listOf(
            Vec3Iota(
                Vec3(
                    it.next().castTo(HexIotaTypes.DOUBLE).getDouble(),
                    it.next().castTo(HexIotaTypes.DOUBLE).getDouble(),
                    it.next().castTo(HexIotaTypes.DOUBLE).getDouble()
                )
            )
        )
    }
}
