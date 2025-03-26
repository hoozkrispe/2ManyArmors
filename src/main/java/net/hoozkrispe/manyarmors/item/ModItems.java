package net.hoozkrispe.manyarmors.item;

import net.hoozkrispe.manyarmors.ManyArmors;
import net.hoozkrispe.manyarmors.item.custom.ModArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ManyArmors.MOD_ID);

    public static final RegistryObject<Item> DIRT_HELMET = ITEMS.register("dirt_helmet",
            () -> new ModArmorItem(ModArmorMaterials.DIRT_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(1))));
    public static final RegistryObject<Item> DIRT_CHESTPLATE = ITEMS.register("dirt_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.DIRT_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(1))));
    public static final RegistryObject<Item> DIRT_LEGGINGS = ITEMS.register("dirt_leggings",
            () -> new ModArmorItem(ModArmorMaterials.DIRT_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(1))));
    public static final RegistryObject<Item> DIRT_BOOTS = ITEMS.register("dirt_boots",
            () -> new ModArmorItem(ModArmorMaterials.DIRT_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(1))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
