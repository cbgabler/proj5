import processing.core.PImage;

import java.util.List;

public class Brain extends Entity {
    public static final String BRAIN_KEY = "brain";
    public static final int BRAIN_ANIMATION_PERIOD = 1;
    public static final int BRAIN_ACTION_PERIOD = 0;

    public Brain(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
