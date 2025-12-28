package cn.qy.MomoTech.Items.Machines.BasicMachine.Machine;

import cn.qy.MomoTech.Exceptions;
import cn.qy.MomoTech.GUI.AbstractGUI;
import cn.qy.MomoTech.Items.Items;
import cn.qy.MomoTech.Items.MomotechItem;
import cn.qy.MomoTech.utils.NumberCombinator;
import cn.qy.MomoTech.utils.Utils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.matl114.matlib.utils.CraftUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdinaryNumberCombinator extends AbstractGUI implements RecipeDisplayItem {
    public OrdinaryNumberCombinator(ItemGroup itemGroup, String id, ItemStack it, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, new SlimefunItemStack(id, it), recipeType, recipe);
    }

//    public static boolean check(ItemStack it) {
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_ADDITION, false, false)) return true;
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_DIVISION, false, false)) return true;
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_MULTIPLICATION, false, false)) return true;
//        return SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_SUBTRACTION, false, false);
//    }

    public static String check1(ItemStack it) {
        if (it == null || it.getType().isAir()) return null;
        String id = Slimefun.getItemDataService().getItemData(it).orElse(null);
        if (id == null) return null;
        return switch (id){
            case "MOMOTECH_SYMBOL_ADDITION" -> "+";
            case "MOMOTECH_SYMBOL_DIVISION" -> "/";
            case "MOMOTECH_SYMBOL_MULTIPLICATION" -> "*";
            case "MOMOTECH_SYMBOL_SUBTRACTION" -> "-";
            default -> null;
        };
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_ADDITION, false, false)) return "+";
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_DIVISION, false, false)) return "/";
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_MULTIPLICATION, false, false)) return "*";
//        if (SlimefunUtils.isItemSimilar(it, Items.MOMOTECH_SYMBOL_SUBTRACTION, false, false)) return "-";
//        return null;
    }

    @NotNull
    @Override
    public String getMachineIdentifier() {
        return "MOMOTECH_ORDINARY_NUMBER_COMBINATOR";
    }

    @Override
    public int[] IN() {
        return new int[]{0, 1, 2, 3, 4, 9, 13, 18, 19, 20, 21, 22};
    }

    @Override
    public int[] OUT() {
        return new int[]{6, 7, 8, 15, 17, 24, 25, 26};
    }

    @Override
    public int[] EMPTY() {
        return new int[]{5, 14, 23};
    }

    public int[] getInputSlots() {
        return new int[]{10, 11, 12};
    }

    public int[] getOutputSlots() {
        return new int[]{16};
    }

    private ItemStack out(String a, String b, String c) {
        double A = Double.parseDouble(a);
        double B = Double.parseDouble(b);
        return MomotechItem.resultDigital(NumberCombinator.Ordinary(A, B, c));
    }

    protected void findNextRecipe(BlockMenu inv) {
        if(inv.getItemInSlot(16)!=null){
            return;
        }
        ItemStack it1 = inv.getItemInSlot(getInputSlots()[0]),
                it2 = inv.getItemInSlot(getInputSlots()[1]),
                it3 = inv.getItemInSlot(getInputSlots()[2]);
        if(it1==null || it2==null || it3==null){
            return;
        }
        if ("MOMOTECH_DIGITAL".equals(Slimefun.getItemDataService().getItemData(it1).orElse(null)))
            if ("MOMOTECH_DIGITAL".equals(Slimefun.getItemDataService().getItemData(it3).orElse(null))){
                String symbol=check1(it2);
                if (symbol!=null) {
                    ItemMeta meta1 = it1.getItemMeta(), meta2 = it3.getItemMeta();
                    List<String> lore1 = Utils.getLore(meta1);
                    List<String> lore2 = Utils.getLore(meta2);
                    if (lore1.isEmpty() || lore2.isEmpty()) return;
                    String a = lore1.get(0);
                    a = a.substring(a.indexOf("f") + 1);
                    String b = lore2.get(0);
                    b = b.substring(b.indexOf("f") + 1);
                    ItemStack ans = out(a, b ,symbol);
                    it1.setAmount(it1.getAmount()-1);
                    it2.setAmount(it2.getAmount()-1);
                    it3.setAmount(it3.getAmount()-1);
                    inv.replaceExistingItem(16,ans);
                    //inv.pushItem(ans.clone(), 16);
                }
            }
    }

    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> it = new ArrayList<>(16);
        it.add(new CustomItemStack(Material.BOOK, "&f输入运算式进行简单地四则运算"));
        it.add(MomotechItem.digital(0.0));
        it.add(new CustomItemStack(Material.BOOK, "&f当输入的算式不合法的时候输出BUG - 运算式"));
        it.add(MomotechItem.bug.clone());
        it.add(new CustomItemStack(Material.BOOK, "&f当计算结果小数位数超过4位的时候输出BUG - 小数"));
        it.add(MomotechItem.bug1.clone());
        it.add(new CustomItemStack(Material.BOOK, "&f当计算结果等于今日日期组成的数字的百分之一(比如202407.29)时输出BUG - 日期"));
        it.add(MomotechItem.bug2.clone());
        it.add(new CustomItemStack(Material.BOOK, "&f当计算结果超过16777216时输出BUG - 正过载"));
        it.add(MomotechItem.bug3.clone());
        it.add(new CustomItemStack(Material.BOOK, "&f当计算结果小于-16777216时输出BUG - 负过载"));
        it.add(MomotechItem.bug4.clone());
        it.add(new CustomItemStack(Material.BOOK, "&f随机出现BUG - 系统, 其概率和计算结果, 在线人数以及TPS息息相关"));
        it.add(MomotechItem.bug5.clone());
        return it;
    }
}
