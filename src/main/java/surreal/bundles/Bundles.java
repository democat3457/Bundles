package surreal.bundles;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import surreal.bundles.items.ItemBundle;
import surreal.bundles.recipes.RecipeBundleColoring;

import java.util.Objects;
import java.util.Set;

@Mod(modid = Bundles.MODID, name = "Bundles", version = Tags.VERSION, dependencies = "required-after:mixinbooter@[4.2,);after:mousetweaks@[3.0,)")
public class Bundles {

    public static final String MODID = "bundles";

    public static ItemBundle BUNDLE = registerItem("bundle", new ItemBundle());

    // Config Cache
    private static Set<ItemStack> itemSet = null;

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new surreal.bundles.events.TooltipEvent());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        itemSet = ModConfig.getItemList();
    }

    // Config
    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID) && itemSet == null) {
            itemSet = ModConfig.getItemList();
        }
    }

    public static boolean canPutItem(ItemStack stack) {
        boolean blacklist = ModConfig.isBlackList != itemSet.contains(stack);
        boolean tools = ModConfig.allowTools || !stack.isItemStackDamageable() || !(stack.getItem() instanceof ItemTool);
        boolean storage = ModConfig.allowStorageItems || (!stack.hasTagCompound() || !hasAnyKeys(Objects.requireNonNull(stack.getTagCompound()), "BlockEntityTag", "Items"));
        return blacklist && tools && storage;
    }

    private static boolean hasAnyKeys(NBTTagCompound tag, String... keys) {
        for (String str : keys) {
            if (tag.hasKey(str)) return true;
        }

        return false;
    }

    // Registry
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(BUNDLE);
    }

    @SubscribeEvent
    public void registerRecipe(RegistryEvent.Register<IRecipe> event) {
        ItemStack string = new ItemStack(Items.STRING);
        ItemStack hide = new ItemStack(Items.RABBIT_HIDE);

        GameRegistry.addShapedRecipe(BUNDLE.getRegistryName(), null, new ItemStack(BUNDLE), "ABA", "B B", "BBB", 'A', string, 'B', hide);

        if (!ModConfig.allowCustomColors) {
            for (int i = 0; i < 16; i++) {
                EnumDyeColor color = EnumDyeColor.byDyeDamage(i);
                ItemStack stack = new ItemStack(BUNDLE);
                ItemBundle.setColor(stack, color);
                GameRegistry.addShapelessRecipe(new ResourceLocation(BUNDLE.getRegistryName() + "_" + color.getName()), null, stack, Ingredient.fromItem(BUNDLE), Ingredient.fromStacks(new ItemStack(Items.DYE, 1, color.getDyeDamage())));
            }
        } else event.getRegistry().register(new RecipeBundleColoring().setRegistryName(MODID, "bundle_coloring"));
    }

    @SubscribeEvent
    public void registerSound(RegistryEvent.Register<SoundEvent> event) {
        ModSounds.init();
    }

    private static <T extends Item> T registerItem(String name, T item) {
        item.setRegistryName(MODID, name).setTranslationKey(MODID + "." + name);
        return item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(BUNDLE, 0, new ModelResourceLocation(new ResourceLocation(MODID, "bundle"), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerItemColors(ColorHandlerEvent.Item event) {
        IItemColor BUNDLE_COLOR = (stack, tintIndex) -> {
            if (tintIndex == 0) {
                return ItemBundle.getColor(stack);
            }

            return 0xFFFFFF;
        };
        event.getItemColors().registerItemColorHandler(BUNDLE_COLOR, BUNDLE);
    }
}
