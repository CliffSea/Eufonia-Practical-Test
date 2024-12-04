package me.cliff.eufoniatest.mixin;

import me.cliff.eufoniatest.callbacks.onChestPlaceCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockUpdateS2CPacket.class)
public class BlockUpdateS2CPacketMixin {


    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
    public void BlockUpdateS2CPacket(BlockPos pos, BlockState state, CallbackInfo ci){
        if (!(state.getBlock() instanceof ChestBlock)) return;
        ActionResult result = onChestPlaceCallback.EVENT.invoker().interact(pos, state);

        if (result == ActionResult.FAIL){
            return;
        }
    }

}

