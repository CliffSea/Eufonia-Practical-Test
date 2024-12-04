package me.cliff.eufoniatest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Loots {

    private HashMap<String, List<String>> lootTables = new HashMap<String, List<String>>();

    public HashMap<String, List<String>> getLootTables() {
        return lootTables;
    }

    public void setLootTables(HashMap<String, List<String>> lootTables) {
        this.lootTables = lootTables;
    }

    public String getRandomLootTable(String biome){
        List<String> lootTable;
        if (lootTables.get(biome) == null) lootTable = lootTables.get("default"); else lootTable = lootTables.get(biome);

        if (lootTable != null && !lootTable.isEmpty()){
            int randomInt = new Random().nextInt(lootTable.size());
            return lootTable.get(randomInt);
        }
        return null;
    }

}