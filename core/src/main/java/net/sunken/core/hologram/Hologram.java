package net.sunken.core.hologram;

import lombok.Getter;
import net.sunken.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    @Getter
    private Location location;
    @Getter
    private List<String> lines;

    private ArrayList<LivingEntity> entities;
    private boolean spawned;
    private double offsetY;

    public Hologram(Location location, List<String> lines, double offsetY){
        this.location = location;
        this.lines = lines;
        this.spawned = false;
        this.offsetY = offsetY;

        this.entities = new ArrayList<LivingEntity>();
        this.spawnEntities();
    }

    private void spawnEntities(){
        if(this.lines.size() > 0 && !this.spawned){
            Chunk chunk = this.location.getChunk();
            if (chunk != null && !chunk.isLoaded()) {
                chunk.load(true);
            }

            double offY = 0;

            for(String line : this.lines){
                ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0, offY, 0), EntityType.ARMOR_STAND);
                entity.setVisible(false);
                entity.setCustomNameVisible(true);
                entity.setSmall(true);
                entity.setBasePlate(true);
                entity.setGravity(false);
                entity.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
                entity.setMetadata("Hologram", new FixedMetadataValue(Core.getPlugin(), true));

                this.entities.add((LivingEntity) entity);
                offY -= this.offsetY;
            }

            this.spawned = true;
        }
    }

    public void addLine(String line){
        double offY = (0 - (this.offsetY * this.lines.size()));

        ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0, offY, 0), EntityType.ARMOR_STAND);
        entity.setVisible(false);
        entity.setCustomNameVisible(true);
        entity.setSmall(true);
        entity.setBasePlate(true);
        entity.setGravity(false);
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
        entity.setMetadata("Hologram", new FixedMetadataValue(Core.getPlugin(), true));

        this.lines.add(line);
        this.entities.add((LivingEntity) entity);
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

    public String getLine(int index){
        if((index + 1) <= this.lines.size()){
            return this.lines.get(index);
        }

        return null;
    }

    public void teleport(Location location){
        this.location = location;
        this.updateAll();
    }
}
