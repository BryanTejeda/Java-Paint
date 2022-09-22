/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame {

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private JPanel topPart;
    private JPanel line1 = new JPanel();
    private JPanel line2 = new JPanel();

    // create the widgets for the firstLine Panel.
    private final JButton undoButton;
    private final JButton clearButton;
    private final DefaultComboBoxModel shapeDropDown = new DefaultComboBoxModel();
    private final JCheckBox fillCheckBox;

    //create the widgets for the secondLine Panel.
    private final JButton color1;
    private final JButton color2;
    private final JCheckBox gradientCheckBox, dashCheckBox;
    private final JTextField lineWidth, dashLength;

    // Variables for drawPanel.
    private DrawPanel drawPanel = new DrawPanel();
    ArrayList<MyShapes> shapes = new ArrayList();
    private Boolean fillStatus;
    private Boolean gradientStatus;
    private MyShapes selectedShape;
    private Stroke stroke;
    private Paint paint1, paint2, paint3;
    private Color colorOne;
    private Color colorTwo;

    // add status label
    private final JLabel statusLabel;

    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame() {
        // add widgets to panels
        // firstLine widgets
        undoButton = new JButton("Undo");
        line1.add(undoButton);
        clearButton = new JButton("Clear");
        line1.add(clearButton);

        line1.add(new JLabel("Shapes:"));
        shapeDropDown.addElement("Rectangle");
        shapeDropDown.addElement("Oval");
        shapeDropDown.addElement("Line");
        line1.add(new JComboBox(shapeDropDown));

        fillCheckBox = new JCheckBox();
        line1.add(fillCheckBox);

        line1.add(new JLabel("Filled"));


        // secondLine widgets

        gradientCheckBox = new JCheckBox();
        color1 = new JButton("1st Color...");
        color2 = new JButton("2nd Color...");
        lineWidth = new JTextField(1);
        lineWidth.setText("10");
        dashLength = new JTextField(1);
        dashCheckBox = new JCheckBox();

        line2.add(gradientCheckBox);
        line2.add(new JLabel("Use Gradient"));
        line2.add(color1);
        line2.add(color2);
        line2.add(new JLabel("Line Width:"));
        line2.add(lineWidth);
        line2.add(new JLabel("Dash Length:"));
        line2.add(dashLength);
        line2.add(dashCheckBox);
        line2.add(new JLabel("Dashed"));

        // add top panel of two panels
        setLayout(new BorderLayout());
        topPart = new JPanel(new GridLayout(2, 1));

        topPart.add(line1);
        topPart.add(line2);
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPart, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        statusLabel = new JLabel();
        add(statusLabel, BorderLayout.SOUTH);
        //add listeners and event handlers
        //Now this is Pod-Racing
        //ButtonHandler buttonHandler = new ButtonHandler;
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (shapes.size() != 0) {
                    shapes.remove(shapes.size()-1);
                    repaint();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (clearButton == ae.getSource()) {
                    shapes.clear();
                    repaint();
                }
            }
        });
        color1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorOne = JColorChooser.showDialog(null, "Pick your color", Color.WHITE);
            }
        });
        color2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorTwo = JColorChooser.showDialog(null, "Pick your color", Color.WHITE);
            }
        });
        //drawPanel.addMouseMotionListener(DrawPanel.MouseHandler);

    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel {

        public DrawPanel() {
            MouseHandler handler = new MouseHandler();
            MouseHandler handler2 = new MouseHandler();
            addMouseListener(handler2);
            addMouseMotionListener(handler);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (int i = 0; i < shapes.size(); i++) {
                shapes.get(i).draw(g2d);
            }

        }

        private class MouseHandler extends MouseAdapter implements MouseMotionListener {

            public void mousePressed(MouseEvent event) {
                int xPos = event.getX();
                int yPos = event.getY();

                fillStatus = fillCheckBox.isSelected();
                gradientStatus = gradientCheckBox.isSelected();

                if (dashCheckBox.isSelected()) {
                    stroke = new BasicStroke(Float.parseFloat(lineWidth.getText()),
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{Float.parseFloat(
                            dashLength.getText())}, 0);
                } else {
                    stroke = new BasicStroke(Float.parseFloat(lineWidth.getText()));
                }

                if (gradientStatus) {
                    paint1 = new GradientPaint(0, 0, colorOne, 50, 50, colorTwo, true);
                } else {
                    paint1 = colorOne;
                }

                if (shapeDropDown.getSelectedItem() == "Oval") {
                    selectedShape = new MyOval(new Point(xPos, yPos), new Point(xPos, yPos), paint1, stroke, fillStatus);
                    shapes.add(selectedShape);
                }

                if (shapeDropDown.getSelectedItem() == "Line") {
                    selectedShape = new MyLine(new Point(xPos, yPos), new Point(xPos, yPos), paint1, stroke);
                    shapes.add(selectedShape);
                }

                if (shapeDropDown.getSelectedItem() == "Rectangle") {
                    selectedShape = new MyRectangle(new Point(xPos, yPos), new Point(xPos, yPos), paint1, stroke, fillStatus);
                    shapes.add(selectedShape);
                }
            }

            public void mouseReleased(MouseEvent event) {
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                selectedShape.setEndPoint(new Point(event.getX(), event.getY()));
                repaint();

                int xPos = event.getX();
                int yPos = event.getY();
                statusLabel.setText("(" + String.valueOf(xPos) + "," + String.valueOf(yPos) + ")");

            }


            @Override
            public void mouseMoved(MouseEvent event) {
                statusLabel.setText("(" + event.getX() + "," + event.getY() + ")");
            }
        }

    }
}
