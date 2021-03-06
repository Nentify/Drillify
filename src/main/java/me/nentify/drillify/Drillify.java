package me.nentify.drillify;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.nentify.drillify.armourers.ArmourersCommonManager;
import me.nentify.drillify.integration.TEItems;
import me.nentify.drillify.item.DrillifyItems;
import me.nentify.drillify.proxy.CommonProxy;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = Drillify.MODID, version = Drillify.VERSION, dependencies = Drillify.DEPENDENCIES)
public class Drillify {
    public static final String MODID = "drillify";
    public static final String VERSION = "@VERSION@";
    public static final String PREFIX = MODID + ".";
    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String DEPENDENCIES = "required-after:Forge@[10.13.2.1291,);after:ThermalExpansion;after:armourersWorkshop";
    public static final String PROXY_CLIENT_CLASS = "me.nentify.drillify.proxy.ClientProxy";
    public static final String PROXY_COMMNON_CLASS = "me.nentify.drillify.proxy.CommonProxy";

    @SidedProxy(serverSide = PROXY_COMMNON_CLASS, clientSide = PROXY_CLIENT_CLASS)
    public static CommonProxy proxy;

    public static Logger logger;
    public static CreativeTabs creativeTab;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Starting Drillify");

        Config.preInit(event);

        creativeTab = new DrillifyCreativeTab();

        DrillifyItems.preInit();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        TEItems.init();
        DrillifyItems.init();
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        DrillifyItems.postInit();
        if (Config.armourersWorkshopModels && Loader.isModLoaded("armourersWorkshop")) {
            ArmourersCommonManager.instance.postInit();
        }
        proxy.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Config.armourersWorkshopModels && Loader.isModLoaded("armourersWorkshop")) {
            ArmourersCommonManager.instance.serverStarting();
        }
    }
}
