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
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 *
 * @author  mewin, BangL
 */
public final class Utils {
    
    public static boolean explosionAllowedAtLocation(WorldGuardPlugin wgp, ExplosionType explosionType, Location loc) {
        RegionManager rm = wgp.getRegionManager(loc.getWorld());
        if (rm == null) {
            return true;
        }
        ApplicableRegionSet regions = rm.getApplicableRegions(loc);
        Iterator<ProtectedRegion> itr = regions.iterator();
        Map<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
        Set<ProtectedRegion> ignoredRegions = new HashSet<>();
        
        while(itr.hasNext()) {
            ProtectedRegion region = itr.next();
            
            if (ignoredRegions.contains(region)) {
                continue;
            }
            
            Object allowed = explosionAllowedInRegion(region, explosionType);
            
            if (allowed != null) {
                ProtectedRegion parent = region.getParent();
                
                while(parent != null) {
                    ignoredRegions.add(parent);
                    
                    parent = parent.getParent();
                }
                
                regionsToCheck.put(region, (boolean) allowed);
            }
        }
        
        if (regionsToCheck.size() >= 1) {
            Iterator<Entry<ProtectedRegion, Boolean>> itr2 = regionsToCheck.entrySet().iterator();
            
            while(itr2.hasNext()) {
                Entry<ProtectedRegion, Boolean> entry = itr2.next();
                
                ProtectedRegion region = entry.getKey();
                boolean value = entry.getValue();
                
                if (ignoredRegions.contains(region)) {
                    continue;
                }
                
                if (value) { // allow > deny
                    return true;
                }
            }
            
            return false;
        } else {
            Object allowed = explosionAllowedInRegion(rm.getRegion("__global__"), explosionType);
            
            if (allowed != null) {
                return (boolean) allowed;
            } else {
                return true;
            }
        }
    }
    
    public static Object explosionAllowedInRegion(ProtectedRegion region, ExplosionType explosionType) {
        if (region == null)
        {
            return true;
        }
        else
        {
            HashSet<ExplosionType> allowedExplosions = (HashSet<ExplosionType>) region.getFlag(WGExplosionFlagsPlugin.ALLOW_EXPLOSION_FLAG);
            HashSet<ExplosionType> blockedExplosions = (HashSet<ExplosionType>) region.getFlag(WGExplosionFlagsPlugin.DENY_EXPLOSION_FLAG);

            if (allowedExplosions != null && (allowedExplosions.contains(explosionType) || allowedExplosions.contains(ExplosionType.ANY))) {
                return true;
            }
            else if(blockedExplosions != null && (blockedExplosions.contains(explosionType) || blockedExplosions.contains(ExplosionType.ANY))) {
                return false;
            } else {
                return null;
            }
        }
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
