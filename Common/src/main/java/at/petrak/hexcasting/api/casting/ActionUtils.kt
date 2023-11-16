@file:JvmName("OperatorUtils")

package at.petrak.hexcasting.api.casting

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.*
import com.mojang.datafixers.util.Either
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.*
import java.util.function.DoubleUnaryOperator
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun List<Iota>.getDouble(idx: Int, argc: Int = 0): Double {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        return iota.get().double
    } else {
        // TODO: I'm not sure this calculation is correct
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "double")
    }
}

fun List<Iota>.getEntity(idx: Int, argc: Int = 0): Entity {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(ENTITY)
    if (iota.isPresent) {
        return iota.get().entity
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity")
    }
}

fun List<Iota>.getList(idx: Int, argc: Int = 0): SpellList {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(LIST)
    if (iota.isPresent) {
        return iota.get().list
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "list")
    }
}

fun List<Iota>.getPattern(idx: Int, argc: Int = 0): HexPattern {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(PATTERN)
    if (iota.isPresent) {
        return iota.get().pattern
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "pattern")
    }
}

fun List<Iota>.getVec3(idx: Int, argc: Int = 0): Vec3 {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(VEC3)
    if (iota.isPresent) {
        return iota.get().vec3
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "vector")
    }
}

fun List<Iota>.getBool(idx: Int, argc: Int = 0): Boolean {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(BOOLEAN)
    if (iota.isPresent) {
        return iota.get().bool
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "boolean")
    }
}

// Helpers

fun List<Iota>.getItemEntity(idx: Int, argc: Int = 0): ItemEntity {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(ENTITY)
    if (iota.isPresent) {
        val e = iota.get().entity
        if (e is ItemEntity)
            return e
    }
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.item")
}

fun List<Iota>.getPlayer(idx: Int, argc: Int = 0): ServerPlayer {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(ENTITY)
    if (iota.isPresent) {
        val e = iota.get().entity
        if (e is ServerPlayer)
            return e
    }
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.player")
}

fun List<Iota>.getMob(idx: Int, argc: Int = 0): Mob {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(ENTITY)
    if (iota.isPresent) {
        val e = iota.get().entity
        if (e is Mob)
            return e
    }
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.mob")
}

fun List<Iota>.getLivingEntityButNotArmorStand(idx: Int, argc: Int = 0): LivingEntity {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(ENTITY)
    if (iota.isPresent) {
        val e = iota.get().entity
        if (e is LivingEntity && e !is ArmorStand)
            return e
    }
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.living")
}

fun List<Iota>.getPositiveDouble(idx: Int, argc: Int = 0): Double {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        if (0 <= double) {
            return double
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "double.positive")
}

fun List<Iota>.getPositiveDoubleUnder(idx: Int, max: Double, argc: Int = 0): Double {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        if (0.0 <= double && double < max) {
            return double
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "double.positive.less", max)
}

fun List<Iota>.getPositiveDoubleUnderInclusive(idx: Int, max: Double, argc: Int = 0): Double {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        if (double in 0.0..max) {
            return double
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "double.positive.less.equal", max)
}

fun List<Iota>.getDoubleBetween(idx: Int, min: Double, max: Double, argc: Int = 0): Double {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        if (double in min..max) {
            return double
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "double.between", min, max)
}

fun List<Iota>.getInt(idx: Int, argc: Int = 0): Int {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToInt()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int")
}

fun List<Iota>.getLong(idx: Int, argc: Int = 0): Long {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToLong()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int") // shh we're lying
}

fun List<Iota>.getPositiveInt(idx: Int, argc: Int = 0): Int {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToInt()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE && rounded >= 0) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int.positive")
}

fun List<Iota>.getPositiveIntUnder(idx: Int, max: Int, argc: Int = 0): Int {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToInt()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE && rounded in 0 until max) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int.positive.less", max)
}

fun List<Iota>.getPositiveIntUnderInclusive(idx: Int, max: Int, argc: Int = 0): Int {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToInt()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE && rounded in 0..max) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int.positive.less.equal", max)
}

