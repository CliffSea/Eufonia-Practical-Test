# Eufonia Practical Test

A little server side mod that connect loot Tables to chest on open

# Features

- An easy-to-use config file located on the root of the server in ChestJson/config.json
  - The mod uses the minecraft nbt serialization to save if the player have already open the chest so the loot is not generated twice
  - Also use the same nbt if a player in survival places a chest so they can't exploit it
  - The mod have a command build in to reload the config file in game

# Config file Example

```json
{
  "default": [
    ""
  ],
  "minecraft:desert": [
    "minecraft:chests/desert_pyramid",
    "minecraft:chests/buried_treasure",
    "minecraft:chests/simple_dungeon"
  ],
  "minecraft:jungle": [
    "minecraft:chests/ancient_city"
  ]
}
```
# Breakthrough Performance

I had some issues in how to implement the method to add the nbt tag on survival place because of the performance impact

1. my first though on how to do it was to check every tick for block places, for one user it could be fine but for a big server this option was quickly discarded
   2. after discarding that i though what if i add a mixin on the chest block spawn or something like that but after a bit of trying I realize that would trigger even when a chest spawned normally on generation so discarded too
   3. the final one was probably the obvious make a custom fabricCallBackEvent on the BlockUpdateS2CPacket, why? because it triggers only when a client places a block it sends a Blockupdate packet to the server that we can listen but this can impact performance?  

   - not much we are only listening to an already existing packet and in the first line if the state (it holds the block type) checks if the block is an instance of ChestBlock so we can return early if it's not a chest
````java
 @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
    public void BlockUpdateS2CPacket(BlockPos pos, BlockState state, CallbackInfo ci) {
    if (!(state.getBlock() instanceof ChestBlock)) return;
    // rest of the code
}
````
- funny thing i thought this would be enough but after using a profile just to be sure I realize the Callback was creating a lot of objects even if it wasn't a chest
  - just by holding left click with anything on hand in like 2 minutes i reach 2k objects on memory using 5kb of ram i know not a lot but an issue nevertheless 
  - the fix was really easy actually just check before the callback if the player was holding a chest and returning more early in the creative check
````java
public void onInitializeServer() {

    UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
        
        if (player.getActiveItem().getItem().equals(Items.CHEST)) {
            onChestPlaceCallback.EVENT.register(((pos, state) -> {

                if (player.isCreative() || player.isSpectator()) {
                    return ActionResult.PASS;
                }

                ChestData.setUsed((IChestBlockDataSaver) world.getBlockEntity(pos), true);

                return ActionResult.SUCCESS;
            }));
        }
        
        // rest of the code
    });
}
````
- With this fix the objects stays on 1 with a one player, and we don't reach more :D

# Breakthrough NBT

Working with NBT data for the first time was kinda fun and pain there's not a lot of info on the fabric wiki on how to manipulate the serialization but after messing around a bit I figured out

The only problem I had with nbt was kinda funny after a bit when I got the nbt tag to work I was like "FINALLY YEAHHH" but after restarting the server the data on the chest was wiped, turns out if we edit the data of a block with mods minecraft usually don't know it happened so it doesn't save it in the chuck.

This was a easy fix too, just make the block dirty in other words tagged so minecraft knows it need to save the new data in the next chuck auto save
````java
     block.markDirty();
     world.markDirty(block.getPos());
````

And done! hope you liked this lil breakthrough