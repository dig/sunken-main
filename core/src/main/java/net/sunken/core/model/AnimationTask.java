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
    private boolean repeat;
    private boolean reverse;

    private AnimationTask(int frame, Animation animation, Model model,
                          boolean repeat, boolean reverse) {
        this(animation, model, repeat, reverse);
        this.frame = frame;
        this.model = model;
        this.repeat = repeat;
        this.reverse = reverse;
    }

    public AnimationTask(Animation animation, Model model,
                         boolean repeat, boolean reverse) {

        if (reverse) {
            this.frame = (animation.getFrames().size() + 1);
        } else {
            this.frame = 0;
        }

        this.animation = animation;
        this.model = model;
        this.repeat = repeat;
        this.reverse = reverse;
    }

    @Override
    public void run() {
        if (model.getEntities().size() > 0 && model.isSpawned()) {
            Common.getLogger().log(Level.INFO, "Playing frame: " + frame);

            Frame frameObj = this.animation.getFrames().get(this.frame);
            for (Structure structure : frameObj.getStructures()) {
                model.updateEntity(structure);
            }

            boolean check = frame < animation.getFrames().size();
            if (reverse) {
                check = frame > 1;
            }

            if (check) {
                this.start();
            } else if (!repeat) {
                for (Structure structure : model.getContainer().getStructures()) {
                    model.updateEntity(structure);
                }
            } else {
                if (reverse) {
                    this.frame = (animation.getFrames().size() + 1);
                } else {
                    this.frame = 0;
                }

                this.start();
            }
        }
    }

    public void start() {
        int newFrame = this.frame + 1;
        if (reverse) {
            newFrame = this.frame - 1;
        }

        AnimationTask task = new AnimationTask(newFrame, this.animation, this.model, this.repeat, this.reverse);
        double seconds = ((double) this.animation.getFrames().get(newFrame).getWait()) / 1000;
        long delay = Double.valueOf((seconds * 20)).longValue();

        task.runTaskLater(Core.getPlugin(), delay);
    }
}
