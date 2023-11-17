package at.petrak.hexcasting.common.casting.arithmetic.operator.vec

import at.petrak.hexcasting.api.casting.arithmetic.IterPair
import at.petrak.hexcasting.api.casting.arithmetic.TripleIterable
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapDivideByZero
import at.petrak.hexcasting.api.casting.mishaps.MishapDivideByZero.Companion.of
import at.petrak.hexcasting.common.casting.arithmetic.DoubleArithmetic.getOperator
import at.petrak.hexcasting.common.casting.arithmetic.operator.castTo
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.world.phys.Vec3
import java.util.*
import java.util.function.BiFunction
import kotlin.collections.Iterable

class OperatorVec3Delegating(private val op: BiFunction<Vec3, Vec3, Iota>?, fallback: HexPattern?) :
    OperatorBasic(
        2,
        IotaMultiPredicate.any(IotaPredicate.ofType(HexIotaTypes.VEC3), IotaPredicate.ofType(HexIotaTypes.DOUBLE))
    ) {
    private val fb: OperatorBasic

    init {
        fb = Objects.requireNonNull(
            getOperator(
                fallback!!
            )
        ) as OperatorBasic
    }

    @Throws(Mishap::class)
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        val left = it.next()
        val right = it.next()
        return try {
            if (op != null && left is Vec3Iota && right is Vec3Iota) {
                return listOf(op.apply(left.getVec3(), right.getVec3()))
            }
            val lh = if (left is Vec3Iota) left.getVec3() else triplicate(left.castTo(HexIotaTypes.DOUBLE).getDouble())
            val rh =
                if (right is Vec3Iota) right.getVec3() else triplicate(right.castTo(HexIotaTypes.DOUBLE).getDouble())
            TripleIterable<Iota, Iota, Iota, Iota>(
                fb.apply(IterPair<Iota>(DoubleIota(lh.x()), DoubleIota(rh.x())), env),
                fb.apply(IterPair<Iota>(DoubleIota(lh.y()), DoubleIota(rh.y())), env),
                fb.apply(IterPair<Iota>(DoubleIota(lh.z()), DoubleIota(rh.z())), env)
            ) { x: Iota, y: Iota?, z: Iota ->
                Vec3Iota(
                    Vec3(
                        x.castTo(HexIotaTypes.DOUBLE).getDouble(),
                        x.castTo(HexIotaTypes.DOUBLE).getDouble(),
                        z.castTo(HexIotaTypes.DOUBLE).getDouble()
                    )
                )
            }
        } catch (e: MishapDivideByZero) {
            throw of(left, right, e.suffix)
        }
    }

    companion object {
        fun triplicate(`in`: Double): Vec3 {
            return Vec3(`in`, `in`, `in`)
        }
    }
}

