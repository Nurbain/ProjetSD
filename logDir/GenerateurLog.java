import java.util.ArrayList;
import java.io.*;

public class GenerateurLog{
  private ArrayList<JoueurLog> ListJoueur = new ArrayList<JoueurLog>();
  private ArrayList<ProducteurLog> ListProducteur = new ArrayList<ProducteurLog>();

  public static void main(String[] args){
    FileInputStream fis = null;
    try{
      fis = new FileInputStream(new File(args[0]));
      byte[] buf = new byte[8];
      while((fis.read(buf)) >= 0){
        for(byte bit : buf){
          System.out.print((char) bit);
        }
        buf = new byte[8];
      }
    }
    catch (FileNotFoundException e){e.printStackTrace();}
    catch (IOException e){e.printStackTrace();}
    finally{
      try{
        if(fis != null)
          fis.close();
      } catch (IOException e){ e.printStackTrace(); }
    }
    try{
      if(fis != null)
        fis.close();
    } catch (IOException e){ e.printStackTrace(); }
  }


}
