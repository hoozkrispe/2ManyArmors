/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

/**
 * Forge reimplementation of vanilla's {@link ItemModelGenerator}, i.e. builtin/generated models with some tweaks:
 * - Represented as {@link IUnbakedGeometry} so it can be baked as usual instead of being special-cased
 * - Not limited to an arbitrary number of layers (5)
 * - Support for per-layer render types
 */
public class ItemLayerModel implements IUnbakedGeometry<ItemLayerModel>
{
    @Nullable
    private ImmutableList<Material> textures;
    private final Int2ObjectMap<ResourceLocation> renderTypeNames;
    private final Int2ObjectMap<ResourceLocation> renderTypeFastNames;

    private ItemLayerModel(@Nullable ImmutableList<Material> textures, Int2ObjectMap<ResourceLocation> renderTypeNames)
    {
        this(textures, renderTypeNames, new Int2ObjectOpenHashMap<>());
    }

    private ItemLayerModel(@Nullable ImmutableList<Material> textures, Int2ObjectMap<ResourceLocation> renderTypeNames, Int2ObjectMap<ResourceLocation> renderTypeFastNames) {
        this.textures = textures;
        this.renderTypeNames = renderTypeNames;
        this.renderTypeFastNames = renderTypeFastNames;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides)
    {
        if (textures == null)
        {
            ImmutableList.Builder<Material> builder = ImmutableList.builder();
            for (int i = 0; context.hasMaterial("layer" + i); i++)
            {
                builder.add(context.getMaterial("layer" + i));
            }
            textures = builder.build();
        }

        TextureAtlasSprite particle = spriteGetter.apply(
                context.hasMaterial("particle") ? context.getMaterial("particle") : textures.get(0)
        );
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);

        var normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
        CompositeModel.Baked.Builder builder = CompositeModel.Baked.builder(context, particle, overrides, context.getTransforms());
        for (int i = 0; i < textures.size(); i++)
        {
            TextureAtlasSprite sprite = spriteGetter.apply(textures.get(i));
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(i, sprite.contents());
            var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState);
            var renderTypeName = renderTypeNames.get(i);
            var renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : null;
            var renderTypeFastName = renderTypeFastNames.get(i);
            var renderTypesFast = renderTypeFastName != null ? context.getRenderType(renderTypeFastName) : null;
            builder.addQuads(renderTypes != null ? renderTypes : normalRenderTypes, renderTypesFast != null ? renderTypesFast : RenderTypeGroup.EMPTY, quads);
        }

        return builder.build();
    }

    public static final class Loader implements IGeometryLoader<ItemLayerModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public ItemLayerModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
        {
            var renderTypeNames = new Int2ObjectOpenHashMap<ResourceLocation>();
            if (jsonObject.has("render_types"))
            {
                var renderTypes = jsonObject.getAsJsonObject("render_types");
                for (Map.Entry<String, JsonElement> entry : renderTypes.entrySet())
                {
                    var renderType = ResourceLocation.parse(entry.getKey());
                    for (var layer : entry.getValue().getAsJsonArray())
                        if (renderTypeNames.put(layer.getAsInt(), renderType) != null)
                            throw new JsonParseException("Registered duplicate render type for layer " + layer);
                }
            }

            var renderTypeFastNames = new Int2ObjectOpenHashMap<ResourceLocation>();
            if (jsonObject.has("render_types_fast")) {
                var renderTypes = jsonObject.getAsJsonObject("render_types_fast");
                for (var entry : renderTypes.entrySet()) {
                    var renderType = ResourceLocation.parse(entry.getKey());
                    for (var layer : entry.getValue().getAsJsonArray())
                        if (renderTypeFastNames.put(layer.getAsInt(), renderType) != null)
                            throw new JsonParseException("Registered duplicate render type for layer " + layer);
                }
            }

            return new ItemLayerModel(null, renderTypeNames, renderTypeFastNames);
        }

        protected void readLayerData(JsonObject jsonObject, String name, Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames, Int2ObjectMap<ForgeFaceData> layerData, boolean logWarning)
        {
            this.readLayerData(jsonObject, name, renderTypeNames, new Int2ObjectOpenHashMap<>(), layerData, logWarning);
        }

        @Deprecated(forRemoval = true, since = "1.21.4")
        protected void readLayerData(JsonObject jsonObject, String name, Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames, Int2ObjectOpenHashMap<ResourceLocation> renderTypeFastNames, Int2ObjectMap<ForgeFaceData> layerData, boolean logWarning)
        {
            if (!jsonObject.has(name))
            {
                return;
            }
            var fullbrightLayers = jsonObject.getAsJsonObject(name);
            for (var entry : fullbrightLayers.entrySet())
            {
                int layer = Integer.parseInt(entry.getKey());
                var data = ForgeFaceData.read(entry.getValue(), ForgeFaceData.DEFAULT);
                layerData.put(layer, data);
            }
        }
    }
}
