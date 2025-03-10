package se.mickelus.tetra.blocks.workbench;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import se.mickelus.tetra.network.BlockPosPacket;
import se.mickelus.tetra.util.CastOptional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TweakWorkbenchPacket extends BlockPosPacket {

    String slot;
    Map<String, Integer> tweaks;

    public TweakWorkbenchPacket() {
        tweaks = new HashMap<>();
    }

    public TweakWorkbenchPacket(BlockPos pos, String slot, Map<String, Integer> tweaks) {
        super(pos);

        this.slot = slot;
        this.tweaks = tweaks;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);

        try {
            writeString(slot, buffer);
        } catch (IOException e) {
            System.err.println("An error occurred when writing tweak packet to buffer");
        }
        buffer.writeInt(tweaks.size());
        tweaks.forEach((tweakKey, step) -> {
            try {
                writeString(tweakKey, buffer);
                buffer.writeInt(step);
            } catch (IOException e) {
                System.err.println("An error occurred when writing tweak packet to buffer");
            }
        });
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);

        try {
            slot = readString(buffer);
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                    tweaks.put(readString(buffer), buffer.readInt());
            }
        } catch (IOException e) {
            System.err.println("An error occurred when reading tweak packet from buffer");
        }
    }

    @Override
    public void handle(EntityPlayer player) {
        CastOptional.cast(player.world.getTileEntity(pos), TileEntityWorkbench.class)
                .ifPresent(workbench -> workbench.tweak(player, slot, tweaks));
    }
}
