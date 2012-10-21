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
package com.mewin.WGExplosionFlag;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author mewin <mewin001@hotmail.de>
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
