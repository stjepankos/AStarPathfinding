import java.util.ArrayList;

public class APathfinder {
    /**
     * Created by kunc on Mar, 2020
     */
    private Node Start;
    private Node End;
    private ArrayList<Node> path;
    private ArrayList<Node> openList;
    private ArrayList<Node> closedList;
    private ArrayList<Node> border;
    private int size,diagonalMoveCost;
    private Frame frame;
    private boolean solved, running,noPath,notSet,pause,diagonal;

    public APathfinder(Frame frame, int size) {

        diagonalMoveCost = (int)(Math.sqrt(2*(Math.pow(size,2))));
        this.frame = frame;
        this.size = size;
        this.path= new ArrayList<>();
        this.openList= new ArrayList<>();
        this.closedList= new ArrayList<>();
        this.border= new ArrayList<>();
        this.solved=false;
        this.running =false;
        this.pause=false;
        this.noPath=false;
        this.notSet=true;
        this.diagonal=true;
    }

    public void setup(Node s, Node e){
        Start = s;
        Start.setG(0);
        End = e;
        this.openList.add(Start);
        if (Node.isEqual(Start,End)) {
            this.path = new ArrayList<>();
        }
    }

    public void solve(){
            Node current = getLowest();
            openList.remove(current);
            closedList.add(current);
            if (current==null){                   //if no more openNodes then there's no path
                System.out.println("END No path");
                setRunning(false);
                setSolved(false);
                setNoPath(true);
                closedList.remove(current);
                return;
            }
            if (Node.isEqual(current,End)){   //if current Node == End Node, finishes.
                retracePath(current);
                setSolved(true);
                System.out.println("Completed: Path is found!");
                setRunning(false);
                return;
            }

            for (int i =0;i<3;i++){              //Checks neighbours
                for (int j=0;j<3;j++){
                    if (!diagonal){
                        if (i==0 &&(j==0 || j==2) || i==2 &&(j==0 || j==2)){
                            continue;
                        }
                    }
                    if (i==1 && j==1){
                        continue;
                    }
                    int possibleX =(current.getX()-size)+(size*i);
                    int possibleY = (current.getY()-size)+(size*j);
                    if(possibleX<0 || possibleY<0 || possibleX > frame.getWidth() || possibleY > frame.getHeight()){
                        // Checks if node is in the frame, if it's not, skips to the next one
                        continue;
                    }

                    Node possible = new Node(possibleX,possibleY);
                    Node borderCheck = getNode(border,possibleX,possibleY);
                    Node openCheck = getNode(openList,possibleX,possibleY);
                    Node closedCheck = getNode(closedList,possibleX,possibleY);
                    if (borderCheck!=null || closedCheck!=null){      // if it's a wall or a closed node
                        continue;                                     // skips to next one
                    }
                    gcost(possible,current);
                    if (openCheck==null){
                        hcost(possible,End);
                        possible.setF(possible.getG()+possible.getH());
                        possible.setParent(current);
                        openList.add(possible);

                    }else{
                        if (possible.getG()<openCheck.getG()){    //Checks if the new g cost for the open Node is smaller then previous
                            openCheck.setG(possible.getG());      // if yes,sets new G cost, changes parent to current and calculates new F cost
                            openCheck.setParent(current);
                            openCheck.setF(openCheck.getG()+openCheck.getH());
                        }
                    }
                }
            }
        }

    // calculates Gcost
    public void gcost(Node openNode, Node parent){
        int GxMoveCost = openNode.getX() - parent.getX();
        int GyMoveCost = openNode.getY() - parent.getY();
        int gCost = parent.getG();

        if (GxMoveCost != 0 && GyMoveCost != 0) {
            gCost += diagonalMoveCost;
        } else {
            gCost += size;
        }
        openNode.setG(gCost);
    }

    // calculates Hcost
    public void hcost( Node openNode, Node endNode){
        int HxDiff = Math.abs(endNode.getX() - openNode.getX());
        int HyDiff = Math.abs(endNode.getY() - openNode.getY());
        int hCost = HxDiff + HyDiff;
        openNode.setH(hCost);
    }

    public Node getNode(ArrayList<Node> List,int x, int y){
        for (Node node : List) {
            if (node.getX() == x && node.getY() == y) {
                return node;
            }
        }
        return null;
    }

    private Node getLowest(){           //Finds open Node with lowest F cost, if more, it checks H cost
        if (openList.size()==0){        // and takes the one with lowest H cost.
            return null;
        }
        Node Low = openList.get(0);
        for (Node i : openList){
            if (i.getF() <= Low.getF()){
                if (i.getF() == Low.getF()){
                    if (!(i.getH()<Low.getH())) {
                        continue;
                    }
                }
                Low=i;
            }
        }
        return Low;
    }

    private void retracePath(Node current) {  //When path found, retraces through parents to start Node
        Node temp = current;
        this.path.add(current);

        while (temp.getParent() != null) {
            this.path.add(temp.getParent());
            temp = temp.getParent();
        }

        this.path.add(Start);
    }

    public void addBorder(Node node) {    //adds walls to list, checks if not duplicate
        if (border.size() == 0) {
            border.add(node);
        } else if (!checkBorderDuplicate(node)) {
            border.add(node);
        }
    }
    public boolean checkBorderDuplicate(Node node) {
        for (Node value : border) {
            if (node.getX() == value.getX() && node.getY() == value.getY()) {
                return true;
            }
        }
        return false;
    }
    public int searchBorder(int xSearch, int ySearch) {
        int Location = -1;

        for (int i = 0; i < border.size(); i++) {
            if (border.get(i).getX() == xSearch && border.get(i).getY() == ySearch) {
                Location = i;
                break;
            }
        }
        return Location;
    }
    public void removeBorder(int location) {
        border.remove(location);
    }

    public ArrayList<Node> getOpenList() {
        return openList;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public ArrayList<Node> getBorder() {
        return border;
    }
    public void reset() {
        while (openList.size() > 0) {
            openList.remove(0);
        }

        while (closedList.size() > 0) {
            closedList.remove(0);
        }

        while (path.size() > 0) {
            path.remove(0);
        }
        setNoPath(false);
        setRunning(false);
        setSolved(false);
        frame.repaint();
    }
    public void resetWalls(){       //removes walls
        while(border.size()>0){
            border.remove(0);
        }
        frame.repaint();
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isNoPath() {
        return noPath;
    }

    public void setNoPath(boolean noPath) {
        this.noPath = noPath;
    }

    public boolean isNotSet() {
        return notSet;
    }

    public void setNotSet(boolean notSet) {
        this.notSet = notSet;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }
}
