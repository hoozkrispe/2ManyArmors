package net.hoozkrispe.manyarmors.item;

import net.hoozkrispe.manyarmors.ManyArmors;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ManyArmors.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MANY_ARMORS_TAB = CREATIVE_MODE_TABS.register("many_armors_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DIRT_CHESTPLATE.get()))
                    .title(Component.translatable("creativetab.manyarmors.many_armors_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DIRT_HELMET.get());
                        output.accept(ModItems.DIRT_CHESTPLATE.get());
                        output.accept(ModItems.DIRT_LEGGINGS.get());
                        output.accept(ModItems.DIRT_BOOTS.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
