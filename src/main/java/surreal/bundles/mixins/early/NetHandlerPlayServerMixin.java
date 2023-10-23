package surreal.bundles.mixins.early;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.ITickable;
import surreal.bundles.mixins.interfaces.IExtendedSlotClick;
import surreal.bundles.mixins.interfaces.IPacketClickCtrlHeld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerPlayServer.class)
public abstract class NetHandlerPlayServerMixin implements INetHandlerPlayServer, ITickable {

    @Shadow public EntityPlayerMP player;

    @Redirect(method = "processClickWindow", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/inventory/Container;slotClick(IILnet/minecraft/inventory/ClickType;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"
    ))
    public ItemStack slotClick(Container container, int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, CPacketClickWindow packetIn) {
        return ((IExtendedSlotClick) container).slotClick(slotId, dragType, clickTypeIn, ((IPacketClickCtrlHeld) packetIn).getCtrlHeld(), player);
    }
}
