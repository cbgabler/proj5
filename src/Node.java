import java.util.List;
public class Node {
    private final List<Point> finalPath;
    private final Point p;
    private final int f;

    public Node(Point p, List<Point> finalPath, int f) {
        this.p = p;
        this.finalPath = finalPath;
        this.f = f;
    }

    public Point getPoint() {
        return p;
    }
    public List<Point> getPath(){
        return finalPath;
    }
    public int getF(){
        return f;
    }
}