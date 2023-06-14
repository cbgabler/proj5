import processing.core.PImage;

import java.util.List;

public class Brain extends OneCycleAnimations {
    public static final String BRAIN_KEY = "brain";
    public static final int BRAIN_ANIMATION_PERIOD = 1;
    public static final int BRAIN_ACTION_PERIOD = 1;
    public Brain(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int animationCompleted) {
        super(id, position, images, actionPeriod, animationPeriod, animationCompleted);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
        }
    }
