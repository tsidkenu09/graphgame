/* 2210A Assignment 5
 * Tsid Tomori
 * class to represent a node of a graph
 */

public class GraphNode {
    private int name;
    private boolean mark;

    public GraphNode (int name) {
        this.name = name;
    }

    public void mark(boolean mark){
        this.mark = mark;
    }

    public boolean isMarked(){
        return mark;
    }

    public int getName(){
        return name;
    }
}

