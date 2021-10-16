package aspwil.fractamatelegacy;

//this class is a vector, the data is stored as a x,y value, a length, and a rotation
//it aslo incude methods for turning 2 points into a vector, and getting the end point of a vector

import java.lang.Math;
public class Vector{
   private static double PI = Math.PI; 
   public double x, y, length, angle;
   //standard constructer
   public Vector(double INx, double INy, double INlength, double INangle){
      this.x = INx;
      this.y = INy;
      this.length = INlength;
      this.angle = INangle%360;
   }
   //dual point constructer
   public Vector(Point p1, Point p2){
      this.x = p1.x;
      this.y = p1.y;
      this.length = Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
      this.angle = getAngle(p1.x-p2.x,p1.y-p2.y);
   }
   //copy constructor
   public Vector(Vector copy) {
      this.x = copy.x;
      this.y = copy.y;
      this.angle = copy.angle;
      this.length = copy.length;  
   }
   //used to obtain angle compaired to x+ axis from the x amd y dist
   private double getAngle(double xDist, double yDist){
      double newAngle = 0d;
      if (xDist == 0d && yDist == 0d){
         System.out.println("WARN @ class Vector warn#1 : x && y Dist == 0, angle assumed 0");
      } 
      else if (xDist > 0d && yDist == 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = PI;
      } 
      else if (xDist == 0d && yDist > 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = 3*PI/2;
      } 
      else if (xDist < 0d && yDist == 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = 0d;
      } 
      else if (xDist == 0d && yDist < 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = PI/2;
      } 
      else if (xDist > 0d && yDist > 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = PI+Math.atan(yDist/xDist);
      } 
      else if (xDist < 0d && yDist > 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = 2*PI-Math.atan(yDist/xDist);
      } 
      else if (xDist > 0d && yDist < 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = PI-Math.atan(yDist/xDist);
      } 
      else if (xDist < 0d && yDist < 0d){
         xDist = abs(xDist);
         yDist = abs(yDist);
         newAngle = Math.atan(yDist/xDist);
      }
   
      return newAngle;
   }
   //get end point
   public Point getEndPoint(){
      angle = angle%(PI*2);
      if (angle < 0d){
         angle = angle+2*PI*(Math.floor(angle/(PI*-2))+1);
      }
      Point endPoint = new Point(x,y);
      if(angle == 0d){
         endPoint = new Point(x+length,y);
      } 
      else if (angle == PI/2){
         endPoint = new Point(x,y+length);
      } 
      else if (angle == PI){
         endPoint = new Point(x-length,y);
      } 
      else if (angle == 3*PI/2){
         endPoint = new Point(x,y-length);
      } 
      else {
         switch ((int) Math.floor(angle/(PI/2))){
            case 0:
               endPoint = new Point(x+length*Math.cos(angle),y+length*Math.sin(angle));
               break;
            case 1:
               endPoint = new Point(x+length*Math.sin(angle+PI/2),y-length*Math.cos(angle+PI/2));
               break;
            case 2:
               endPoint = new Point(x-length*Math.cos(angle+PI),y-length*Math.sin(angle+PI));
               break;
            case 3:
               endPoint = new Point(x-length*Math.sin(angle+3*PI/2),y+length*Math.cos(angle+3*PI/2));
               break;
         }
      }
      return endPoint;
   }
   //Math.abs() shortcut
   private double abs(double Value){
      return Math.abs(Value);
   }
   // toString
   //print out the vectors data in the form V: (X, Y, LENGTH, ANGLE)
   @Override
   public String toString(){
      return new String("V:("+
              ((double)((int)(x *10000000.0)))/10000000.0+","+
              ((double)((int)(y *10000000.0)))/10000000.0+","+
              ((double)((int)(length *10000000.0)))/10000000.0+","+
              ((double)((int)(angle *10000000.0)))/10000000.0+")");
   }
   //draw, relies on standard draw and draws a line
   public void draw(){
      //fix the angle if it broken
      angle = rectifyAngle(angle);
      Point end = getEndPoint();
      StdDraw.line(x,y,end.x,end.y);
   }
   //makes angle within correct range of possible angles
   public static double rectifyAngle(double in){
      in = in%(PI*2);
      if (in < 0d){
         in = in+2*PI*(Math.floor(in/(PI*-2))+1);
      }
      return in;
   }
   // builds a vector from the end of this one to the point secified
   public Vector build(double px, double py){
      return new Vector(getEndPoint(),new Point (px,py));
   }
   public Vector build(Point p){
      return build(p.x, p.y);
   }
   //builds a vector by changeing its length and angle relative to this vector
   public Vector relativeBuild(double lmult, double achange){
      return new Vector(getEndPoint().x,getEndPoint().y,length*lmult,angle+achange);
   }
   public static Vector sum(Vector[] toSum){
      double totalX = 0;
      double totalY = 0;
      for(Vector v : toSum){
         totalX += v.getEndPoint().x - v.x;
         totalY += v.getEndPoint().y - v.y;
      }
   
      return new Vector(new Point(toSum[0].x, toSum[0].y), new Point(toSum[0].x + totalX, toSum[0].y + totalY)); 
   }
   //add vector toAdd onto the end of this vector
   public Vector add(Vector toAdd){
      Vector[] foo = {this,toAdd};
      return sum(foo);
   }
}