package cn.qy.MomoTech.Items.Machines.BasicMachine.InfMachine;

import cn.qy.MomoTech.GUI.AbstractElectricGUI;
import cn.qy.MomoTech.utils.MachineUtils;
import cn.qy.MomoTech.utils.Utils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UGenerator extends AbstractElectricGUI implements RecipeDisplayItem {
    public UGenerator(ItemGroup itemGroup, String id, ItemStack it, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, new SlimefunItemStack(id, it), recipeType, recipe);
    }

    @Override
    public int[] IN() {
        return new int[]{0, 2};
    }

    @Override
    public int[] OUT() {
        return new int[]{3, 8};
    }

    @Override
    public int[] EMPTY() {
        return new int[]{};
    }

    @NotNull
    @Override
    public String getMachineIdentifier() {
        return "MOMOTECH_U_GENERATOR";
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{1};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{4, 5, 6, 7};
    }

    @Override
    public int getCapacity() {
        return 100000;
    }

    @Override
    public int getEnergyConsumption() {
        return 5000;
    }
    ItemStack consumed= ItemStackWrapper.wrap( new ItemStack(Material.COBBLESTONE));
    ItemStack output=new CustomItemStack(SlimefunItems.URANIUM,64);
    @Override
    protected boolean findNextRecipe(BlockMenu inv) {
        return MachineUtils.simpleProcessor(inv,getInputSlots(),getOutputSlots(), consumed ,output);
//        if (Utils.checkOutput(inv, getOutputSlots())) return false;
//        for (int i : getInputSlots()) {
//            if (inv.getItemInSlot(i) != null) {
//                ItemStack it = inv.getItemInSlot(i);
//
//                if (it.getType()==Material.COBBLESTONE&&!it.hasItemMeta()) {
//                    inv.consumeItem(i, 1);
//                    inv.pushItem(new SlimefunItemStack(SlimefunItems.URANIUM, 64), getOutputSlots());
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> it = new ArrayList<>(4);
        it.add(new ItemStack(Material.COBBLESTONE));
        it.add(new SlimefunItemStack(SlimefunItems.URANIUM, 64));
        return it;
    }
}
