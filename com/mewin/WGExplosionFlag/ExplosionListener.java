/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mewin.WGExplosionFlag;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author mewin
 */
public class ExplosionListener implements Listener {
    private WorldGuardPlugin wgPlugin;
    
    public ExplosionListener(WorldGuardPlugin wgPlugin)
    {
        this.wgPlugin = wgPlugin;
    }
    
    @EventHandler
    public void onExplosion(EntityExplodeEvent e)
    {
        ExplosionType type = Utils.explosionTypeForEntity(e.getEntityType());
        
        if (!Utils.explosionAllowedAtLocation(wgPlugin, type, e.getLocation()))
        {
            e.setCancelled(true);
        }
    }
}
