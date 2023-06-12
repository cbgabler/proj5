public class Activity extends Action{

    public Activity(EntityScheduling entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Activity createActivityAction(EntityScheduling entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler){
        this.getEntity().executeActivity(this.getEntity(), this.getWorld(), this.getImageStore(), scheduler);
    }
}
