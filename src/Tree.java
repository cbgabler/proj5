import processing.core.PImage;

import java.util.List;

public class Tree extends EntityScheduling implements Transformed {
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;

    private int health;

    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health){
        super(id, position, images, actionPeriod, animationPeriod);
        this.health=health;
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    public static void parseTree(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Entity entity = Factory.createTree(id, pt, Double.parseDouble(properties[TREE_ACTION_PERIOD]), Double.parseDouble(properties[TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[TREE_HEALTH]), imageStore.getImageList(TREE_KEY));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", TREE_KEY, TREE_NUM_PROPERTIES));
        }
    }

    public void subHealth() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }

    public static int getTreeHealth() {
        return TREE_HEALTH;
    }

    public static int getTreeNumProperties() {
        return TREE_NUM_PROPERTIES;
    }
}