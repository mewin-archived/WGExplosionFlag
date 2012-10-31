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

import com.mewin.util.Util;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 *
 * @author  mewin, BangL
 */
public final class Utils {
    
    public static boolean explosionAllowedAtLocation(WorldGuardPlugin wgp, ExplosionType explosionType, Location loc) {
        return Util.flagAllowedAtLocation(wgp, explosionType, loc, WGExplosionFlagsPlugin.ALLOW_EXPLOSION_FLAG, WGExplosionFlagsPlugin.DENY_EXPLOSION_FLAG, ExplosionType.ANY);
    }
    
    public static ExplosionType explosionTypeForEntity(EntityType entityType)
    {
        switch(entityType)
        {
            case CREEPER:
                return ExplosionType.CREEPER;
            case ENDER_CRYSTAL:
                return ExplosionType.ENDER_CRYSTAL;
            case GHAST:
                return ExplosionType.GHAST;
            case PRIMED_TNT:
                return ExplosionType.TNT;
            case WITHER:
                return ExplosionType.WITHER;
            case WITHER_SKULL:
                return ExplosionType.WITHER_SKULL;
            default:
                return ExplosionType.OTHER;
        }
    }
}
