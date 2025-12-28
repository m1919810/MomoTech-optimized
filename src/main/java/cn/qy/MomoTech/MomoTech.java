package cn.qy.MomoTech;

import cn.qy.MomoTech.Items.IDDisplay;
import cn.qy.MomoTech.Listeners.Listeners;
import cn.qy.MomoTech.tasks.ItemRegisterTask;
import cn.qy.MomoTech.tasks.MachineRegisterTask;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import lombok.Getter;

import me.matl114.matlib.core.UtilInitialization;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MomoTech extends JavaPlugin implements SlimefunAddon {

    public static double tps;
    public static Logger logger;
    public static Server server;
    public static int playerNumber;
    public static int seed;
    private static MomoTech instance;
    public static Config config;
    @Getter
    private static boolean autoUpdate = false;
    @Getter
    private static boolean disableCopierDupeStorage=false;
    @Getter
    private static Set<String> copierBlacklist = new HashSet<>();
    private static boolean hasGuizhanLib = false;
    public static MomoTech getInstance() {
        return instance;
    }

    public static void init(Logger l, Server s) {
        playerNumber = s.getOnlinePlayers().size();
        tps = s.getTPS()[0];
        seed = new Random().nextInt() % 1000000;
    }

    @Override
    public void onEnable() {
        new UtilInitialization(this,"MomoTech").displayName("乱码科技").onEnable();
        getLogger().info("MomoTech has been on enable.");
        getLogger().info("---------< MomoTech >-------");
        getLogger().info("|      Authors:QYhB05      |");
        getLogger().info("|      Plugin:MomoTech     |");
        getLogger().info("|      Version:1.1.11      |");
        getLogger().info("----------------------------");
        getLogger().info("> QQ反馈群:827684043");
        getLogger().info("> E-mail:3392295184@qq.com");
        getLogger().info("> 使用的API版本: 1.20.1-R0.1-SNAPSHOT");
        getLogger().info("> 支持SlimeFun4版本: 汉化版");
        getLogger().info("> 使用的License:MIT");
        getLogger().info("----------------------------");
        getLogger().info("载入附属中...");
        instance = this;
        config=new Config(this);
        autoUpdate = config.getOrSetDefault("options.auto-update",true);
        disableCopierDupeStorage = config.getOrSetDefault("options.disable-copier-dupe-storage",false);
        List<String> blacklist = config.getOrSetDefault("options.copier-blacklist",new ArrayList<String>());
        if (blacklist!=null){
            copierBlacklist.addAll(blacklist);
        }
        config.save();
        getLogger().info("开始注册监听器");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getLogger().info("监听器注册成功");
        getLogger().info("初始化参数");
        Bukkit.getScheduler().runTaskTimer(this,()->{
            playerNumber = Bukkit.getOnlinePlayers().size();
            tps = Bukkit.getTPS()[0];
        },0,40);
        init(getLogger(), getServer());
        logger = getLogger();
        server = getServer();
        getLogger().info("开始注册物品");
        ItemRegisterTask.run(getLogger());
        getLogger().info("物品注册成功");
        getLogger().info("开始注册机器");
        MachineRegisterTask.run(getLogger());
        getLogger().info("机器注册成功");
        IDDisplay.registerService(this);
        if (getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            getLogger().info( "检测到 鬼斩前置库插件(GuizhanLibPlugin)!");
            if(autoUpdate){

                try{
                    if ( getDescription().getVersion().startsWith("Build")) {
                        GuizhanUpdater.start(this, getFile(), "m1919810", "MomoTech-optimized", "main");
                        getLogger().info("自动更新功能已开启!");
                    }else{
                        getLogger().info( "并非正式版本,不进行自动更新!");
                    }
                }catch (Throwable e){
                    getLogger().log(Level.SEVERE, "自动更新功能出现异常!");
                    e.printStackTrace();
                    getLogger().log(Level.SEVERE, "该报错可以忽略");
                }
            }else{
                getLogger().info( "自动更新功能已关闭!");
            }
            return;
        }
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
        getLogger().info("MomoTech has been on disable.");
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }
}
