package cn.qy.MomoTech.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.matl114.matlib.utils.itemCache.ItemStackCache;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class MachineUtils {
    public static boolean consumeAndPushNullOnly(BlockMenu inv, int[] inputSlots, int[] outputSlots, Predicate<ItemStack> consumeCondition, Function<ItemStack,ItemStack> pushCondition){
        return consumeAndPushNullOnly(inv,inputSlots,outputSlots,consumeCondition,pushCondition,true);
    }
    public static boolean consumeAndPushNullOnly(BlockMenu inv, int[] inputSlots, int[] outputSlots, Predicate<ItemStack> consumeCondition, Function<ItemStack,ItemStack> pushCondition,boolean returnAfterPushed){
        int inputlen=inputSlots.length;
        int outputlen=outputSlots.length;
        int i=0,j=0;
        ItemStack testInput;
        ItemStack testOutput;
        while(i<inputlen && j<outputlen){
            testInput=inv.getItemInSlot(inputSlots[i]);
            if(testInput==null||!consumeCondition.test(testInput)){
                i++;
                continue;
            }
            testOutput=inv.getItemInSlot(outputSlots[j]);
            if(testOutput!=null){
                j++;
                continue;
            }
            inv.replaceExistingItem(outputSlots[j],pushCondition.apply(testInput));
            if(returnAfterPushed){
                return true;
            }
        }
        return false;
    }

    public static boolean simpleNullonlyProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, SlimefunItemStack itemToBeConsumed, ItemStack itemToBePushed){
        return consumeAndPushNullOnly(inv,inputSlot,outputSlots,(it)->{
            return it.getAmount() >= itemToBeConsumed.getAmount() && Objects.equals(itemToBeConsumed.getItemId(), Slimefun.getItemDataService().getItemData(it).orElse(null));
        },(it)->{
            it.setAmount(it.getAmount()-itemToBeConsumed.getAmount());
            return itemToBePushed;
        },true);
    }

    public static boolean simpleNullonlyProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, ItemStackWrapper itemToBeConsumed, ItemStack itemToBePushed){
        return consumeAndPushNullOnly(inv,inputSlot,outputSlots,(it)->{
            return SlimefunUtils.isItemSimilar(it,itemToBeConsumed,false,true,false);
        },(it)->{
            it.setAmount(it.getAmount()-itemToBeConsumed.getAmount());
            return itemToBePushed;
        },true);
    }
    public static boolean consumeAndPushSimple(BlockMenu inv,int[] inputSlot,int[] outputSlots,Function<ItemStack, Pair<ItemStack,Runnable>> recipeCondition,boolean returnAfterPushed ){
        for(int i=0;i<inputSlot.length;i++){
            ItemStack itemToBeConsumed=inv.getItemInSlot(inputSlot[i]);
            if(itemToBeConsumed!=null){
                Pair<ItemStack,Runnable> re=recipeCondition.apply(itemToBeConsumed);
                if(re!=null){
                    ItemStack pushed=re.getFirstValue();
                    if(pushed!=null){
                        int amount=pushed.getAmount();
                        pushed=inv.pushItem(pushed,outputSlots);
                        if(pushed!=null&&pushed.getAmount()==amount){
                            continue;
                        }
                    }
                    re.getSecondValue().run();
                    if(returnAfterPushed){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean simpleProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, HashMap<ItemStack,ItemStack> itemMaps){
        return consumeAndPushSimple(inv,inputSlot,outputSlots,(item)->{
            for(Map.Entry<ItemStack,ItemStack> entry:itemMaps.entrySet()){
                if(SlimefunUtils.isItemSimilar(item,entry.getKey(),false,true,false)){
                    int should=item.getAmount()-entry.getKey().getAmount();
                    ItemStack returned=entry.getValue();
                    if(returned!=null){
                        returned=returned.clone();
                    }
                    return new Pair<>(returned,()->item.setAmount(should));
                }
            }
            return null;
        },true);
    }
    public static boolean simpleProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, ItemStack itemConsumed,ItemStack itemPushed){
       return consumeAndPushSimple(inv,inputSlot,outputSlots,(item)->{
            if(SlimefunUtils.isItemSimilar(item,itemConsumed,false,true,false)){
                int should=item.getAmount()-itemConsumed.getAmount();
                return new Pair<>(itemPushed==null?null:itemPushed.clone(),()->item.setAmount(should));
            }
            return null;
        },true);
    }
    public static boolean multiProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, ItemStack itemConsumed,ItemStack itemPushed,boolean returnAfterPushed){
        return consumeAndPushSimple(inv,inputSlot,outputSlots,(item)->{
            if(SlimefunUtils.isItemSimilar(item, itemConsumed,false,false,false)){
                ItemStack itemResult= itemPushed==null? null: itemPushed.clone();
                itemResult.setAmount(item.getAmount());
                return new Pair<>(itemResult,()->item.setAmount(0));
            }
            return null;
        },returnAfterPushed);
    }
    public static boolean multiProcessor(BlockMenu inv, int[] inputSlot, int[] outputSlots, HashMap<ItemStack, ItemStack> itemMaps,boolean returnAfterPushed){
        return consumeAndPushSimple(inv,inputSlot,outputSlots,(item)->{
            for(Map.Entry<ItemStack,ItemStack> entry:itemMaps.entrySet()){
                if(SlimefunUtils.isItemSimilar(item, entry.getKey(),false,false,false)){
                    int should=item.getAmount();
                    ItemStack returned=entry.getValue();
                    if(returned!=null){
                        returned=returned.clone();
                        returned.setAmount(should);
                    }
                    return new Pair<>(returned,()->item.setAmount(0));
                }
            }
            return null;
//            if(SlimefunUtils.isItemSimilar(item,itemConsumed,false,false,false)){
//                ItemStack itemResult= itemPushed==null? null: itemPushed.clone();
//                itemResult.setAmount(item.getAmount());
//                return new Pair<>(itemResult,()->item.setAmount(0));
//            }
//            return null;
        },returnAfterPushed);
    }

}
