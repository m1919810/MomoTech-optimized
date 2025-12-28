package cn.qy.MomoTech.Items;

import cn.qy.MomoTech.MomoTech;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.chat.ChatInput;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.matl114.matlib.algorithms.algorithm.IterUtils;
import me.matl114.matlib.implement.custom.inventory.*;
import me.matl114.matlib.implement.custom.inventory.inventoryImpl.ChestMenuImpl;
import me.matl114.matlib.utils.AddUtils;
import me.matl114.matlib.utils.ThreadUtils;
import me.matl114.matlib.utils.chat.lan.DisplayNameUtils;
import me.matl114.matlib.utils.chat.lan.i18n.ZhCNLocalizationHelper;
import me.matl114.matlib.utils.chat.lan.pinyinAdaptor.PinyinHelper;
import me.matl114.matlib.utils.inventory.itemStacks.CleanItemStack;
import me.matl114.matlib.utils.reflect.ReflectUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IDDisplay //extends SlimefunItem implements RecipeDisplayItem
{
    List<ItemStack> ans = new ArrayList<>();
//    public IDDisplay(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
//        super(itemGroup, item, id, recipeType, recipe);
//    }

    public static void registerService(Plugin plugin){
        Bukkit.getPluginManager().registerEvents(idDisplayHistory, plugin);
    }

    @NotNull

    public List<ItemStack> getDisplayRecipes() {
        if(ans.isEmpty()){
            List<SlimefunItem> list=Slimefun.getRegistry().getEnabledSlimefunItems();
            for (SlimefunItem slimefunItem : list) {
                ans.add(new CustomItemStack(slimefunItem.getItem(), slimefunItem.getItemName(), "&f&lID: " + slimefunItem.getId()));
            }
        }
        return ans;
    }

    public static boolean openIDDisplayPage(Player var1, int var2, ItemStack var3, ClickAction var4, @Nullable PlayerProfile var5, @Nullable SlimefunGuideMode var6, @Nullable FlexItemGroup group, int page){
        idDisplayHistory.openLastOrCreate(ChestMenuImpl.FACTORY, var1, ()-> createIDDisplayScreen(var6));
        return false;
    }


    private static ScreenBuilder createIDDisplayScreen(SlimefunGuideMode mode){

        return createItemsScreen("&6ID Display, 正在搜索: &f", Slimefun.getRegistry().getAllSlimefunItems(), mode);
    }
    private static final ScreenHistoryStack idDisplayHistory = new ScreenHistoryStackImpl();
    private static void applySearch(Player player, String str, SlimefunGuideMode mode){

        if("取消".equals(str) || "cancel".equals(str)){
            idDisplayHistory.openLastOrCreate(
                ChestMenuImpl.FACTORY,
                player,
                ()->createIDDisplayScreen(mode)
            );
            return;
        }
        idDisplayHistory.popLast(player);
        ThreadUtils.executeAsync(()->{
            Predicate<ItemStack> itemStackPredicate = DisplayNameUtils.getPinyinFilter(
                ZhCNLocalizationHelper.P.I,
                PinyinHelper.P.I,
                str
            );
            List<SlimefunItem> sfItem = Slimefun.getRegistry().getAllSlimefunItems().stream().filter(i -> itemStackPredicate.test(i.getItem())).toList();
            ThreadUtils.executeSync(()->{
                idDisplayHistory.openAndPush(
                    ChestMenuImpl.FACTORY,
                    player,
                    1,
                    createItemsScreen("&6ID Display, 正在搜索: &f"+ str, sfItem , mode)
                );
            });
        });


    }


    private static ScreenBuilder createItemsScreen(String name, List<SlimefunItem> items, SlimefunGuideMode mode){
        ScreenBuilder builder = createGuide(name, mode);
        for (var entry: IterUtils.fastEnumerate(items)){
            SlimefunItem item = entry.getValue();
            builder.pageContent(
                entry.getIndex(),
                SlotProvider.instance()
                    .withStack(()->{
                        return generateItemIcon(item);
                    })
                    .handler(i->((inventory, player, slotIndex, clickType) -> {
                        ItemStack itemStack = inventory.getItem(slotIndex);
                        if(itemStack != null){
                            String sfid =  Slimefun.getItemDataService().getItemData(itemStack).orElse(null);
                            if(sfid != null){
                                AddUtils.displayCopyString(player, "点击拷贝该物品的粘液ID : " + sfid  , sfid  , sfid );
                            }
                        }
                        return false;
                    }))
            );
        }
        return builder;
    }
    private static ScreenBuilder createGuide(String name, SlimefunGuideMode mode){
        return new ScreenBuilder(ScreenTemplate.GUIDE)
            .title(name)
            .relateToHistory(idDisplayHistory)
            .override(7, ScreenUtils.SEARCH_BUTTON, InteractHandler.task((p)->{
                ChatInput.waitForPlayer(
                    MomoTech.getInstance(),
                    p,
                    (str)->applySearch(p, str, mode)
                );
                p.sendMessage("§a输入搜索关键词(应该支持拼音),输入\"取消\"或者\"cancel\"以保留当前搜索");
                ThreadUtils.executeSyncSched(p::closeInventory);
            }))
            .override(1, ScreenUtils.BACK_BUTTON, InteractHandler.task(p -> SlimefunGuide.openGuide(p, mode)))
            .override(4, new CleanItemStack(Material.BARRIER, "&7重置...", new String[]{"", "&7⇨ &b单击重置搜索界面"}), InteractHandler.task(p -> {
                idDisplayHistory.openWithHistoryClear(
                    ChestMenuImpl.FACTORY,
                    p,
                    1,
                    createIDDisplayScreen(mode)
                );
            }))
            ;
    }

    private static ItemStack generateItemIcon(SlimefunItem item){
        return new CleanItemStack(item.getItem(),(meta)->{
            List<String> lores = meta.getLore();
            if(lores == null)lores= new ArrayList<>();
            lores.add("");
            lores.add("§aID: " + item.getId());
            meta.setLore( lores);
        });
    }
}
