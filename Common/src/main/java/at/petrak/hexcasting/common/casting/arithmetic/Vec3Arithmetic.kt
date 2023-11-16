package at.petrak.hexcasting.common.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.castTo
import at.petrak.hexcasting.common.casting.arithmetic.operator.vec.OperatorPack
import at.petrak.hexcasting.common.casting.arithmetic.operator.vec.OperatorUnpack
import at.petrak.hexcasting.common.casting.arithmetic.operator.vec.OperatorVec3Delegating
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.VEC3
import net.minecraft.world.phys.Vec3
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.math.ceil
import kotlin.math.floor

object Vec3Arithmetic : Arithmetic {
    private val OPS = listOf(
        PACK,
        UNPACK,
        ADD,
        SUB,
        MUL,
        DIV,
        ABS,
        POW,
        FLOOR,
        CEIL,
        MOD
    )
    val ACCEPTS = IotaMultiPredicate.any(IotaPredicate.ofType(HexIotaTypes.VEC3), IotaPredicate.ofType(HexIotaTypes.DOUBLE))


    override fun arithName() = "vec3_math"

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern): Operator = when (pattern) {
        PACK -> OperatorPack
        UNPACK -> OperatorUnpack
        ADD -> make2Fallback(pattern)
        SUB -> make2Fallback(pattern)
        MUL -> make2Double(pattern, Vec3::dot)
        DIV -> make2Vec(pattern, Vec3::cross)
        ABS -> make1Double(Vec3::length)
        POW -> make2Vec(pattern) { u, v -> v.normalize().scale(u.dot(v.normalize())) }
        FLOOR -> make1{ v -> Vec3(floor(v.x), floor(v.y), floor(v.z)) }
        CEIL -> make1{ v -> Vec3(ceil(v.x), ceil(v.y), ceil(v.z)) }
        MOD -> make2Fallback(pattern)
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")

    }

    private fun make1(op: Function<Vec3, Vec3>): OperatorUnary =
        OperatorUnary(ACCEPTS) { i: Iota -> Vec3Iota(op.apply(i.castTo(VEC3).getVec3())) }

    private fun make1Double(op: Function<Vec3, Double>): OperatorUnary =
        OperatorUnary(ACCEPTS) { i: Iota -> DoubleIota(op.apply(i.castTo(VEC3).getVec3())) }

    private fun make2Fallback(pattern: HexPattern): OperatorVec3Delegating = OperatorVec3Delegating(null, pattern)

    private fun make2Double(pattern: HexPattern, op: BiFunction<Vec3, Vec3, Double>): OperatorVec3Delegating = OperatorVec3Delegating(op.andThen{DoubleIota(it)}, pattern)

    private fun make2Vec(pattern: HexPattern, op: BiFunction<Vec3, Vec3, Vec3>): OperatorVec3Delegating = OperatorVec3Delegating(op.andThen{Vec3Iota(it)}, pattern)

}




