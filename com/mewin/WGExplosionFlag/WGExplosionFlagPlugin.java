/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mewin.WGExplosionFlag;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author mewin
 */
public class WGExplosionFlagPlugin extends JavaPlugin {
    public static final EnumFlag EXPLOSION_TYPE_FLAG = new EnumFlag("explosion-type", ExplosionType.class);
    public static final CustomSetFlag ALLOW_EXPLOSION_FLAG = new CustomSetFlag("allow-explosions", EXPLOSION_TYPE_FLAG);
    public static final CustomSetFlag DENY_EXPLOSION_FLAG = new CustomSetFlag("deny-explosions", EXPLOSION_TYPE_FLAG);
    
    private WGCustomFlagsPlugin custPlugin;
    private WorldGuardPlugin wgPlugin;
    private ExplosionListener listener;
    
    @Override
    public void onEnable()
    {
        custPlugin = getCustPlugin();
        wgPlugin = getWGPlugin();
        
        if (custPlugin == null)
        {
            getLogger().warning("This plugin requires WorldGuard Custom Flags, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        if (wgPlugin == null)
        {
            getLogger().warning("This plugin requires WorldGuard, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        listener = new ExplosionListener(wgPlugin);
        
        custPlugin.addCustomFlag(ALLOW_EXPLOSION_FLAG);
        custPlugin.addCustomFlag(DENY_EXPLOSION_FLAG);
        
        getServer().getPluginManager().registerEvents(listener, wgPlugin);
    }
    
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin == null || ! (plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        
        return (WorldGuardPlugin) plugin;
    }
    
    private WGCustomFlagsPlugin getCustPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");
        
        if (plugin == null || ! (plugin instanceof WGCustomFlagsPlugin))
        {
            return null;
        }
        
        return (WGCustomFlagsPlugin) plugin;
    }
}
