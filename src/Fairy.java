import processing.core.PImage;

import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class Fairy extends EntityScheduling {
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;
    public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(entity.getPosition(), new ArrayList<>(List.of(Stump.class, Brain.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo(world, fairyTarget.get(), scheduler)) {
                if (fairyTarget.get() instanceof Stump) {
                    Sapling sapling = Factory.createSapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Sapling.SAPLING_KEY), 0);

                    world.addEntity(sapling);
                    sapling.scheduleActions(scheduler, world, imageStore);
                } else {
                    DudeNotFull dudeNotFull = new DudeNotFull(Dude.DUDE_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Dude.DUDE_KEY), Dude.DUDE_ACTION_PERIOD, Dude.DUDE_ANIMATION_PERIOD, Dude.DUDE_LIMIT, 0, Dude.DUDE_HEALTH);

                    world.addEntity(dudeNotFull);
                    dudeNotFull.scheduleActions(scheduler, world, imageStore);
                }
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getActionPeriod());
    }

    public static void parseFairy(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Entity entity = Factory.createFairy(id, pt, Double.parseDouble(properties[FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(FAIRY_KEY));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", FAIRY_KEY, FAIRY_NUM_PROPERTIES));
        }
    }

    public Point nextPosition(WorldModel world, Point destPos) {
    /*int horiz = Integer.signum(destPos.getX() - getPosition().getX());
    Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

    if (horiz == 0 || world.isOccupied(newPos)) {
        int vert = Integer.signum(destPos.getY() - getPosition().getY());
        newPos = new Point(getPosition().getX(), getPosition().getY() + vert);

        if (vert == 0 || world.isOccupied(newPos)) {
            newPos = getPosition();
        }
    }
    return newPos;*/
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        BiPredicate<Point, Point> neighbors = (Point p1, Point p2) ->
                ((Math.abs(p1.getX() - p2.getX()) == 1) && (p1.getY() == p2.getY())) ||
                        ((p1.getX() == p2.getX()) && (Math.abs(p1.getY() - p2.getY()) == 1));

        List<Point> path = pathingStrategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p), neighbors, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() >= 1) {
            return path.get(0);
        } else {
            return this.getPosition();
        }
    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
}