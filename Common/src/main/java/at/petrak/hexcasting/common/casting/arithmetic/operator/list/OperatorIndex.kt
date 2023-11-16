package at.petrak.hexcasting.common.casting.arithmetic.operator.list

import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.casting.arithmetic.operator.castTo
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.*
import kotlin.math.roundToInt

object OperatorIndex : Operator(2, IotaMultiPredicate.pair(IotaPredicate.ofType(LIST), IotaPredicate.ofType(DOUBLE))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        val list = it.next().castTo(LIST).list.toMutableList()
        val index = it.next().castTo(DOUBLE).double
        val x = list.getOrElse(index.roundToInt()) { NullIota() }
        return listOf(x)
    }
}
