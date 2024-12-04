package me.cliff.eufoniatest.mixin;


import me.cliff.eufoniatest.utils.IChestBlockDataSaver;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin implements IChestBlockDataSaver {


    private  NbtCompound data;

    @Override
    public NbtCompound getData(){
        if (this.data == null){
            this.data = new NbtCompound();
        }
        return data;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void writeNbt(NbtCompound nbt, CallbackInfo ci){
        if (data != null){
            nbt.put("eufonia.data",data);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci){
        if (nbt.contains("eufonia.data")){
            data = nbt.getCompound("eufonia.data");
        }
    }

}
