//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.qy.MomoTech.utils;

import cn.qy.MomoTech.Items.Items;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import lombok.AllArgsConstructor;
import me.matl114.matlib.implement.slimefun.menu.menuGroup.CustomMenuGroup;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final String[] mineral__ = {"DIAMOND_BLOCK", "NETHERITE_BLOCK", "COAL_BLOCK", "EMERALD_BLOCK",
            "QUARTZ_BLOCK", "REDSTONE_BLOCK", "IRON_BLOCK", "GOLD_BLOCK", "LAPIS_BLOCK"};
    public static final Material[] it = {Material.DIAMOND, Material.NETHERITE_INGOT, Material.COAL,
            Material.EMERALD, Material.QUARTZ, Material.REDSTONE,
            Material.IRON_INGOT, Material.GOLD_INGOT, Material.LAPIS_LAZULI};
    public static final String[] id = {"I", "II", "III"};

    public static boolean checkOutput(BlockMenu inv, int[] output) {
        for (int i : output) {
            if (inv.getItemInSlot(i) == null) return false;
        }
        return true;
    }

    public static List<String> getLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        return (lore == null ? List.of() : lore);
    }


    public static List<SlimefunItem> getRecipeByRecipeType(@Nonnull RecipeType recipeType) {
        List<SlimefunItem> list = Slimefun.getRegistry().getEnabledSlimefunItems();
        List<SlimefunItem> ans = new ArrayList<>(list.size());
        for (SlimefunItem slimefunItem : list) {
            if (!slimefunItem.isDisabled() && recipeType.equals(slimefunItem.getRecipeType())) {
                ans.add(slimefunItem);
            }
        }
        return ans;
    }
    @AllArgsConstructor
    public static class DelegateClickHandler implements ChestMenu.MenuClickHandler{
        ChestMenu.MenuClickHandler delegate;
        ChestMenu holder;
        ItemStack holdItemStack;
        static Random rand = new Random();
        @Override
        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
            int nextSlot;
            do{
                int row = i / 9;
                int column = i % 9;
                if(row <= 0 ){
                    row += rand.nextInt(0, 2);
                }else if(row >= 5){
                    row -= rand.nextInt(0, 2);;
                }else  {
                    row += rand.nextInt(-1, 2);
                }

                if(column <= 0 ){
                    column += rand.nextInt(0, 2);;
                }else if(column >= 8){
                    column -= rand.nextInt(0, 2);;
                }else  {
                    column += rand.nextInt(-1, 2);
                }
                nextSlot = row * 9 + column;
            }while (nextSlot == i);

            ChestMenu.MenuClickHandler handler = holder.getMenuClickHandler(nextSlot);
            DelegateClickHandler newHolder = new DelegateClickHandler(handler == null ? ChestMenuUtils.getEmptyClickHandler(): handler, holder, holder.getItemInSlot(nextSlot));
            holder.addMenuClickHandler(nextSlot, newHolder);
            holder.replaceExistingItem(nextSlot, holder.getItemInSlot(i));

            holder.addMenuClickHandler(i, delegate == null ? ChestMenuUtils.getEmptyClickHandler(): delegate);
            holder.replaceExistingItem(i, holdItemStack);
            return false;
        }
    }
    private static CustomMenuGroup.CustomMenuClickHandler uncontrollableVoid = (menu)-> new DelegateClickHandler(null, menu.getMenu(), null);

    public static CustomMenuGroup.CustomMenuClickHandler getUncontrollableVoidHandler(){
        return uncontrollableVoid;
    }
}
