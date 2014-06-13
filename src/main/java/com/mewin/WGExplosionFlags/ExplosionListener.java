/*
 * Copyright (C) 2012 mewin <mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mewin.WGExplosionFlags;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author mewin
 */
public class ExplosionListener implements Listener
{
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
        else
        {     
            Iterator<Block> itr = e.blockList().iterator();

            while (itr.hasNext())
            {
                Block block = itr.next();
                
                if (!Utils.explosionAllowedAtLocation(wgPlugin, type, block.getLocation()))
                {
                    itr.remove();
                }
            }
        }
    }
    
    @EventHandler
    public void onHangingBreak(HangingBreakEvent e)
    {
        if (e.getCause() == RemoveCause.EXPLOSION && e.getEntity().getLastDamageCause() != null)
        {
            EntityDamageEvent ldc = e.getEntity().getLastDamageCause();
            if (ldc != null && ldc instanceof EntityDamageByEntityEvent)
            {
                ExplosionType type = Utils.explosionTypeForEntity(((EntityDamageByEntityEvent) ldc).getDamager().getType());
                if (type != null && !Utils.explosionAllowedAtLocation(wgPlugin, type, e.getEntity().getLocation()))
                {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BED_BLOCK && (e.getClickedBlock().getWorld().getEnvironment() != Environment.NORMAL) && !Utils.explosionAllowedAtLocation(wgPlugin, ExplosionType.BED, e.getClickedBlock().getLocation()))
        {
            e.getPlayer().sendMessage(ChatColor.RED + "Care, it could explode.");
            e.setCancelled(true);
        }
    }
}
