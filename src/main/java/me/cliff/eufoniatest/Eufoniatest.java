package me.cliff.eufoniatest;

import me.cliff.eufoniatest.callbacks.onChestPlaceCallback;
import me.cliff.eufoniatest.utils.ChestData;
import me.cliff.eufoniatest.utils.IChestBlockDataSaver;
import me.cliff.eufoniatest.utils.ConfigManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;


public class Eufoniatest implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Eufonia_Test");

    @Override
    public void onInitializeServer() {

        ConfigManager.loadConfig();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("chestloot")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(literal("reload")
                                .executes(context -> {
                                    ConfigManager.loadConfig();
                                    context.getSource().sendFeedback(Text.literal("Reloaded Json loot"),false);
                                    return 1;
                                })
                        )
                ));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {

            // Logic for Chest Placing on survival
            if (player.getActiveItem().getItem().equals(Items.CHEST)) {
                onChestPlaceCallback.EVENT.register(((pos, state) -> {

                    if (player.isCreative() || player.isSpectator()) {
                        return ActionResult.PASS;
                    }

                    ChestData.setUsed((IChestBlockDataSaver) world.getBlockEntity(pos), true);

                    return ActionResult.SUCCESS;
                }));
            }

            if (!world.isClient) {
                BlockEntity block = world.getBlockEntity(hitResult.getBlockPos());

                if (block instanceof ChestBlockEntity) {

                    Identifier biome = world.getBiome(hitResult.getBlockPos()).getKey().get().getValue();

                    boolean used = ChestData.getUsed((IChestBlockDataSaver) block);

                    if (used){
                        return ActionResult.PASS;
                    }

                    Identifier lootT = new Identifier(ConfigManager.loot.getRandomLootTable(biome.toString()));
                    ((ChestBlockEntity) block).setLootTable(lootT,0);
                    ChestData.setUsed((IChestBlockDataSaver) block, true);
                    block.markDirty();
                    world.markDirty(block.getPos());
                }
            }
            return ActionResult.PASS;
        });



    }



}
