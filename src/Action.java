abstract public class Action
{
    protected EntityScheduling entity;
    protected WorldModel world;
    protected ImageStore imageStore;
    protected int repeatCount;

    public Action(EntityScheduling entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    abstract void executeAction(EventScheduler scheduler);

    public EntityScheduling getEntity() {
        return entity;
    }

    public WorldModel getWorld() {
        return world;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    public int getRepeatCount() {return repeatCount;}
}
