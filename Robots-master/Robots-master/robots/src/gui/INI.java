package gui;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class INI {
    public ArrayList<Object_Window> Obj;
    public static String path = "option.ini";

    public ArrayList<Object_Window> LoadINI() throws IOException {
  try {
      List<String> file = Files.readAllLines(Paths.get(path), UTF_8);
      Obj = new ArrayList<Object_Window>();
      for (int i = 0; i < file.size(); i++) {
          String t = file.get(i++);
          int h = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
          int w = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
          int x = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
          int y = Integer.parseInt(file.get(i).replaceAll("[\\D]", ""));
          Object_Window tmp =new Object_Window(t, x, y, h, w);
          if (t.equals("Игровое поле")) {
              i++;
               t = file.get(i++);
                h = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
               w = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
               x = Integer.parseInt(file.get(i++).replaceAll("[\\D]", ""));
               y = Integer.parseInt(file.get(i).replaceAll("[\\D]", ""));
               tmp.podWindow=new Object_Window(t, x, y, h, w);
          }
          Obj.add(tmp);

      }
      return Obj;
  } catch (IOException ex) {
      return null;
  }

    }



    public void Save(){

        try(FileWriter writer = new FileWriter(path,false))
        {
            if(Obj.size()==0) writer.write("");
            else
            for (int i=0; i< Obj.size();i++) {
                String t = Obj.get(i).Title;
                int x=Obj.get(i).X;
                int y=Obj.get(i).Y;
                int w=Obj.get(i).W;
                int h=Obj.get(i).H;
                String text = t +"\nH = "+ h+ "\n" + "W = " + w + "\n" + "X = "+x + "\n" + "Y = "+ y;
                writer.write(text);
                if (t=="Игровое поле") {
                     t = Obj.get(i).podWindow.Title;
                     x=Obj.get(i).podWindow.X;
                     y=Obj.get(i).podWindow.Y;
                     w=Obj.get(i).podWindow.W;
                     h=Obj.get(i).podWindow.H;
                    text ="\n"+ t +"\nH = "+ h+ "\n" + "W = " + w + "\n" + "X = "+x + "\n" + "Y = "+ y;
                    writer.write(text);
                }
                if (i+1 != Obj.size()) writer.write("\n");
            }

            writer.flush();
        }
        catch(IOException ex){

            return;
        }
    }

    public void addRecord(String title, int x, int y, int h, int w,Object_Window gameInfo){
        if(Obj==null) Obj = new ArrayList<Object_Window>();
        if (title!=null){
            Object_Window item = new Object_Window(title,x,y,h,w);
            if (title.equals("Игровое поле") && gameInfo != null) item.podWindow=gameInfo;
            Obj.add(item);
        }
    }


}
