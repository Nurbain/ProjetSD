import java.rmi.RemoteException;

public class Observateur extends Client{
  static final long serialVersionUID = 42;
  Fin typeFin;

  public Observateur(String name,Fin typeFin) throws RemoteException{
    super(name);
    this.typeFin=typeFin;
    this.monType=Type.Observateur;
  }

  public void generationLog(){

  }

  public void startAgent(){
    System.out.println(name + " Debut partie");
    try{
      for(int i=0;i < ListJoueur.size();i++){
        ListJoueur.get(i).startAgent();
      }
      for(int i=0;i < ListProducteur.size();i++){
        ListProducteur.get(i).startAgent();
      }
    }catch (RemoteException re) { System.out.println(re) ; }


  }

}
