import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Controls {
    /**
     * Created by kunc on Mar, 2020
     */
    private Frame frame;
    private JCheckBox showStepsCheck, showInfoCheck,isDiagonalCheck;
    private JSlider speed;
    private JLabel speedText, finishText;
    private ArrayList<JCheckBox> checks;
    private ArrayList<JSlider> sliders;
    private ArrayList<JLabel> labels;

    public Controls(Frame frame) {
        this.frame = frame;
        checks= new ArrayList<>();
        sliders= new ArrayList<>();
        labels = new ArrayList<>();
        ColorFont cf = new ColorFont();

        speedText = new JLabel("Speed: ");
        speedText.setName("speedText");
        speedText.setVisible(true);
        speedText.setForeground(cf.lightText);
        labels.add(speedText);

        finishText = new JLabel("State: Build");
        finishText.setName("finishText");
        finishText.setForeground(Color.white);
        finishText.setFont(cf.BIGText);
        finishText.setForeground(cf.darkText);
        labels.add(finishText);



        // Steps check box
        showStepsCheck = new JCheckBox();
        showStepsCheck.setText("Steps?");
        showStepsCheck.setName("showStepsCheck");
        showStepsCheck.setSelected(true);
        showStepsCheck.setOpaque(false);
        showStepsCheck.setFocusable(false);
        showStepsCheck.setVisible(true);
        checks.add(showStepsCheck);
        showStepsCheck.setForeground(cf.lightText);

        // Info check box
        showInfoCheck = new JCheckBox();
        showInfoCheck.setText("Info");
        showInfoCheck.setName("showInfoCheck");
        showInfoCheck.setSelected(true);
        showInfoCheck.setOpaque(false);
        showInfoCheck.setFocusable(false);
        showInfoCheck.setVisible(true);
        checks.add(showInfoCheck);
        showInfoCheck.setForeground(cf.lightText);

        // Diagonal check box
        isDiagonalCheck = new JCheckBox();
        isDiagonalCheck.setText("Diagonal");
        isDiagonalCheck.setName("isDiagonalCheck");
        isDiagonalCheck.setSelected(true);
        isDiagonalCheck.setOpaque(false);
        isDiagonalCheck.setFocusable(false);
        isDiagonalCheck.setVisible(true);
        checks.add(isDiagonalCheck);
        isDiagonalCheck.setForeground(cf.lightText);


        // JSliders
        speed = new JSlider();
        speed.setName("speed");
        speed.setOpaque(false);
        speed.setVisible(true);
        speed.setFocusable(false);
        speed.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            speed.setValue(source.getValue());
            frame.setSpeed();
            frame.repaint();
        });
        sliders.add(speed);


    }

    public void addAll(){
        frame.add(showStepsCheck);
        frame.add(showInfoCheck);
        frame.add(isDiagonalCheck);
        frame.add(speed);
        frame.add(speedText);
        frame.add(finishText);
        frame.revalidate();
        frame.repaint();
    }
    public void allPosition(){
        speedText.setBounds(120, frame.getHeight()-60, 60, 20);
        finishText.setBounds(320, frame.getHeight()-100, 600, 100);
        showStepsCheck.setBounds(20, frame.getHeight()-88, 90, 20);
        showInfoCheck.setBounds(135, frame.getHeight()-88, 90, 20);
        isDiagonalCheck.setBounds(220, frame.getHeight()-88, 90, 20);
        speed.setBounds(60, frame.getHeight()-50, 180, 40);

    }

    public JCheckBox getC(String t) {
        for (JCheckBox check : checks) {
            if (check.getName().equals(t)) {
                return check;
            }
        }
        return null;
    }
    public JSlider getS(String t) {
        for (JSlider slider : sliders) {
            if (slider.getName().equals(t)) {
                return slider;
            }
        }
        return null;
    }
    public JLabel getL(String t) {
        for (JLabel label : labels) {
            if (label.getName().equals(t)) {
                return label;
            }
        }
        return null;
    }
}

