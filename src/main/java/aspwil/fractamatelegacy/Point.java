package aspwil.fractamatelegacy;

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
   //find the from this point distence to point p
   public double distTo(Point p){
      return Math.sqrt(Math.pow(x-p.x,2)+Math.pow(y-p.y,2));
   }
   //rotate this point around point (rx,ry) buy t degrese
   public void rotate(double rx, double ry, double t){
      double oldx = x;
      x = rx+(x-rx)*Math.cos(t)-(y-ry)*Math.sin(t);
      y = ry+(oldx-rx)*Math.sin(t)+(y-ry)*Math.cos(t);
   }
   //rotate this point around point p buy t degrese
   public void rotate(Point p, double t){
      rotate(p.x,p.y,t);
   }
   //convert to string
   public String toString(){
      return new String("P:("+
              ((double)((int)(x *1000.0)))/1000.0+","+
              ((double)((int)(y *1000.0)))/1000.0+","+
              rot+")");
   }
   //makes a copy but shifts the point
   public Point copyShift(double xs, double ys){
      return new Point(x+xs,y+ys,rot);   
   }
   //sums points
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