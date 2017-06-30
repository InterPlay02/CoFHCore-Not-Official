package cofh.core.util.crafting;

import cofh.CoFHCore;
import cofh.api.item.INBTCopyIngredient;
import cofh.lib.util.helpers.ItemHelper;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class UpgradeRecipeFactory implements IRecipeFactory {

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {

		ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

		ShapedPrimer primer = new ShapedPrimer();
		primer.width = recipe.getWidth();
		primer.height = recipe.getHeight();
		primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
		primer.input = recipe.getIngredients();

		return new UpgradeRecipe(new ResourceLocation(CoFHCore.MOD_ID, "upgrade"), recipe.getRecipeOutput(), primer);
	}

	/* RECIPE */
	public static class UpgradeRecipe extends ShapedOreRecipe {

		public UpgradeRecipe(ResourceLocation group, ItemStack result, ShapedPrimer primer) {

			super(group, result, primer);
		}

		@Override
		@Nonnull
		public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {

			ItemStack inputStack = ItemStack.EMPTY;
			ItemStack outputStack = output.copy();

			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stack = inv.getStackInSlot(i);

				if (!stack.isEmpty()) {
					if (stack.getItem() instanceof INBTCopyIngredient) {
						inputStack = stack;
					} else if (Block.getBlockFromItem(stack.getItem()) instanceof INBTCopyIngredient) {
						inputStack = stack;
					}
				}
			}
			outputStack = ItemHelper.copyTag(outputStack, inputStack);
			return outputStack;
		}
	}

}
