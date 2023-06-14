import processing.core.PImage;

import java.util.List;

public abstract class OneCycleAnimations extends EntityScheduling {
    private int animationCompleted;
    public OneCycleAnimations(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int animationCompleted) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.animationCompleted = 0;
    }

    public abstract void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public int getAnimationCompleted() {
        return animationCompleted;
    }

    public void addAnimationCompleted() {
        this.animationCompleted++;
    }
}
