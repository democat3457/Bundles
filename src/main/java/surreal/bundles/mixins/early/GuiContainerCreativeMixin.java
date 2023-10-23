package surreal.bundles.mixins.early;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import surreal.bundles.mixins.interfaces.IExtendedSlotClick;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiContainerCreative.class)
public abstract class GuiContainerCreativeMixin {

    @Redirect(method = "handleMouseClick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/inventory/Container;slotClick(IILnet/minecraft/inventory/ClickType;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"
    ))
    public ItemStack slotClick(Container container, int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        return ((IExtendedSlotClick) container).slotClick(slotId, dragType, clickTypeIn, GuiScreen.isCtrlKeyDown(), player);
    }
}
