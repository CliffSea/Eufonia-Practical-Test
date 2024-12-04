package me.cliff.eufoniatest.utils;

import net.minecraft.nbt.NbtCompound;

public class ChestData {

    public static void setUsed(IChestBlockDataSaver chest, boolean u){
        NbtCompound nbt = chest.getData();

        nbt.putBoolean("used", u);
    }

    public static Boolean getUsed(IChestBlockDataSaver chest){
        NbtCompound nbt = chest.getData();

        return nbt.getBoolean("used");
    }
}
