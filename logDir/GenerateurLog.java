import java.util.ArrayList;
import java.io.*;

public class GenerateurLog{
  private ArrayList<JoueurLog> ListJoueur = new ArrayList<JoueurLog>();
  private ArrayList<ProducteurLog> ListProducteur = new ArrayList<ProducteurLog>();

  public static void main(String[] args){
    BufferedReader bis =null;
    try{
      bis = new BufferedReader(new FileReader(new File(args[0])));
      String line;
      while((line = bis.readLine()) != null){

        System.out.println(line);
        if(line.equals("Producteurs :")){
          System.out.println("Liste Prod :");
          LectureProducteur(bis);
        }
        if(line.equals("Joueur :")){
          System.out.println("Liste Joueur :");
          LectureJoueur(bis);
        }

      }
    }
    catch (FileNotFoundException e){e.printStackTrace();}
    catch (IOException e){e.printStackTrace();}
    finally{
      try{
        if(bis != null)
          bis.close();
      } catch (IOException e){ e.printStackTrace(); }
    }
    try{
      if(bis != null)
        bis.close();
    } catch (IOException e){ e.printStackTrace(); }
  }

  public static void LectureProducteur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          System.out.println("Fin des prod");
          return;
        }
        System.out.println(line);
      }
    }catch (IOException e){e.printStackTrace();}

  }

  public static void LectureJoueur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          System.out.println("Fin des joueur");
          return;
        }
        System.out.println(line);
      }
    }catch (IOException e){e.printStackTrace();}

  }


}
