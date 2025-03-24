package net.minecraft.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;

public class BlockTypes {
    public static final MapCodec<Block> CODEC = BuiltInRegistries.BLOCK_TYPE.byNameCodec().dispatchMap(Block::codec, Function.identity());

    public static MapCodec<? extends Block> bootstrap(Registry<MapCodec<? extends Block>> pRegistry) {
        Registry.register(pRegistry, "block", Block.CODEC);
        Registry.register(pRegistry, "air", AirBlock.CODEC);
        Registry.register(pRegistry, "amethyst", AmethystBlock.CODEC);
        Registry.register(pRegistry, "amethyst_cluster", AmethystClusterBlock.CODEC);
        Registry.register(pRegistry, "anvil", AnvilBlock.CODEC);
        Registry.register(pRegistry, "attached_stem", AttachedStemBlock.CODEC);
        Registry.register(pRegistry, "azalea", AzaleaBlock.CODEC);
        Registry.register(pRegistry, "bamboo_sapling", BambooSaplingBlock.CODEC);
        Registry.register(pRegistry, "bamboo_stalk", BambooStalkBlock.CODEC);
        Registry.register(pRegistry, "banner", BannerBlock.CODEC);
        Registry.register(pRegistry, "barrel", BarrelBlock.CODEC);
        Registry.register(pRegistry, "barrier", BarrierBlock.CODEC);
        Registry.register(pRegistry, "base_coral_fan", BaseCoralFanBlock.CODEC);
        Registry.register(pRegistry, "base_coral_plant", BaseCoralPlantBlock.CODEC);
        Registry.register(pRegistry, "base_coral_wall_fan", BaseCoralWallFanBlock.CODEC);
        Registry.register(pRegistry, "beacon", BeaconBlock.CODEC);
        Registry.register(pRegistry, "bed", BedBlock.CODEC);
        Registry.register(pRegistry, "beehive", BeehiveBlock.CODEC);
        Registry.register(pRegistry, "beetroot", BeetrootBlock.CODEC);
        Registry.register(pRegistry, "bell", BellBlock.CODEC);
        Registry.register(pRegistry, "big_dripleaf", BigDripleafBlock.CODEC);
        Registry.register(pRegistry, "big_dripleaf_stem", BigDripleafStemBlock.CODEC);
        Registry.register(pRegistry, "blast_furnace", BlastFurnaceBlock.CODEC);
        Registry.register(pRegistry, "brewing_stand", BrewingStandBlock.CODEC);
        Registry.register(pRegistry, "brushable", BrushableBlock.CODEC);
        Registry.register(pRegistry, "bubble_column", BubbleColumnBlock.CODEC);
        Registry.register(pRegistry, "budding_amethyst", BuddingAmethystBlock.CODEC);
        Registry.register(pRegistry, "button", ButtonBlock.CODEC);
        Registry.register(pRegistry, "cactus", CactusBlock.CODEC);
        Registry.register(pRegistry, "cake", CakeBlock.CODEC);
        Registry.register(pRegistry, "calibrated_sculk_sensor", CalibratedSculkSensorBlock.CODEC);
        Registry.register(pRegistry, "campfire", CampfireBlock.CODEC);
        Registry.register(pRegistry, "candle_cake", CandleCakeBlock.CODEC);
        Registry.register(pRegistry, "candle", CandleBlock.CODEC);
        Registry.register(pRegistry, "carpet", CarpetBlock.CODEC);
        Registry.register(pRegistry, "carrot", CarrotBlock.CODEC);
        Registry.register(pRegistry, "cartography_table", CartographyTableBlock.CODEC);
        Registry.register(pRegistry, "carved_pumpkin", EquipableCarvedPumpkinBlock.CODEC);
        Registry.register(pRegistry, "cauldron", CauldronBlock.CODEC);
        Registry.register(pRegistry, "cave_vines", CaveVinesBlock.CODEC);
        Registry.register(pRegistry, "cave_vines_plant", CaveVinesPlantBlock.CODEC);
        Registry.register(pRegistry, "ceiling_hanging_sign", CeilingHangingSignBlock.CODEC);
        Registry.register(pRegistry, "chain", ChainBlock.CODEC);
        Registry.register(pRegistry, "cherry_leaves", CherryLeavesBlock.CODEC);
        Registry.register(pRegistry, "chest", ChestBlock.CODEC);
        Registry.register(pRegistry, "chiseled_book_shelf", ChiseledBookShelfBlock.CODEC);
        Registry.register(pRegistry, "chorus_flower", ChorusFlowerBlock.CODEC);
        Registry.register(pRegistry, "chorus_plant", ChorusPlantBlock.CODEC);
        Registry.register(pRegistry, "cocoa", CocoaBlock.CODEC);
        Registry.register(pRegistry, "colored_falling", ColoredFallingBlock.CODEC);
        Registry.register(pRegistry, "command", CommandBlock.CODEC);
        Registry.register(pRegistry, "comparator", ComparatorBlock.CODEC);
        Registry.register(pRegistry, "composter", ComposterBlock.CODEC);
        Registry.register(pRegistry, "concrete_powder", ConcretePowderBlock.CODEC);
        Registry.register(pRegistry, "conduit", ConduitBlock.CODEC);
        Registry.register(pRegistry, "copper_bulb_block", CopperBulbBlock.CODEC);
        Registry.register(pRegistry, "coral", CoralBlock.CODEC);
        Registry.register(pRegistry, "coral_fan", CoralFanBlock.CODEC);
        Registry.register(pRegistry, "coral_plant", CoralPlantBlock.CODEC);
        Registry.register(pRegistry, "coral_wall_fan", CoralWallFanBlock.CODEC);
        Registry.register(pRegistry, "crafter", CrafterBlock.CODEC);
        Registry.register(pRegistry, "crafting_table", CraftingTableBlock.CODEC);
        Registry.register(pRegistry, "crop", CropBlock.CODEC);
        Registry.register(pRegistry, "crying_obsidian", CryingObsidianBlock.CODEC);
        Registry.register(pRegistry, "daylight_detector", DaylightDetectorBlock.CODEC);
        Registry.register(pRegistry, "dead_bush", DeadBushBlock.CODEC);
        Registry.register(pRegistry, "decorated_pot", DecoratedPotBlock.CODEC);
        Registry.register(pRegistry, "detector_rail", DetectorRailBlock.CODEC);
        Registry.register(pRegistry, "dirt_path", DirtPathBlock.CODEC);
        Registry.register(pRegistry, "dispenser", DispenserBlock.CODEC);
        Registry.register(pRegistry, "door", DoorBlock.CODEC);
        Registry.register(pRegistry, "double_plant", DoublePlantBlock.CODEC);
        Registry.register(pRegistry, "dragon_egg", DragonEggBlock.CODEC);
        Registry.register(pRegistry, "drop_experience", DropExperienceBlock.CODEC);
        Registry.register(pRegistry, "dropper", DropperBlock.CODEC);
        Registry.register(pRegistry, "enchantment_table", EnchantingTableBlock.CODEC);
        Registry.register(pRegistry, "ender_chest", EnderChestBlock.CODEC);
        Registry.register(pRegistry, "end_gateway", EndGatewayBlock.CODEC);
        Registry.register(pRegistry, "end_portal", EndPortalBlock.CODEC);
        Registry.register(pRegistry, "end_portal_frame", EndPortalFrameBlock.CODEC);
        Registry.register(pRegistry, "end_rod", EndRodBlock.CODEC);
        Registry.register(pRegistry, "farm", FarmBlock.CODEC);
        Registry.register(pRegistry, "fence", FenceBlock.CODEC);
        Registry.register(pRegistry, "fence_gate", FenceGateBlock.CODEC);
        Registry.register(pRegistry, "fire", FireBlock.CODEC);
        Registry.register(pRegistry, "fletching_table", FletchingTableBlock.CODEC);
        Registry.register(pRegistry, "flower", FlowerBlock.CODEC);
        Registry.register(pRegistry, "flower_pot", FlowerPotBlock.CODEC);
        Registry.register(pRegistry, "frogspawn", FrogspawnBlock.CODEC);
        Registry.register(pRegistry, "frosted_ice", FrostedIceBlock.CODEC);
        Registry.register(pRegistry, "fungus", FungusBlock.CODEC);
        Registry.register(pRegistry, "furnace", FurnaceBlock.CODEC);
        Registry.register(pRegistry, "glazed_terracotta", GlazedTerracottaBlock.CODEC);
        Registry.register(pRegistry, "glow_lichen", GlowLichenBlock.CODEC);
        Registry.register(pRegistry, "grass", GrassBlock.CODEC);
        Registry.register(pRegistry, "grindstone", GrindstoneBlock.CODEC);
        Registry.register(pRegistry, "half_transparent", HalfTransparentBlock.CODEC);
        Registry.register(pRegistry, "hanging_roots", HangingRootsBlock.CODEC);
        Registry.register(pRegistry, "hay", HayBlock.CODEC);
        Registry.register(pRegistry, "heavy_core", HeavyCoreBlock.CODEC);
        Registry.register(pRegistry, "honey", HoneyBlock.CODEC);
        Registry.register(pRegistry, "hopper", HopperBlock.CODEC);
        Registry.register(pRegistry, "huge_mushroom", HugeMushroomBlock.CODEC);
        Registry.register(pRegistry, "ice", IceBlock.CODEC);
        Registry.register(pRegistry, "infested", InfestedBlock.CODEC);
        Registry.register(pRegistry, "infested_rotated_pillar", InfestedRotatedPillarBlock.CODEC);
        Registry.register(pRegistry, "iron_bars", IronBarsBlock.CODEC);
        Registry.register(pRegistry, "jack_o_lantern", CarvedPumpkinBlock.CODEC);
        Registry.register(pRegistry, "jigsaw", JigsawBlock.CODEC);
        Registry.register(pRegistry, "jukebox", JukeboxBlock.CODEC);
        Registry.register(pRegistry, "kelp", KelpBlock.CODEC);
        Registry.register(pRegistry, "kelp_plant", KelpPlantBlock.CODEC);
        Registry.register(pRegistry, "ladder", LadderBlock.CODEC);
        Registry.register(pRegistry, "lantern", LanternBlock.CODEC);
        Registry.register(pRegistry, "lava_cauldron", LavaCauldronBlock.CODEC);
        Registry.register(pRegistry, "layered_cauldron", LayeredCauldronBlock.CODEC);
        Registry.register(pRegistry, "leaves", LeavesBlock.CODEC);
        Registry.register(pRegistry, "lectern", LecternBlock.CODEC);
        Registry.register(pRegistry, "lever", LeverBlock.CODEC);
        Registry.register(pRegistry, "light", LightBlock.CODEC);
        Registry.register(pRegistry, "lightning_rod", LightningRodBlock.CODEC);
        Registry.register(pRegistry, "liquid", LiquidBlock.CODEC);
        Registry.register(pRegistry, "loom", LoomBlock.CODEC);
        Registry.register(pRegistry, "magma", MagmaBlock.CODEC);
        Registry.register(pRegistry, "mangrove_leaves", MangroveLeavesBlock.CODEC);
        Registry.register(pRegistry, "mangrove_propagule", MangrovePropaguleBlock.CODEC);
        Registry.register(pRegistry, "mangrove_roots", MangroveRootsBlock.CODEC);
        Registry.register(pRegistry, "moss", MossBlock.CODEC);
        Registry.register(pRegistry, "moving_piston", MovingPistonBlock.CODEC);
        Registry.register(pRegistry, "mud", MudBlock.CODEC);
        Registry.register(pRegistry, "mushroom", MushroomBlock.CODEC);
        Registry.register(pRegistry, "mycelium", MyceliumBlock.CODEC);
        Registry.register(pRegistry, "nether_portal", NetherPortalBlock.CODEC);
        Registry.register(pRegistry, "netherrack", NetherrackBlock.CODEC);
        Registry.register(pRegistry, "nether_sprouts", NetherSproutsBlock.CODEC);
        Registry.register(pRegistry, "nether_wart", NetherWartBlock.CODEC);
        Registry.register(pRegistry, "note", NoteBlock.CODEC);
        Registry.register(pRegistry, "nylium", NyliumBlock.CODEC);
        Registry.register(pRegistry, "observer", ObserverBlock.CODEC);
        Registry.register(pRegistry, "piglinwallskull", PiglinWallSkullBlock.CODEC);
        Registry.register(pRegistry, "pink_petals", PinkPetalsBlock.CODEC);
        Registry.register(pRegistry, "piston_base", PistonBaseBlock.CODEC);
        Registry.register(pRegistry, "piston_head", PistonHeadBlock.CODEC);
        Registry.register(pRegistry, "pitcher_crop", PitcherCropBlock.CODEC);
        Registry.register(pRegistry, "player_head", PlayerHeadBlock.CODEC);
        Registry.register(pRegistry, "player_wall_head", PlayerWallHeadBlock.CODEC);
        Registry.register(pRegistry, "pointed_dripstone", PointedDripstoneBlock.CODEC);
        Registry.register(pRegistry, "potato", PotatoBlock.CODEC);
        Registry.register(pRegistry, "powder_snow", PowderSnowBlock.CODEC);
        Registry.register(pRegistry, "powered", PoweredBlock.CODEC);
        Registry.register(pRegistry, "powered_rail", PoweredRailBlock.CODEC);
        Registry.register(pRegistry, "pressure_plate", PressurePlateBlock.CODEC);
        Registry.register(pRegistry, "pumpkin", PumpkinBlock.CODEC);
        Registry.register(pRegistry, "rail", RailBlock.CODEC);
        Registry.register(pRegistry, "redstone_lamp", RedstoneLampBlock.CODEC);
        Registry.register(pRegistry, "redstone_ore", RedStoneOreBlock.CODEC);
        Registry.register(pRegistry, "redstone_torch", RedstoneTorchBlock.CODEC);
        Registry.register(pRegistry, "redstone_wall_torch", RedstoneWallTorchBlock.CODEC);
        Registry.register(pRegistry, "redstone_wire", RedStoneWireBlock.CODEC);
        Registry.register(pRegistry, "repeater", RepeaterBlock.CODEC);
        Registry.register(pRegistry, "respawn_anchor", RespawnAnchorBlock.CODEC);
        Registry.register(pRegistry, "rooted_dirt", RootedDirtBlock.CODEC);
        Registry.register(pRegistry, "roots", RootsBlock.CODEC);
        Registry.register(pRegistry, "rotated_pillar", RotatedPillarBlock.CODEC);
        Registry.register(pRegistry, "sapling", SaplingBlock.CODEC);
        Registry.register(pRegistry, "scaffolding", ScaffoldingBlock.CODEC);
        Registry.register(pRegistry, "sculk_catalyst", SculkCatalystBlock.CODEC);
        Registry.register(pRegistry, "sculk", SculkBlock.CODEC);
        Registry.register(pRegistry, "sculk_sensor", SculkSensorBlock.CODEC);
        Registry.register(pRegistry, "sculk_shrieker", SculkShriekerBlock.CODEC);
        Registry.register(pRegistry, "sculk_vein", SculkVeinBlock.CODEC);
        Registry.register(pRegistry, "seagrass", SeagrassBlock.CODEC);
        Registry.register(pRegistry, "sea_pickle", SeaPickleBlock.CODEC);
        Registry.register(pRegistry, "shulker_box", ShulkerBoxBlock.CODEC);
        Registry.register(pRegistry, "skull", SkullBlock.CODEC);
        Registry.register(pRegistry, "slab", SlabBlock.CODEC);
        Registry.register(pRegistry, "slime", SlimeBlock.CODEC);
        Registry.register(pRegistry, "small_dripleaf", SmallDripleafBlock.CODEC);
        Registry.register(pRegistry, "smithing_table", SmithingTableBlock.CODEC);
        Registry.register(pRegistry, "smoker", SmokerBlock.CODEC);
        Registry.register(pRegistry, "sniffer_egg", SnifferEggBlock.CODEC);
        Registry.register(pRegistry, "snow_layer", SnowLayerBlock.CODEC);
        Registry.register(pRegistry, "snowy_dirt", SnowyDirtBlock.CODEC);
        Registry.register(pRegistry, "soul_fire", SoulFireBlock.CODEC);
        Registry.register(pRegistry, "soul_sand", SoulSandBlock.CODEC);
        Registry.register(pRegistry, "spawner", SpawnerBlock.CODEC);
        Registry.register(pRegistry, "sponge", SpongeBlock.CODEC);
        Registry.register(pRegistry, "spore_blossom", SporeBlossomBlock.CODEC);
        Registry.register(pRegistry, "stained_glass_pane", StainedGlassPaneBlock.CODEC);
        Registry.register(pRegistry, "stained_glass", StainedGlassBlock.CODEC);
        Registry.register(pRegistry, "stair", StairBlock.CODEC);
        Registry.register(pRegistry, "standing_sign", StandingSignBlock.CODEC);
        Registry.register(pRegistry, "stem", StemBlock.CODEC);
        Registry.register(pRegistry, "stonecutter", StonecutterBlock.CODEC);
        Registry.register(pRegistry, "structure", StructureBlock.CODEC);
        Registry.register(pRegistry, "structure_void", StructureVoidBlock.CODEC);
        Registry.register(pRegistry, "sugar_cane", SugarCaneBlock.CODEC);
        Registry.register(pRegistry, "sweet_berry_bush", SweetBerryBushBlock.CODEC);
        Registry.register(pRegistry, "tall_flower", TallFlowerBlock.CODEC);
        Registry.register(pRegistry, "tall_grass", TallGrassBlock.CODEC);
        Registry.register(pRegistry, "tall_seagrass", TallSeagrassBlock.CODEC);
        Registry.register(pRegistry, "target", TargetBlock.CODEC);
        Registry.register(pRegistry, "tinted_glass", TintedGlassBlock.CODEC);
        Registry.register(pRegistry, "tnt", TntBlock.CODEC);
        Registry.register(pRegistry, "torchflower_crop", TorchflowerCropBlock.CODEC);
        Registry.register(pRegistry, "torch", TorchBlock.CODEC);
        Registry.register(pRegistry, "transparent", TransparentBlock.CODEC);
        Registry.register(pRegistry, "trapdoor", TrapDoorBlock.CODEC);
        Registry.register(pRegistry, "trapped_chest", TrappedChestBlock.CODEC);
        Registry.register(pRegistry, "trial_spawner", TrialSpawnerBlock.CODEC);
        Registry.register(pRegistry, "trip_wire_hook", TripWireHookBlock.CODEC);
        Registry.register(pRegistry, "tripwire", TripWireBlock.CODEC);
        Registry.register(pRegistry, "turtle_egg", TurtleEggBlock.CODEC);
        Registry.register(pRegistry, "twisting_vines_plant", TwistingVinesPlantBlock.CODEC);
        Registry.register(pRegistry, "twisting_vines", TwistingVinesBlock.CODEC);
        Registry.register(pRegistry, "vault", VaultBlock.CODEC);
        Registry.register(pRegistry, "vine", VineBlock.CODEC);
        Registry.register(pRegistry, "wall_banner", WallBannerBlock.CODEC);
        Registry.register(pRegistry, "wall_hanging_sign", WallHangingSignBlock.CODEC);
        Registry.register(pRegistry, "wall_sign", WallSignBlock.CODEC);
        Registry.register(pRegistry, "wall_skull", WallSkullBlock.CODEC);
        Registry.register(pRegistry, "wall_torch", WallTorchBlock.CODEC);
        Registry.register(pRegistry, "wall", WallBlock.CODEC);
        Registry.register(pRegistry, "waterlily", WaterlilyBlock.CODEC);
        Registry.register(pRegistry, "waterlogged_transparent", WaterloggedTransparentBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_bulb", WeatheringCopperBulbBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_door", WeatheringCopperDoorBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_full", WeatheringCopperFullBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_grate", WeatheringCopperGrateBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_slab", WeatheringCopperSlabBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_stair", WeatheringCopperStairBlock.CODEC);
        Registry.register(pRegistry, "weathering_copper_trap_door", WeatheringCopperTrapDoorBlock.CODEC);
        Registry.register(pRegistry, "web", WebBlock.CODEC);
        Registry.register(pRegistry, "weeping_vines_plant", WeepingVinesPlantBlock.CODEC);
        Registry.register(pRegistry, "weeping_vines", WeepingVinesBlock.CODEC);
        Registry.register(pRegistry, "weighted_pressure_plate", WeightedPressurePlateBlock.CODEC);
        Registry.register(pRegistry, "wet_sponge", WetSpongeBlock.CODEC);
        Registry.register(pRegistry, "wither_rose", WitherRoseBlock.CODEC);
        Registry.register(pRegistry, "wither_skull", WitherSkullBlock.CODEC);
        Registry.register(pRegistry, "wither_wall_skull", WitherWallSkullBlock.CODEC);
        return Registry.register(pRegistry, "wool_carpet", WoolCarpetBlock.CODEC);
    }
}