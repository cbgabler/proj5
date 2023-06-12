public class Animation extends Action{

    public Animation(EntityScheduling entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Animation createAnimationAction(EntityScheduling entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        return new Animation(entity, null, null, repeatCount);
    }

    public void executeAction(EventScheduler scheduler) {
        this.getEntity().nextImage(this.getEntity());

        if (this.getRepeatCount() != 1) {
            scheduler.scheduleEvent(this.getEntity(), createAnimationAction(this.getEntity(), this.getWorld(), this.getImageStore(), Math.max(this.getRepeatCount() - 1, 0)), this.getEntity().getAnimationPeriod());
        }
    }
}