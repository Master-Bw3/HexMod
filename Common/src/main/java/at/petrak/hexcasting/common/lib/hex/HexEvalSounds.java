package at.petrak.hexcasting.common.lib.hex;

import at.petrak.hexcasting.api.spell.casting.sideeffects.EvalSound;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

import static at.petrak.hexcasting.api.HexAPI.modLoc;

public class HexEvalSounds {
    public static final Registry<EvalSound> REGISTRY = IXplatAbstractions.INSTANCE.getEvalSoundRegistry();

    private static final Map<ResourceLocation, EvalSound> TYPES = new LinkedHashMap<>();

    public static final EvalSound NOTHING = make("nothing",
        new EvalSound(null, Integer.MIN_VALUE));
    public static final EvalSound OPERATOR = make("operator",
        new EvalSound(HexSounds.ADD_PATTERN, 0));
    public static final EvalSound SPELL = make("spell",
        new EvalSound(HexSounds.ACTUALLY_CAST, 1000));
    public static final EvalSound MISHAP = make("mishap",
        new EvalSound(HexSounds.FAIL_PATTERN, Integer.MAX_VALUE));

    private static EvalSound make(String name, EvalSound sound) {
        var old = TYPES.put(modLoc(name), sound);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return sound;
    }
}