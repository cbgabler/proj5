import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class DudeNotFull extends Dude implements Transformed {
    public DudeNotFull(String id, Point position,
                       List<PImage> images,
                       double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }

    public boolean transform(WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {

            DudeFull dude = Factory.createDudeFull(this.getId(),
                    this.getPosition(),
                    this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);

            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        BiPredicate<Point, Point> neighbors = (Point p1, Point p2) ->
                ((Math.abs(p1.getX() - p2.getX()) == 1) && (p1.getY() == p2.getY())) ||
                        ((p1.getX() == p2.getX()) && (Math.abs(p1.getY() - p2.getY()) == 1));
        List<Point> path = pathingStrategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && (!world.isOccupied(p) || ((world.isOccupied(p)) && (world.getOccupancyCell(p) instanceof Stump))),
                neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {
            return this.getPosition();
        }
        Point nextPos = path.get(0);

        if (world.isOccupied(nextPos) && !(world.getOccupancyCell(nextPos) instanceof Stump)) {
            return this.getPosition();
        }

        return nextPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            this.resourceCount += 1;
            if (target instanceof Tree) {
                Tree t = (Tree) target;
                t.subHealth();
                return true;
            }

            if (target instanceof Sapling) {
                Sapling s = (Sapling) target;
                s.subHealth();
                return true;
            }
            return false;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }


    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));
        if (notFullTarget.isEmpty() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent( this,
                    Activity.createActivityAction( this, world, imageStore),
                    this.getActionPeriod());
        }
    }
}