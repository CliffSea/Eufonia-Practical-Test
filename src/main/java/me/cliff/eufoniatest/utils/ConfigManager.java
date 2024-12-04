package me.cliff.eufoniatest.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.cliff.eufoniatest.Eufoniatest;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class ConfigManager {

    public static Loots loot = new Loots();

    public static String defaultConfig = "{\n    \"default\": [\n      \"\"\n    ],\n    \"minecraft:desert\": [\n      \"minecraft:chests/desert_pyramid\",\n      \"minecraft:chests/buried_treasure\",\n      \"minecraft:chests/simple_dungeon\"\n    ],\n    \"minecraft:jungle\": [\n      \"minecraft:chests/ancient_city\"\n    ]\n}";

    public static void loadConfig() {

        File mainDirec = new File(FabricLoader.getInstance().getGameDir().toFile(),"ChestJson");
        File data = new File(mainDirec, "config.json");

        if (!mainDirec.exists()){
            if (mainDirec.mkdir()){
                Eufoniatest.LOGGER.info("Directory created");
            }
        }
        if(!data.exists()){
            try {
                if (data.createNewFile()){
                    Eufoniatest.LOGGER.info("Config created");
                    Files.writeString(data.toPath(), defaultConfig);
                }
            }catch (IOException error){
                Eufoniatest.LOGGER.warn("Couldn't create the config file");
            }
        }

        try {

            byte[] jsondata = Files.readAllBytes(data.toPath());

            ObjectMapper objectMapper = new ObjectMapper();

            loot.setLootTables(objectMapper.readValue(jsondata, HashMap.class));

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
