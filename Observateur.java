import java.rmi.RemoteException;

public class Observateur extends Client implements ObservateurInterface{
  static final long serialVersionUID = 42;
  Fin typeFin;

  public Observateur(String name,Fin typeFin) throws RemoteException{
    super(name);
    this.typeFin=typeFin;
  }

  public void generationLog(){

  }

}