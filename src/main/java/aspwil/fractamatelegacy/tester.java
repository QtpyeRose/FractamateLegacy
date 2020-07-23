package aspwil.fractamatelegacy;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
class tester{
   //rulePoints.add(new Vector(0.5,0.5,seed.length/2,seed.angle).getEndPoint()); //up
   //rulePoints.add(new Vector(0.5,0.5,seed.length/2,seed.angle+(2*Math.PI/3)).getEndPoint());//1/3 rortate
   //rulePoints.add(new Vector(0.5,0.5,seed.length/2,seed.angle+(4*Math.PI/3)).getEndPoint());//2/3 rortate
   public static void main(String[] args){
      int num = 5;
      String[] points = new String[num];
      for(int i = 0; i < num; i ++){
         points[i] = r(1/2d,i/(double)num);
      }
      String export = "";
      for(String s : points){
         export += " "+s+",\n";
      }
      export = export.substring(0,export.length()-2);
      System.out.println(export);
      
   }
   private static String r(double lmult, double achange){
      Vector seed = new Vector(new Point(0.5,0.25),new Point(0.5,0.5));
      Point p = seed.relativeBuild(lmult,achange*(2*Math.PI)).getEndPoint();
      p.rot = 0.04;
      p.x-= 0.5;
      p.y-= 0.5;
      return p.toString();
   }
}