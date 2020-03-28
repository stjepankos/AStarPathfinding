
public class Node {
    /**
     * Created by kunc on Mar, 2020
     */
    private int x,y;
    private int g,h,f;
    private Node Parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node getParent() {
        return Parent;
    }

    public void setParent(Node parent) {
        Parent = parent;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static boolean isEqual(Node s, Node e) {
        return s.getX() == e.getX() && s.getY() == e.getY();
    }
}
