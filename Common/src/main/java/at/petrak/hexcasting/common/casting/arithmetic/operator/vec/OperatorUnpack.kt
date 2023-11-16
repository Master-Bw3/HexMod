package at.petrak.hexcasting.common.casting.arithmetic.operator.vec

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.casting.arithmetic.operator.castTo
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes


object OperatorUnpack : Operator(1, IotaMultiPredicate.all(IotaPredicate.ofType(HexIotaTypes.VEC3))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        val vec = it.next().castTo(HexIotaTypes.VEC3).getVec3()
        return listOf<Iota>(DoubleIota(vec.x), DoubleIota(vec.y), DoubleIota(vec.z))
    }

}
