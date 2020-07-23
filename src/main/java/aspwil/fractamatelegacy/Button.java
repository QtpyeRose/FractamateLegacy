package aspwil.fractamatelegacy;

public class Button{
   public double x1,y1,x2,y2;
   public String text;
   //point constructer
   public Button(Point p1, Point p2, String txt){
      this.x1 = p1.x;
      this.y1 = p1.y;
      this.x2 = p2.x;
      this.y2 = p2.y;
      this.text = txt;
   
   }
   //doubles constructer
   public Button(double X1, double Y1, double X2, double Y2, String txt){
      this(new Point(X1,Y1), new Point(X2,Y2), txt);
   }
   //returns water the button is being pressed
   public boolean isPressed(){
      double x = StdDraw.mouseX();
      double y = StdDraw.mouseY();
      return (StdDraw.isMousePressed() && x > x1 && x < x2 && y > y1 && y < y2);
   }
   //draws the button
   public void draw(){
      //draw outside
      StdDraw.rectangle((x1+x2)/2, (y1+y2)/2, (x2-x1)/2, (y2-y1)/2);
      //draw text
      StdDraw.text((x1+x2)/2, (y1+y2)/2, text);
   }
   //converts to string
   public String toString(){
      return "B:{"+x1+","+y1+","+x2+","+y2+","+text+"}";
   }

}