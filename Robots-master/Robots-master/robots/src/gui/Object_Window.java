package gui;

public class Object_Window {
    public String Title;
    public int X;
    public int Y;
    public int H;
    public int W;
    public Object_Window podWindow;

    public Object_Window(String title, int x, int y, int h, int w){
        Title=title;
        X=x;
        Y=y;
        H=h;
        W=w;
    }
    public void addPodWindow(String title, int x, int y, int h, int w){
        podWindow=new Object_Window(title,x,y,h,w);
    }
    public Object_Window getPodWindow(){
        return podWindow;
    }
}
