import processing.core.PImage;

import java.util.List;

public abstract class Alien extends EntityScheduling{
    public Alien(String id, Point position,
                     double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount, List<PImage> images)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }
}
