package net.sunken.core.model;

import net.sunken.common.Common;
import net.sunken.core.Core;
import net.sunken.core.model.type.Animation;
import net.sunken.core.model.type.Frame;
import net.sunken.core.model.type.Structure;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class AnimationTask extends BukkitRunnable {

    private int frame;
    private Animation animation;
    private Model model;

    private AnimationTask(int frame, Animation animation, Model model) {
        this(animation, model);
        this.frame = frame;
        this.model = model;
    }

    public AnimationTask(Animation animation, Model model) {
        this.frame = 0;
        this.animation = animation;
        this.model = model;
    }

    @Override
    public void run() {
        Common.getLogger().log(Level.INFO, "Playing frame: " + frame);

        Frame frameObj = this.animation.getFrames().get(this.frame);
        for (Structure structure : frameObj.getStructures()) {
            model.updateEntity(structure);
        }

        if (frame < animation.getFrames().size()) {
            this.start();
        } else {
            for (Structure structure : model.getContainer().getStructures()) {
                model.updateEntity(structure);
            }
        }
    }

    public void start() {
        AnimationTask task = new AnimationTask(this.frame + 1, this.animation, this.model);
        double seconds = ((double) this.animation.getFrames().get(this.frame + 1).getWait()) / 1000;
        long delay = Double.valueOf((seconds * 20)).longValue();

        task.runTaskLater(Core.getPlugin(), delay);
    }
}
