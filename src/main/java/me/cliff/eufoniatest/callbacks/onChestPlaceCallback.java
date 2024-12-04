package me.cliff.eufoniatest.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface onChestPlaceCallback {

    Event<onChestPlaceCallback> EVENT = EventFactory.createArrayBacked(onChestPlaceCallback.class,
            (listeners) ->(pos, state) -> {
                for (onChestPlaceCallback listener: listeners){
                    ActionResult result = listener.interact(pos, state);

                    if (result != ActionResult.PASS){
                        return result;
                    }
                }
                return  ActionResult.PASS;
            });

        ActionResult interact (BlockPos pos, BlockState state);

}
