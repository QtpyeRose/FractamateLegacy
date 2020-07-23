package aspwil.fractamatelegacy;

/**
 * TODO: - add tutorial
 *
 *
 */
import java.awt.Toolkit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

class MainBet {

    public static void main(String args[]) throws Exception {
        //options
        ImageOutputStream output = new FileImageOutputStream(new File("default.gif"));;
        GifSequenceWriter writer = new GifSequenceWriter(output, 5, 50, true);
        //which frame of the gif you are on
        int imageNum = 0;
        // the number of frames to generate in the gif
        int framesToSave = 0;
        //used to run diffrent code when exporting gif
        boolean exportGif = false;
        //the number of iterations to do
        int iter = 3;
        //0 is line and 1 is point
        int drawType = 0;
        //waether we do showType
        boolean doShowType = false;
        //the step showType is done on
        int showType = 1;
        //the rotation guide
        double rot = 0;
        //the default seed
        Vector seed = new Vector(new Point(0.5, 0.25), new Point(0.5, 0.5));
        ArrayList<Point> originRules = new ArrayList<Point>();
        //setup
        boolean breakOut = false;
        //changes the way we draw
        StdDraw.enableDoubleBuffering();
        //set the canvas size to size of screen (-100 pixels)
        StdDraw.setCanvasSize(Toolkit.getDefaultToolkit().getScreenSize().height - 200, Toolkit.getDefaultToolkit().getScreenSize().height - 200);
        //set the defalt tscreen to start on
        String screen = "main";
        //set the size of the pen to draw with
        StdDraw.setPenRadius(0.001);
        ArrayList<Point> rulePoints = new ArrayList<Point>();
        //used to say wether we sould make a new point
        boolean newPoint;
        //buttons 
        //design screen
        Button drawButton = new Button(0, 0, 0.2, 0.05, "Generate Design");
        Button reset = new Button(0.2, 0, 0.4, 0.05, "Reset Points");
        Button exportDesign = new Button(0.2, 0.05, 0.4, 0.1, "Export Save");
        Button importDesign = new Button(0.2, 0.1, 0.4, 0.15, "Import Save");
        Button switchDraw = new Button(0, 0.05, 0.2, 0.1, "Switch Draw Type");
        Button switchshowType = new Button(0, 0.1, 0.2, 0.15, "Switch Show Type");
        Button iterUp = new Button(0.4, 0, 0.6, 0.05, "Increase Iteration");
        Button iterDown = new Button(0.6, 0, 0.8, 0.05, "Decrease Iteration");
        Button showTypeUp = new Button(0.4, 0.05, 0.6, 0.1, "Increase Show Only");
        Button showTypeDown = new Button(0.6, 0.05, 0.8, 0.1, "Decrease Show Only");
        Button rotUp = new Button(0.4, 0.1, 0.6, 0.15, "Increse Movement");
        Button rotDown = new Button(0.6, 0.1, 0.8, 0.15, "Decrease Movement");
        Button exactPoint = new Button(0.8, 0.95, 1, 1, "Add Exact Point");
        //main screen
        Button start = new Button(0, 0, 0.2, 0.1, "Start");
        //design
        Button design = new Button(0d, 0d, 0.2, 0.1, "Design rule");
        Button exportToGif = new Button(0.2, 0d, 0.4, 0.1, "Export To Gif");

        //main loop
        while (true) {
            if (screen == "main") {
                if (start.isPressed()) {
                    screen = "design";
                    unpress();
                }
                start.draw();
                StdDraw.text(0.5, 0.5, "Fractel generator V1.6.1");
                StdDraw.text(0.5, 0.45, "by aspen");
                StdDraw.show();
                StdDraw.clear();
            } else if (screen == "design") {
                //design screen
                //button pressing code
                if (drawButton.isPressed()) {
                    //set up rule points array listto use
                    rulePoints = new ArrayList<Point>();
                    for (Point p : originRules) {
                        rulePoints.add(new Point(p));
                    }
                    screen = "draw";
                    unpress();
                } else if (switchDraw.isPressed()) {
                    //cycle the draw type
                    drawType = (drawType + 1) % 2;
                    unpress();
                } else if (switchshowType.isPressed()) {
                    //cycle doShowType
                    doShowType = !doShowType;
                    unpress();
                } else if (reset.isPressed()) {
                    //reset the design rule points
                    originRules = new ArrayList<Point>();
                    unpress();
                } else if (iterUp.isPressed()) {
                    iter += 1;
                    unpress();
                } else if (iterDown.isPressed()) {
                    iter -= 1;
                    unpress();
                } else if (showTypeUp.isPressed()) {
                    showType += 1;
                    unpress();
                } else if (showTypeDown.isPressed()) {
                    showType -= 1;
                    unpress();
                } else if (rotUp.isPressed()) {
                    rot += 0.01;
                    unpress();
                } else if (rotDown.isPressed()) {
                    rot -= 0.01;
                    unpress();
                } else if (exactPoint.isPressed()) {
                    //wait for button release becouse JOptionPane.showInputDialog pauses program
                    unpress();
                    //get the code to build the point
                    String input = JOptionPane.showInputDialog(null, "a,b,c...\na=length multipule\nb=rotation (0-1)\nc=movement\nfractions accepted");
                    //strings allowed to be in import code
                    String[] toKeep = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", ",", "/", "-"};
                    //remove all non allowed string from the input code to set up array
                    input = cleanUpString(toKeep, input);
                    //converty to an array
                    String[] in = input.split(",");
                    for (int i = 0; i < Math.floor(in.length / 3d); i++) {
                        //take in the strings and convert them to doubles even if there fractions
                        Point p = seed.relativeBuild(evaluateString(in[3 * i]), evaluateString(in[3 * i + 1]) * (2 * Math.PI)).getEndPoint();
                        p.rot = evaluateString(in[3 * i + 2]);
                        //add the points to the rules
                        originRules.add(p);
                    }
                } else if (importDesign.isPressed()) {
                    //wait for button release becouse JOptionPane.showInputDialog pauses program
                    unpress();
                    //get the code to build the rule
                    String input = JOptionPane.showInputDialog(null, "Paste Save Code");
                    //strings allowed to be in import code
                    String[] toKeep = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", ",", "true", "false", "-"};
                    //remove all non allowed string from the input code to set up array
                    input = cleanUpString(toKeep, input);
                    //converty to an array
                    String[] in = input.split(",");
                    //take in each peice of array and set up options
                    iter = Integer.parseInt(in[0]) - 1;
                    drawType = Integer.parseInt(in[1]);
                    doShowType = Boolean.parseBoolean(in[2]);
                    showType = Integer.parseInt(in[3]);
                    rot = Double.parseDouble(in[4]);
                    seed = new Vector(Double.parseDouble(in[5]), Double.parseDouble(in[6]), Double.parseDouble(in[7]), Double.parseDouble(in[8]));
                    //clear origin rules
                    originRules = new ArrayList<Point>();
                    //set up origin rules with the rest of the code
                    for (int i = 0; i < (in.length - 9) / 3; i++) {
                        originRules.add(new Point(Double.parseDouble(in[9 + 3 * i]) + 0.5, Double.parseDouble(in[10 + 3 * i]) + 0.5, Double.parseDouble(in[11 + 3 * i])));
                    }
                } else if (exportDesign.isPressed()) {
                    //wait becouse JOptionPane.showMessageDialog will pause program
                    unpress();
                    //assemble the string to export out
                    String export = "" + (iter + 1) + "," + drawType + "," + doShowType + "," + showType + "," + rot + ",\n" + seed + ",\n" + "Points:{\n";
                    //add on the rule points
                    for (Point p : originRules) {
                        export += " " + p.copyShift(-0.5, -0.5) + ",\n";
                    }
                    //chop off last comma and newline
                    export = export.substring(0, export.length() - 2);
                    //add on newline and end square bracket
                    export += "\n}";
                    //set up a JTextPane so the code is copyable
                    JTextPane textPane = new JTextPane();
                    textPane.setText(export);
                    //output the code to the user
                    JOptionPane.showMessageDialog(null, textPane);
                } else if (StdDraw.isMousePressed()) {
                    // this code handles adding a new point and moveing + deleteing current points
                    //used to break out of multipul loops at once
                    breakOut = false;
                    //we assume ther is a new point and dont make one if we decide to move a point
                    newPoint = true;
                    //for each point in the design
                    for (int i = 0; i < originRules.size(); i++) {
                        Point p = originRules.get(i);
                        //check if were close enough to drag the point
                        if (p.distTo(mousePoint()) < 0.02) {
                            //dont make new point
                            newPoint = false;
                            //while mouse is pressed we move point
                            while (StdDraw.isMousePressed() && !breakOut) {
                                // if point is dragged into deleted bar then remove it and stop moving it
                                if (mousePoint().y > 0.95) {
                                    originRules.remove(i);
                                    breakOut = true;
                                }
                                //clear the screen
                                StdDraw.clear();
                                //draw the deletion bar
                                StdDraw.text(0.5, 0.975, "Drag here to delete");
                                StdDraw.line(0, 0.95, 1, 0.95);
                                //move the dragging point to the mouse point
                                p.x = mousePoint().x;
                                p.y = mousePoint().y;
                                //save the size of the pen
                                double oldsize = StdDraw.getPenRadius();
                                //change the pen
                                StdDraw.setPenRadius(0.01);
                                //draw the point
                                p.draw();
                                //change the pen size back
                                StdDraw.setPenRadius(oldsize);
                                //draw the lines from the design
                                for (Point drawP : originRules) {
                                    StdDraw.line(seed.getEndPoint().x, seed.getEndPoint().y, drawP.x, drawP.y);
                                }
                                //draw the seed
                                seed.draw();
                                //show the screen
                                StdDraw.show();
                            }
                        }
                    }

                    //if we are still makeing a new point then add one
                    if (newPoint == true) {
                        originRules.add(new Point(StdDraw.mouseX(), StdDraw.mouseY(), rot));
                    }
                    unpress();
                }

                //clear screen
                StdDraw.clear();
                //draw designs
                //draw the info text
                StdDraw.text(0.5, 0.95, "Design Rule");
                StdDraw.textLeft(0.01, 0.975, "Click anywhere to add point");
                StdDraw.textLeft(0.01, 0.95, "or drag any current point");
                StdDraw.textLeft(0.01, 0.925, "then click Generate Design");
                //draw the stats text
                StdDraw.text(0.9, 0.025, "Iteration: " + (iter + 1));
                StdDraw.text(0.9, 0.05, "Show Type: " + (doShowType ? "One Point" : "All Points"));
                StdDraw.text(0.9, 0.075, "Show Only Number: " + showType);
                StdDraw.text(0.9, 0.1, "Movement :" + rot);
                StdDraw.text(0.9, 0.125, "draw type: " + (drawType == 0 ? "Line" : "Point"));
                //draw the lines of the rule
                for (Point p : originRules) {
                    StdDraw.line(seed.getEndPoint().x, seed.getEndPoint().y, p.x, p.y);
                    StdDraw.text(p.x, p.y, "" + p.rot);
                }
                //draw all of the buttons
                drawButton.draw();
                reset.draw();
                exportDesign.draw();
                importDesign.draw();
                switchDraw.draw();
                switchshowType.draw();
                seed.draw();
                iterUp.draw();
                iterDown.draw();
                showTypeUp.draw();
                showTypeDown.draw();
                rotUp.draw();
                rotDown.draw();
                exactPoint.draw();
                //show the screen
                StdDraw.show();
                //clear it to set up for the nest screen
                StdDraw.clear();
            } else if (screen == "draw") {
                //rotate the points
                for (Point p : rulePoints) {
                    p.rotate(seed.getEndPoint(), p.rot);
                }
                //drawing screen
                unpress();
                if (drawType == 0) {
                    StdDraw.clear();
                }
                //draw design button
                if (!exportGif) {
                    design.draw();
                    exportToGif.draw();
                }

                //this draws the text on the side of the output screen
                StdDraw.textLeft(0.01, 0.975, "" + (iter + 1) + "," + drawType + "," + doShowType + "," + showType + "," + ((double) ((int) (rot * 1000.0))) / 1000.0 + ",");
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
                if (originRules.isEmpty()) {
                    StdDraw.textLeft(0.01, 0.9, "}");
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

                        }
                        //point draw type
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
                StdDraw.show();
                if (imageNum < framesToSave && exportGif) {
                    //add the image to gif
                    writer.writeToSequence((RenderedImage) StdDraw.getImage());
                    //set the image number up one
                    imageNum++;
                    StdDraw.clear();
                }
                if (!(imageNum < framesToSave) && exportGif) {
                    // turn off export
                    exportGif = false;
                    //close the writer and output
                    writer.close();
                    output.close();
                    //go back to design screen
                    screen = "design";
                }
                if (design.isPressed() && !exportGif) {
                    //change screen back to design
                    screen = "design";
                    unpress();
                } else if (exportToGif.isPressed() && !exportGif) {
                    //say we want to export gif
                    exportGif = true;
                    //find the smallest rot on all of the rules
                    double minRot = 1;
                    for (Point p : originRules) {
                        if (p.rot < minRot && p.rot != 0d) {
                            minRot = p.rot;
                            System.out.println(minRot);
                        }
                    }
                    //use the smallest rot to find how many frames for one loop
                    framesToSave = (int) Math.round((Math.PI * 2) / minRot);
                    //creat new output and writer
                    output = new FileImageOutputStream(new File("savedGif" + System.currentTimeMillis() + ".gif"));
                    writer = new GifSequenceWriter(output, 5, 50, true);
                    //reset image num
                    imageNum = 0;
                    StdDraw.clear();
                }
            }
        }
    }
    //stop progrtam untill mouse is released

