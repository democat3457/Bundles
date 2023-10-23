package surreal.bundles.mixins.early;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import surreal.bundles.mixins.interfaces.IExtendedSlotClick;
import surreal.bundles.mixins.interfaces.IPacketClickCtrlHeld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {

    @Redirect(method = "windowClick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/inventory/Container;slotClick(IILnet/minecraft/inventory/ClickType;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"
    ))
    public ItemStack slotClick(Container container, int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        return ((IExtendedSlotClick) container).slotClick(slotId, dragType, clickTypeIn, GuiScreen.isCtrlKeyDown(), player);
    }

    @Redirect(method = "windowClick", at = @At(
        value = "NEW",
        target = "(IIILnet/minecraft/inventory/ClickType;Lnet/minecraft/item/ItemStack;S)Lnet/minecraft/network/play/client/CPacketClickWindow;"
    ))
    public CPacketClickWindow constructCPacketClickWindow(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn) {
        CPacketClickWindow packet = new CPacketClickWindow(windowIdIn, slotIdIn, usedButtonIn, modeIn, clickedItemIn, actionNumberIn);
        ((IPacketClickCtrlHeld) packet).setCtrlHeld(GuiScreen.isCtrlKeyDown());
        return packet;
    }
}
