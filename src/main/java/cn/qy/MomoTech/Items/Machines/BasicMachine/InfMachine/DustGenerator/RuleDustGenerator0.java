package cn.qy.MomoTech.Items.Machines.BasicMachine.InfMachine.DustGenerator;

import cn.qy.MomoTech.GUI.AbstractEasyGeneratorGUI;
import cn.qy.MomoTech.GUI.RuleDustGenerator;
import cn.qy.MomoTech.utils.Maths;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.inventory.ItemStack;

public class RuleDustGenerator0 extends RuleDustGenerator {
    public RuleDustGenerator0(ItemGroup itemGroup, String id, ItemStack it, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, id, it, recipeType, recipe);
    }
    ItemStack output=new AbstractEasyGeneratorGUI.RandomizedItemStack(64,SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST,
            SlimefunItems.GOLD_DUST, SlimefunItems.LEAD_DUST, SlimefunItems.IRON_DUST, SlimefunItems.SILVER_DUST,
            SlimefunItems.TIN_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.MAGNESIUM_DUST);
    @Override
    protected ItemStack getItems() {
        return output;
    }
}
