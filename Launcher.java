
import java.io.*;

class AfficheurFlux implements Runnable {

    private final InputStream inputStream;

    AfficheurFlux(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        BufferedReader br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Launcher{
	public static void main(String args[]){
		System.out.println("Début du programme");
        try {
            String[] commande = {"java", "ClientServ","8080","C1"};
            Process p = Runtime.getRuntime().exec(commande);
            AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
            AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());

            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();

            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Fin du programme");
	}
}
