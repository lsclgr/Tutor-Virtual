package tutorvirtual;
//import ferramentas.LoggerCustomizado;
//import java.util.logging.Logger;
import controler.FacebookServer;
import controler.TelegramServer;

public class TutorVirtual {

    //private static Logger log = LoggerCustomizado.criaLog(TutorVirtual.class.getName());
    
    public static void main(String[] args) {
     
        FacebookServer servidorFace = new FacebookServer();
        servidorFace.start();
        
        TelegramServer servidorTelegram = new TelegramServer();
        servidorTelegram.start();
        
        try {
            //barreira de sincronização
            servidorFace.join();
            servidorTelegram.join();
        } catch (InterruptedException ex) {
            System.err.println("Erro na barreira de sicronização");
        }
    }
}
