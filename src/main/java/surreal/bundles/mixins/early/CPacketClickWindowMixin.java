package surreal.bundles.mixins.early;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClickWindow;
import surreal.bundles.mixins.interfaces.IPacketClickCtrlHeld;

@Mixin(CPacketClickWindow.class)
public abstract class CPacketClickWindowMixin implements Packet<INetHandlerPlayServer>, IPacketClickCtrlHeld {

    @Unique public boolean ctrlHeld = false;

    @Unique
    public boolean getCtrlHeld() {
        return this.ctrlHeld;
    }

    @Unique
    public CPacketClickWindowMixin setCtrlHeld(boolean value) {
        this.ctrlHeld = value;
        return this;
    }
}
