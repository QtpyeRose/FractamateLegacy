/*
 */
package aspwil.fractamatelegacy;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 * this class is just set up so that you can plug in lines of code and generate
 * the fractal asosiated with it
 *
 */
public class CodeRenderTrace {

    public static void main(String args[]) throws Exception {

        //the number of iterations to do
        int iter = 3;
        //0 is line and 1 is point
        int drawType = 1;
        //waether we do showType
        boolean doShowType = false;
        //the step showType is done on
        int showType = 1;
        
        int maxSteps = 1000;

        //used to run diffrent code when exporting gif
        boolean exportGif = true;

        
        //###############################set up the rule here
        Vector seed = new Vector(new Point(0.5, 0.25), new Point(0.5, 0.5));
        ArrayList<Point> originRules = new ArrayList<Point>();
        //originRules.add(new Vector(0.5,0.5,seed.length/2,seed.angle).getEndPoint()); //up
        originRules.add(new Vector(0.5, 0.5, seed.length/Math.sqrt(2), seed.angle + (3 * 2 * Math.PI / 4)).getEndPoint().rotAdd(new Point(0,0,0.01)));
        originRules.add(new Vector(0.5, 0.5, seed.length/Math.sqrt(2), seed.angle + (1 * 2 * Math.PI / 4)).getEndPoint().rotAdd(new Point(0,0,0.01)));

        
        
        
        
        //options
        File gifFile = null;
        ImageOutputStream output = null;
        GifSequenceWriter writer = null;
        
        //which frame of the gif you are on
        int imageNum = 0;
        // the number of frames to generate in the gif
        int framesToSave = 0;
        //changes the way we draw
        StdDraw.enableDoubleBuffering();
        //set the canvas size to size of screen (-100 pixels)
        StdDraw.setCanvasSize(Toolkit.getDefaultToolkit().getScreenSize().height - 200, Toolkit.getDefaultToolkit().getScreenSize().height - 200);
        //set the size of the pen to draw with
        StdDraw.setPenRadius(0.001);

        //set up rule points array listto use
        ArrayList<Point> rulePoints = new ArrayList<Point>();
        for (Point p : originRules) {
            rulePoints.add(new Point(p));
        }

//        if (exportGif) {
//            //find the smallest rot on all of the rules
//            double minRot = 1;
//            for (Point p : originRules) {
//                if (Math.abs(p.rot) < minRot && p.rot != 0d) {
//                    minRot = Math.abs(p.rot);
//                    System.out.println(minRot);
//                }
//            }
//            //use the smallest rot to find how many frames for one loop
//            framesToSave = (int) Math.round((Math.PI * 2) / minRot);
//            //creat new output and writer
//            output = new FileImageOutputStream(new File("savedGif" + System.currentTimeMillis() + ".gif"));
//            writer = new GifSequenceWriter(output, 5, 50, true);
//        }

        if (exportGif) {
            framesToSave = 32;
            //creat new output and writer
            gifFile = new File("savedGif" + System.currentTimeMillis() + ".gif");
            output = new FileImageOutputStream(gifFile);
            writer = new GifSequenceWriter(output, 5, 50, true);
        }

        //main loop
        int inc = 0;
        int steps = 0;
        while (true) {
            //############################## this is how th pointsmove each frame
            //rotate the points
            for (Point p : rulePoints) {
                p.rotate(seed.getEndPoint(), p.rot);
            }
            //rulePoints.get(0).y=Math.sin(inc/(2*10d))/(2*Math.PI)+0.5;
            //rulePoints.get(1).y=Math.cos(inc/(2*10d))/(2*Math.PI)+0.5;
            //inc++;
            if (drawType == 0) {
                StdDraw.clear();
            }
            
            
            
            
            

            

            //seed.draw();
            ArrayList<Vector> active = new ArrayList<>();
            active.add(seed);
            ArrayList<Vector> gen = new ArrayList<>();
            //generate and draw vectors
            for (int i = 0; i < iter + 2; i++) {
                for (int j = 0; j < active.size(); j++) {
                    Vector activeVector = new Vector(active.get(j));
                    //line draw type
                    if (drawType == 0) {//
                        activeVector.draw();
                    } //point draw type
                    else if (drawType == 1) {// 
                        //draw the point if dont doShowType (every time) or on the last layer and point is showType
                        if (i == iter + 1 && j == showType || !doShowType) {
                            activeVector.getEndPoint().draw();
                        }
                    }
                    //generate the next vectors 
                    for (Point p : rulePoints) {
                        //generate a vector from the rule
                        Vector ruleVector = seed.build(new Point(1 - p.x, p.y));
                        //find the relative changes from seed for that rule point
                        double lMult = ruleVector.length / seed.length;
                        double aChange = Vector.rectifyAngle(seed.angle - ruleVector.angle);
                        //generate a new vector with same realitvity nased on active vector 
                        Vector newVector = activeVector.relativeBuild(lMult, aChange);
                        //add the newVector to the generated vectors list
                        gen.add(newVector);
                    }
                }
                //set the new active vectors to the newly generated vectors to make next row
                active = gen;
                //clear the gen array
                gen = new ArrayList<>();
            }
            if (steps == maxSteps){
                //draw the seed
                StdDraw.setPenColor(StdDraw.BLUE);
                seed.draw();
                for (Point p : originRules) {
                    StdDraw.line(seed.getEndPoint().x, seed.getEndPoint().y, p.x, p.y);
                }
                //this draws the text on the side of the output screen
            StdDraw.textLeft(0.01, 0.975, "" + (iter + 1) + "," + drawType + "," + doShowType + "," + showType + ",0,");
            StdDraw.textLeft(0.01, 0.950, "" + seed + ",");
            StdDraw.textLeft(0.01, 0.925, "Points:{\n");
            for (int i = 0; i < originRules.size(); i++) {
                if (i != originRules.size() - 1) {
                    StdDraw.textLeft(0.01, 0.9 - 0.025 * i, " " + originRules.get(i).copyShift(-0.5, -0.5) + ",");
                } else {
                    StdDraw.textLeft(0.01, 0.9 - 0.025 * i, " " + originRules.get(i).copyShift(-0.5, -0.5));
                    StdDraw.textLeft(0.01, 0.875 - 0.025 * i, "}");
                }
            }
            if (rulePoints.isEmpty()) {
                StdDraw.textLeft(0.01, 0.9, "}");
            }
                StdDraw.setPenColor(StdDraw.BLACK);
            }
            StdDraw.show();
            if (imageNum < framesToSave && exportGif && steps == maxSteps) {
                //reset steps
                steps = 0;
                //add the image to gif
                writer.writeToSequence((RenderedImage) StdDraw.getImage());
                //set the image number up one
                imageNum++;
                
                originRules.get(0).y=Math.tan(imageNum*(2*Math.PI/64))/(2*Math.PI)+0.5;
                originRules.get(1).y=-Math.tan(imageNum*(2*Math.PI/64))/(2*Math.PI)+0.5;
                
                //originRules.get(0).rotate(seed.getEndPoint(), 0.1);
                //originRules.get(1).rotate(seed.getEndPoint(), 0.1);
                
                rulePoints = new ArrayList<Point>();
                for (Point p : originRules) {
                    rulePoints.add(new Point(p));
                }
                
                StdDraw.clear();
            }
            if (!(imageNum < framesToSave) && exportGif) {
                //close the writer and output
                System.out.println("files saved as: "+ gifFile.getAbsolutePath());
                writer.close();
                output.close();
                //exit
                System.exit(0);
            }
            steps++;
        }
    }
}