fun List<Iota>.getIntBetween(idx: Int, min: Int, max: Int, argc: Int = 0): Int {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(DOUBLE)
    if (iota.isPresent) {
        val double = iota.get().double
        val rounded = double.roundToInt()
        if (abs(double - rounded) <= DoubleIota.TOLERANCE && rounded in min..max) {
            return rounded
        }
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "int.between", min, max)
}

fun List<Iota>.getBlockPos(idx: Int, argc: Int = 0): BlockPos {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastTo(VEC3)
    if (iota.isPresent) {
        return BlockPos.containing(iota.get().vec3)
    }

    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "vector")
}

fun List<Iota>.getNumOrVec(idx: Int, argc: Int = 0): Either<Double, Vec3> {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val iota = x.tryCastToEither(DOUBLE, VEC3);

    if (iota.isPresent) {
        return iota.get().mapBoth(
            { it.double },
            { it.vec3 })
    }
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "numvec")
}

fun List<Iota>.getLongOrList(idx: Int, argc: Int = 0): Either<Long, SpellList> {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    val err = { MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "numlist") }

    val iota = x.tryCastTo(DOUBLE, LIST)
    if (iota.isPresent) {
        val datum = iota.get()
        return when (datum) {
            is DoubleIota -> run {
                val double = datum.double
                val rounded = double.roundToLong()
                if (abs(double - rounded) <= DoubleIota.TOLERANCE) {
                    Either.left(rounded)
                } else throw err()
            }

            is ListIota -> Either.right(datum.list)
            else -> throw err()

        }
    }
    throw err()
}

fun evaluatable(iota: Iota, reverseIdx: Int): Either<Iota, SpellList> {
    val x = iota.tryCastTo(LIST)
    return if (x.isPresent()) {
        Either.right(x.get().list)
    } else if (iota.executable()) {
        Either.left(iota)
    } else throw MishapInvalidIota(
        iota,
        reverseIdx,
        "hexcasting.mishap.invalid_value.evaluatable".asTranslatedComponent
    )
}

fun Iota?.orNull() = this ?: NullIota()

fun <A : Iota, B : Iota> Iota.tryCastToEither(a: IotaType<A>, b: IotaType<B>): Optional<Either<A, B>> {
    return this.tryCastTo(a).map<Either<A, B>?> { x -> Either.left(x) }
        .or { this.tryCastTo(b).map<Either<A, B>?> { x -> Either.right(x) } }
}

fun <A : Iota, B : Iota> Iota.tryCastTo(a: IotaType<A>, b: IotaType<B>): Optional<Iota> {
    return (this.tryCastTo(a) as Optional<Iota>).or { this.tryCastTo(b) }
}

// TODO do we make this work on lists
// there should probably be some way to abstract function application over lists, vecs, and numbers,
// and i bet it's fucking monads
fun aplKinnie(operatee: Either<Double, Vec3>, fn: DoubleUnaryOperator): Iota =
    operatee.map(
        { num -> DoubleIota(fn.applyAsDouble(num)) },
        { vec -> Vec3Iota(Vec3(fn.applyAsDouble(vec.x), fn.applyAsDouble(vec.y), fn.applyAsDouble(vec.z))) }
    )

inline val Boolean.asActionResult get() = listOf(BooleanIota(this))
inline val Double.asActionResult get() = listOf(DoubleIota(this))
inline val Number.asActionResult get() = listOf(DoubleIota(this.toDouble()))

inline val SpellList.asActionResult get() = listOf(ListIota(this))
inline val List<Iota>.asActionResult get() = listOf(ListIota(this))

inline val BlockPos.asActionResult get() = listOf(Vec3Iota(Vec3.atCenterOf(this)))
inline val Vector3f.asActionResult get() = listOf(Vec3Iota(Vec3(this)))
inline val Vec3.asActionResult get() = listOf(Vec3Iota(this))

inline val Entity?.asActionResult get() = listOf(if (this == null) NullIota() else EntityIota(this))
inline val HexPattern.asActionResult get() = listOf(PatternIota(this))