    private static void unpress() {
        while (StdDraw.isMousePressed()) {
        }
    }
    //relplaces code: new Point(StdDraw.mouseX(),StdDraw.mouseY())
    //with mousePoint()

    private static Point mousePoint() {
        return new Point(StdDraw.mouseX(), StdDraw.mouseY());
    }
    //removes everything from the string foo except the strings specified in the array

    private static String cleanUpString(String[] strings, String foo) {
        //sort the array by length, so we check the longest string first.
        Arrays.sort(strings, (a, b) -> b.length() - a.length());
        String newFoo = "";
        for (int i = 0; i < foo.length();) {
            int j = i;
            for (String s : strings) {
                //find subString instead of character.
                if (foo.length() >= i + s.length()) {
                    String sub = foo.substring(i, i + s.length());
                    if (sub.equals(s)) {
                        //move to the next index. Ex if 10 is at 0, next check start at 2
                        i += sub.length();
                        newFoo += sub;
                        break;
                    }
                }
            }
            // check the index if it has been modified
            i = i == j ? ++j : i;
        }
        return newFoo;
    }

    private static double evaluateString(String in) {
        double out = 0;
        //check if fraction or double
        if (in.contains("/")) {
            //split by the / and convet each to double then devide
            out = Double.parseDouble(in.split("/")[0]) / Double.parseDouble(in.split("/")[1]);
        } else {
            //treat as just double
            out = Double.parseDouble(in);
        }
        return out;
    }
}
