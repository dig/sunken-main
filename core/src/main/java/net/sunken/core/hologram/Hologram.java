package net.sunken.core.hologram;

import lombok.Getter;
import net.sunken.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hologram {

    @Getter
    private Location location;
    @Getter
    private List<String> lines;

    private ArrayList<LivingEntity> entities;
    private boolean spawned;

    public Hologram(Location location, List<String> lines){
        this.location = location;
        this.lines = lines;
        this.spawned = false;

        this.entities = new ArrayList<LivingEntity>();
        this.spawnEntities();
    }

    private void spawnEntities(){
        if(this.lines.size() > 0 && !this.spawned){
            double offsetY = (0.25 * this.lines.size());

            for(String line : this.lines){
                ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0, offsetY, 0), EntityType.ARMOR_STAND);
                entity.setVisible(false);
                entity.setCustomNameVisible(true);
                entity.setSmall(true);
                entity.setBasePlate(true);
                entity.setGravity(false);
                entity.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
                entity.setMetadata("Hologram", new FixedMetadataValue(Core.getPlugin(), true));

                this.entities.add((LivingEntity) entity);
                offsetY -= 0.25;
            }

            this.spawned = true;
        }
    }

    public void updateAll(){
        if(this.entities.size() > 0 && this.spawned){
            for(LivingEntity ent : this.entities){
                ent.remove();
            }

            this.entities.clear();
            this.spawned = false;

            this.spawnEntities();
        }
    }

    public void updateLine(int index, String line){
        if((index + 1) <= entities.size()){
            lines.set(index, line);
            entities.get(index).setCustomName(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    public void removeAll(){
        if(this.entities.size() > 0 && this.spawned){
            for(LivingEntity ent : this.entities){
                ent.remove();
            }

            this.entities.clear();
            this.spawned = false;
        }
    }

    public void removeLine(int index){
        if((index + 1) <= this.lines.size()){
            this.lines.remove(index);

            this.entities.get(index).remove();
            this.entities.remove(index);
        }
    }

    public void teleport(Location location){
        this.location = location;
        this.updateAll();
    }
}
