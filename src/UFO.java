import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class UFO extends EntityScheduling {
    public static final String UFO_KEY = "ufo";
    public static final int UFO_ANIMATION_PERIOD = 0;
    public static final int UFO_ACTION_PERIOD = 1;
    public static final int UFO_NUM_PROPERTIES = 2;
    private int animationCompleted;

    public UFO(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.animationCompleted = 0;
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (animationCompleted != 4) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getAnimationPeriod());
            animationCompleted++;
        } else {
            scheduler.unscheduleAllEvents(this);
        }
    }

    public static void parseUFO(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == UFO_NUM_PROPERTIES) {
            Entity entity = Factory.createUFO(id, pt, imageStore.getImageList(UFO_KEY), Double.parseDouble(properties[UFO_ACTION_PERIOD]), Double.parseDouble(properties[UFO_ANIMATION_PERIOD]));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", UFO_KEY, UFO_NUM_PROPERTIES));
        }
    }
}
