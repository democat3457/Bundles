package surreal.bundles.mixins.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public interface IExtendedSlotClick {

    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, boolean ctrlHeld, EntityPlayer player);
}