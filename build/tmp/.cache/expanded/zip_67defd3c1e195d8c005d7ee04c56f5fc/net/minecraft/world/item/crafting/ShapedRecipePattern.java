package net.minecraft.world.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public final class ShapedRecipePattern {
    private static final int MAX_SIZE = 3;
    public static final MapCodec<ShapedRecipePattern> MAP_CODEC = ShapedRecipePattern.Data.MAP_CODEC
        .flatXmap(
            ShapedRecipePattern::unpack,
            p_341595_ -> p_341595_.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
        );
    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipePattern> STREAM_CODEC = StreamCodec.ofMember(
        ShapedRecipePattern::toNetwork, ShapedRecipePattern::fromNetwork
    );
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    private final Optional<ShapedRecipePattern.Data> data;
    private final int ingredientCount;
    private final boolean symmetrical;

    public ShapedRecipePattern(int pWidth, int pHeight, NonNullList<Ingredient> pIngredients, Optional<ShapedRecipePattern.Data> pData) {
        this.width = pWidth;
        this.height = pHeight;
        this.ingredients = pIngredients;
        this.data = pData;
        int i = 0;

        for (Ingredient ingredient : pIngredients) {
            if (!ingredient.isEmpty()) {
                i++;
            }
        }

        this.ingredientCount = i;
        this.symmetrical = Util.isSymmetrical(pWidth, pHeight, pIngredients);
    }

    public static ShapedRecipePattern of(Map<Character, Ingredient> pKey, String... pPattern) {
        return of(pKey, List.of(pPattern));
    }

    public static ShapedRecipePattern of(Map<Character, Ingredient> pKey, List<String> pPattern) {
        ShapedRecipePattern.Data shapedrecipepattern$data = new ShapedRecipePattern.Data(pKey, pPattern);
        return unpack(shapedrecipepattern$data).getOrThrow();
    }

    private static DataResult<ShapedRecipePattern> unpack(ShapedRecipePattern.Data p_312333_) {
        String[] astring = shrink(p_312333_.pattern);
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        CharSet charset = new CharArraySet(p_312333_.key.keySet());

        for (int k = 0; k < astring.length; k++) {
            String s = astring[k];

            for (int l = 0; l < s.length(); l++) {
                char c0 = s.charAt(l);
                Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : p_312333_.key.get(c0);
                if (ingredient == null) {
                    return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                }

                charset.remove(c0);
                nonnulllist.set(l + i * k, ingredient);
            }
        }

        return !charset.isEmpty()
            ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charset)
            : DataResult.success(new ShapedRecipePattern(i, j, nonnulllist, Optional.of(p_312333_)));
    }

    @VisibleForTesting
    static String[] shrink(List<String> pPattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < pPattern.size(); i1++) {
            String s = pPattern.get(i1);
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    k++;
                }

                l++;
            } else {
                l = 0;
            }
        }

        if (pPattern.size() == l) {
            return new String[0];
        } else {
            String[] astring = new String[pPattern.size() - l - k];

            for (int k1 = 0; k1 < astring.length; k1++) {
                astring[k1] = pPattern.get(k1 + k).substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String pRow) {
        int i = 0;

        while (i < pRow.length() && pRow.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    private static int lastNonSpace(String pRow) {
        int i = pRow.length() - 1;

        while (i >= 0 && pRow.charAt(i) == ' ') {
            i--;
        }

        return i;
    }

    public boolean matches(CraftingInput pInput) {
        if (pInput.ingredientCount() != this.ingredientCount) {
            return false;
        } else {
            if (pInput.width() == this.width && pInput.height() == this.height) {
                if (!this.symmetrical && this.matches(pInput, true)) {
                    return true;
                }

                if (this.matches(pInput, false)) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean matches(CraftingInput pInput, boolean pSymmetrical) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Ingredient ingredient;
                if (pSymmetrical) {
                    ingredient = this.ingredients.get(this.width - j - 1 + i * this.width);
                } else {
                    ingredient = this.ingredients.get(j + i * this.width);
                }

                ItemStack itemstack = pInput.getItem(j, i);
                if (!ingredient.test(itemstack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void toNetwork(RegistryFriendlyByteBuf p_335258_) {
        p_335258_.writeVarInt(this.width);
        p_335258_.writeVarInt(this.height);

        for (Ingredient ingredient : this.ingredients) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_335258_, ingredient);
        }
    }

    private static ShapedRecipePattern fromNetwork(RegistryFriendlyByteBuf p_332293_) {
        int i = p_332293_.readVarInt();
        int j = p_332293_.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        nonnulllist.replaceAll(p_327210_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(p_332293_));
        return new ShapedRecipePattern(i, j, nonnulllist, Optional.empty());
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public NonNullList<Ingredient> ingredients() {
        return this.ingredients;
    }

    public Optional<ShapedRecipePattern.Data> data() {
        return this.data;
    }

    public static record Data(Map<Character, Ingredient> key, List<String> pattern) {
        private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(p_311191_ -> {
            if (p_311191_.size() > ShapedRecipe.MAX_HEIGHT) {
                return DataResult.error(() -> "Invalid pattern: too many rows, " + ShapedRecipe.MAX_HEIGHT + " is maximum");
            } else if (p_311191_.isEmpty()) {
                return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
            } else {
                int i = p_311191_.getFirst().length();

                for (String s : p_311191_) {
                    if (s.length() > ShapedRecipe.MAX_HEIGHT) {
                        return DataResult.error(() -> "Invalid pattern: too many columns, " + ShapedRecipe.MAX_HEIGHT + " is maximum");
                    }

                    if (i != s.length()) {
                        return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                    }
                }

                return DataResult.success(p_311191_);
            }
        }, Function.identity());
        private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(p_313217_ -> {
            if (p_313217_.length() != 1) {
                return DataResult.error(() -> "Invalid key entry: '" + p_313217_ + "' is an invalid symbol (must be 1 character only).");
            } else {
                return " ".equals(p_313217_) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(p_313217_.charAt(0));
            }
        }, String::valueOf);
        public static final MapCodec<ShapedRecipePattern.Data> MAP_CODEC = RecordCodecBuilder.mapCodec(
            p_310577_ -> p_310577_.group(
                        ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(p_311797_ -> p_311797_.key),
                        PATTERN_CODEC.fieldOf("pattern").forGetter(p_309770_ -> p_309770_.pattern)
                    )
                    .apply(p_310577_, ShapedRecipePattern.Data::new)
        );
    }
}
