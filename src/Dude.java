import processing.core.PImage;
import java.util.List;

abstract public class Dude extends EntityScheduling{
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static final String ALIEN_KEY = "alien";
    public static final int ALIEN_ACTION_PERIOD = 2;
    public static final int ALIEN_ANIMATION_PERIOD = 1;
    protected int resourceLimit;
    protected int resourceCount;

    public Dude(String id, Point position, List<PImage> images,
                 double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id,position,images,actionPeriod,animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;;
    }

    public int getResourceLimit(){return resourceLimit;}
    public int getResourceCount(){return resourceCount;}

    public abstract Point nextPosition(WorldModel world, Point destPos);

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public static void parseDude(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Entity entity = new DudeNotFull(id, pt, imageStore.getImageList(DUDE_KEY), Double.parseDouble(properties[DUDE_ACTION_PERIOD]), Double.parseDouble(properties[DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[DUDE_LIMIT]), 0);
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", DUDE_KEY, DUDE_NUM_PROPERTIES));
        }
    }
}