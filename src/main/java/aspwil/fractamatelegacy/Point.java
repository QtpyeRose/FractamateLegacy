package aspwil.fractamatelegacy;

//this class defined a point object, it has a x value, a y value, and a rotation value, rotation is jsut used for spining.

public class Point{
   public double x,y,rot;
   //point with rotation constructer
   public Point(double INx, double INy, double INrot){
      this.x = INx;
      this.y = INy;
      this.rot = INrot;
   }
   //point without rotation constructer
   public Point(double INx, double INy){
      this(INx,INy,0);
   }
   //copy constructer
   public Point(Point copy){
      this(copy.x,copy.y,copy.rot);
   }
   
   //draw the point
   public void draw(){
      StdDraw.point(x,y);  
   }
   //find the distance from this point to point p
   public double distTo(Point p){
      return Math.sqrt(Math.pow(x-p.x,2)+Math.pow(y-p.y,2));
   }
   //call this method this point around the point (rx,ry) by t degrese
   //this will change the value of the point and wont return a new point
   public void rotate(double rx, double ry, double t){
      double oldx = x;
      x = rx+(x-rx)*Math.cos(t)-(y-ry)*Math.sin(t);
      y = ry+(oldx-rx)*Math.sin(t)+(y-ry)*Math.cos(t);
   }
   //rotate this point around point p buy t degrese
   //same this as other rotate just takes a point object instead
   public void rotate(Point p, double t){
      rotate(p.x,p.y,t);
   }
   //convert to string, this rounds to 7 decimal places and looks like this: P: (X, Y, ROT)
   public String toString(){
      return new String("P:("+
              ((double)((int)(x *10000000.0)))/10000000.0+","+
              ((double)((int)(y *10000000.0)))/10000000.0+","+
              rot+")");
   }
   //returns a shifted copy of this point (will not effect this points values)
   public Point copyShift(double xs, double ys){
      return new Point(x+xs,y+ys,rot);   
   }
   //sums points, this is a static method so dont call it as Point.rotSum(Point[]);
   public static Point rotSum(Point[] toSum){
      double totalX = 0;
      double totalY = 0;
      double totalRot = 0;
      for(Point p : toSum){
         totalX += p.x;
         totalY += p.y;
         totalRot += p.rot;
      }
      return new Point(totalX,totalY,totalRot);
   }
   //adds the point p 
   public Point rotAdd(Point p){
      Point[] points = {this,p};
      return rotSum(points);      
   }
   //sums points but uses the rot of the first point
   public static Point sum(Point[] toSum){
      return new Point(rotSum(toSum).x,rotSum(toSum).y,toSum[0].rot);
   }
   //adds the point p but keeps rot of this
   public Point add(Point p){
      Point[] points = {this,p};
      return sum(points);      
   }
}