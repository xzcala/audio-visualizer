/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jon
 */
public class AudioVisualizer implements Visualizer{
    
    private final String name = "Jcbrzf Visualizer";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private String vizPaneInitialStyle = "";
    
    private final Double bandHeightPercentage = 1.3;
    private final Double minRadius = 1.0;
    private final Double rotatePhaseMultiplier = 300.0;
    
    private Double width = 0.0;
    private Double height = 0.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 340.0;
    
    private Circle[] outerCircles;
    private Circle[] innerCircles1;
    private Circle[] innerCircles2;
    private Circle[] innerCircles3;
    
    public JcbrzfVisualizer() {
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {        
        end();
        
        vizPaneInitialStyle = vizPane.getStyle();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        outerCircles = new Circle[numBands];
        innerCircles1 = new Circle[numBands/2];
        innerCircles2 = new Circle[numBands/3];
        innerCircles3 = new Circle[numBands/4];
        
        for (int i = 0; i < numBands/8; i++) {
            double angle = 16*i*Math.PI/numBands;
            double xOffset = (height-230)/4*Math.cos(angle);
            double yOffset = (height-230)/4*Math.sin(angle);
            double xPos=width/2 + xOffset;
            double yPos=height/2 + yOffset;
            Circle circle = new Circle(xPos,yPos,18);
            circle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(circle);
            innerCircles3[i] = circle;
        }
        
        for (int i = 0; i < numBands/3; i++) {
            double angle = 6*i*Math.PI/numBands;
            double xOffset = (height-100)/4*Math.cos(angle);
            double yOffset = (height-100)/4*Math.sin(angle);
            double xPos=width/2 + xOffset;
            double yPos=height/2 + yOffset;
            Circle circle = new Circle(xPos,yPos,12);
            circle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(circle);
            innerCircles2[i] = circle;
        }
        
        for (int i = 0; i < numBands/2; i++) {
            double angle = 4*i*Math.PI/numBands;
            double xOffset = (height)/4*Math.cos(angle);
            double yOffset = (height)/4*Math.sin(angle);
            double xPos=width/2 + xOffset;
            double yPos=height/2 + yOffset;
            Circle circle = new Circle(xPos,yPos,8);
            circle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(circle);
            innerCircles1[i] = circle;
        }
     
        for (int i = 0; i < numBands; i++) {
            double angle = 2*i*Math.PI/numBands;
            double xOffset = (height+100)/4*Math.cos(angle);
            double yOffset = (height+100)/4*Math.sin(angle);
            double xPos=width/2 + xOffset;
            double yPos=height/2 + yOffset;
            Circle circle = new Circle(xPos,yPos,5);
            circle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(circle);
            outerCircles[i] = circle;
        }
    }
    
    @Override
    public void end() {
        if (outerCircles != null && innerCircles1!=null && innerCircles2!=null && innerCircles3!=null) {
            for (Circle circle : outerCircles) {
                vizPane.getChildren().remove(circle);
            }
            for(Circle circle : innerCircles1){
                vizPane.getChildren().remove(circle);
            }
            for(Circle circle : innerCircles2){
                vizPane.getChildren().remove(circle);
            }
            for(Circle circle : innerCircles3){
                vizPane.getChildren().remove(circle);
            }
            outerCircles = null;
            innerCircles1 = null;
            innerCircles2 = null;
            innerCircles3 = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }        
        
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (outerCircles == null) {
            return;
        }
        
        Double hue;
        Integer num = min(outerCircles.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            hue = startHue-magnitudes[i]*-6.0;
            outerCircles[i].setFill(Color.hsb(hue, 1.0, 1.0, 1.0));
        }
        
        num = min(innerCircles1.length, magnitudes.length);
        for (int i = 0; i < num; i++) {
            hue = startHue-((60.0 + magnitudes[0])/60.0) * 360;
            innerCircles1[i].setFill(Color.hsb(hue, 1.0, 1.0));
        }
        
        num = min(innerCircles2.length, magnitudes.length);
        for (int i = 0; i < num; i++) {
            hue = startHue-((60.0 + magnitudes[num/2])/60.0) * 360;
            innerCircles2[i].setFill(Color.hsb(hue, 1.0, 1.0));
        }
        
        num = min(innerCircles3.length, magnitudes.length);
        for (int i = 0; i < num; i++) {
            hue = startHue-((60.0 + magnitudes[num])/60.0) * 360;
            innerCircles3[i].setFill(Color.hsb(hue, 1.0, 1.0));
        }
    }
}
