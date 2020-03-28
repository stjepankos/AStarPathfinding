import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Frame extends JPanel
        implements KeyListener, MouseListener,MouseMotionListener, ActionListener {
    /**
     * Created by kunc on Mar, 2020
     */
    JFrame window;
    int size;
    Node startNode, endNode;
    APathfinder pathfinder;
    char currentKey = 0;
    Timer timer;
    boolean showSteps,showInfo,infoChange,infoChange2;
    ColorFont cf;
    Controls controls;
    double a1,a2;
    long startTime,finalTime;

    public Frame(){
        size=50;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setLayout(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.infoChange=true;
        this.infoChange2=true;
        cf = new ColorFont();

        //used for Speed slider
        a1 = (5000.0000 / (Math.pow(25.0000/5000, 1.0/49)));
        a2 = 625.0000;

        pathfinder = new APathfinder(this,size);

        timer = new Timer(100,this);
        timer.start();




        window = new JFrame();
        window.setContentPane(this);
        window.setTitle("A* Pathfinding by Stjepan Kos");
        window.getContentPane().setPreferredSize(new Dimension(1699,1045));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        controls = new Controls(this);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        controls.addAll();

        this.revalidate();
        this.repaint();
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        //Updates control position
        controls.allPosition();

        // Draws and updates info text

        if (pathfinder.isSolved()) {
            if (!showSteps) {
                controls.getL("finishText").setText("Completed in " + finalTime + "ms");
            } else {
                controls.getL("finishText").setText("Completed!");
            }
        }else if(pathfinder.isNotSet()){
            controls.getL("finishText").setText("Set start and end");
        }else if(pathfinder.isPause()){
            controls.getL("finishText").setText("Paused, press q");
        }else if(pathfinder.isRunning()){
            controls.getL("finishText").setText("Running!");
        }else if(pathfinder.isNoPath()) {
            controls.getL("finishText").setText("NO Path!");
        }else{
            controls.getL("finishText").setText("Press space to start");
        }


        int height = getHeight();
        int width = getWidth();

        // Drawing GRID
        g.setColor(Color.lightGray);
        for (int i=0;i<height;i+=size){
            for (int j=0;j<width;j+=size){
                g.setColor(cf.gridColor);
                g.fillRect(j,i,size,size);
                g.setColor(Color.BLACK);
                g.drawRect(j,i,size,size);
            }
        }
        // Draws all borders
        g.setColor(cf.borderColor);
        for (int i = 0; i < pathfinder.getBorder().size(); i++) {
            g.fillRect(pathfinder.getBorder().get(i).getX() + 1, pathfinder.getBorder().get(i).getY() + 1,
                    size - 1, size - 1);
        }

        // Draws open Nodes
        for (int i=0;i< pathfinder.getOpenList().size();i++){
            Node current = pathfinder.getOpenList().get(i);
            g.setColor(cf.openColor);
            g.fillRect(current.getX()+1,current.getY(),size-1,size-1);

            drawInfo(current, g);
        }
        // Draws closed Nodes
        for (int i =0;i<pathfinder.getClosedList().size();i++){
            Node current = pathfinder.getClosedList().get(i);
            g.setColor(cf.closedColor);
            g.fillRect(current.getX()+1,current.getY(),size-1,size-1);

            drawInfo(current, g);
        }
        // Draws path
        for (int i = 0; i < pathfinder.getPath().size(); i++) {
            Node current = pathfinder.getPath().get(i);

            g.setColor(cf.pathColor);
            g.fillRect(current.getX() + 1, current.getY() + 1, size - 1, size - 1);

            drawInfo(current, g);
        }
        // Draws start
        if (startNode != null) {
            g.setColor(cf.startColor);
            g.fillRect(startNode.getX() + 1, startNode.getY() + 1, size - 1, size - 1);
        }
        // Draws end
        if (endNode != null) {
            g.setColor(cf.endColor);
            g.fillRect(endNode.getX() + 1, endNode.getY() + 1, size - 1, size - 1);
        }

        // Draws Panel Rectangle
        g.setColor(cf.btnPanel);
        g.fillRect(10, height-96, 300, 90);

        // Checks JBoxCheck state
        if (!pathfinder.isRunning()) {
            pathfinder.setDiagonal(controls.getC("isDiagonalCheck").isSelected());
        }
            showSteps = controls.getC("showStepsCheck").isSelected();
            showInfo = controls.getC("showInfoCheck").isSelected();
            infoChange = controls.getC("showInfoCheck").isSelected();

    }
    public void MapCalculations(MouseEvent e) {
        //if left mouse button is pressed
        if (SwingUtilities.isLeftMouseButton(e)) {
            int mouseBoxLX = e.getX() - (e.getX() % size);
            int mouseBoxLY = e.getY() - (e.getY() % size);
            if (currentKey == 's') {
                pathfinder.reset();
                if (startNode == null) {
                    startNode = new Node(mouseBoxLX, mouseBoxLY);
                } else {
                    startNode.setX(mouseBoxLX);
                    startNode.setY(mouseBoxLY);
                }
                repaint();
            } else if (currentKey == 'e') {
                pathfinder.reset();
                if (endNode == null) {
                    endNode = new Node(mouseBoxLX, mouseBoxLY);
                } else {
                    endNode.setX(mouseBoxLX);
                    endNode.setY(mouseBoxLY);
                }
                repaint();
            } else if (!pathfinder.isRunning()){
                pathfinder.reset();
                Node newBorder = new Node(mouseBoxLX, mouseBoxLY);
                pathfinder.addBorder(newBorder);
                repaint();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // if right mouse button is pressed
            int mouseBoxX = e.getX() - (e.getX() % size);
            int mouseBoxY = e.getY() - (e.getY() % size);
            if (currentKey == 's') {
                if (startNode != null && mouseBoxX == startNode.getX() && startNode.getY() == mouseBoxY) {
                    startNode = null;
                    repaint();
                }
            }else if (currentKey=='e'){
                    if (endNode != null && mouseBoxX == endNode.getX() && endNode.getY() == mouseBoxY) {
                        endNode = null;

                    }
                    repaint();
                }else{
                    int Location = pathfinder.searchBorder(mouseBoxX,mouseBoxY);
                    if (Location!=-1){
                        pathfinder.removeBorder(Location);
                    }
                    repaint();
                }
        }

    }
    // draws Info on Nodes if checked
    public void drawInfo(Node current, Graphics g) {
        if (showInfo) {
            g.setFont(cf.numbers);
            g.setColor(Color.black);
            g.drawString(Integer.toString(current.getF()/10), current.getX() + 4, current.getY() + 16);
            g.setFont(cf.smallNumbers);
            g.drawString(Integer.toString(current.getG()/10), current.getX() + 4, current.getY() + size - 7);
            g.drawString(Integer.toString(current.getH()/10), current.getX() + size - 26, current.getY() + size - 7);
        }
    }




    public static void main(String[] args) {
        new Frame();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if ((!pathfinder.getOpenList().isEmpty() || !pathfinder.isSolved())&& pathfinder.isRunning() && !pathfinder.isPause()) {
            if(showSteps) {
                // Shows steps
                timer.start();
                setSpeed();
                pathfinder.solve();
                if (pathfinder.isRunning() || pathfinder.isSolved()) {
                    repaint();
                }
            }else{
                // Instant and measures time
                startTime = System.currentTimeMillis();
                while((!pathfinder.isSolved())&& pathfinder.isRunning()){
                    pathfinder.solve();
                    repaint();
                }
                finalTime= System.currentTimeMillis()-startTime;
                System.out.println("Finished in "+finalTime+"ms");
            }
        }
        // Checks if Start and Finish Node have been selected
        pathfinder.setNotSet(startNode == null || endNode == null);

        // Checks if infoCheckBox has changed state
        if(infoChange2^infoChange){
            infoChange2=!infoChange2;
            repaint();
        }
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        currentKey= keyEvent.getKeyChar();
        if (currentKey== KeyEvent.VK_SPACE){
            pathfinder.reset();
            start();
        }
        else if (currentKey=='r'){
            pathfinder.reset();
        }
        else if (currentKey=='q' && pathfinder.isRunning()){
            pathfinder.setPause(!pathfinder.isPause());
        }
        else if(currentKey=='w'){
            pathfinder.resetWalls();
        }


    }

    void start(){
        if (startNode!=null && endNode != null){
            System.out.println("Starting");
            pathfinder.setup(startNode,endNode);
            pathfinder.setPause(false);
            pathfinder.setRunning(true);
        }
    }

    void setSpeed() {
        int delay = 0;
        int value = controls.getS("speed").getValue();

        if(value == 0) {
            timer.stop();
        }
        else if(value >= 1 && value < 50) {
            if(!timer.isRunning()) {
                timer.start();
            }
            // Exponential function. value(1) == delay(5000). value (50) == delay(25)
            delay = (int)(a1 * (Math.pow(25/5000.0000, value / 49.0000)));
        }
        else if(value >= 50 && value <= 100) {
            if(!timer.isRunning()) {
                timer.start();
            }
            // Exponential function. value (50) == delay(25). value(100) == delay(1).
            delay = (int)(a2 * (Math.pow(1/25.0000, value/50.0000)));
        }
        timer.setDelay(delay);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        currentKey = (char)0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MapCalculations(e);
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}


    @Override
    public void mouseDragged(MouseEvent e) {
        MapCalculations(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
