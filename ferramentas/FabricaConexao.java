package ferramentas;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FabricaConexao {

    private static int quantidadeConexoes;
    //private static Logger log = LoggerCustomizado.criaLog(FabricaConexao.class.getName());

    public static Connection criaConexao() {

        //tratando problema relacionado ao limite de conexoes simultaneas
        while (quantidadeConexoes >= 200) {
            System.err.println("Estamos no limite de nossas conexões. Tentaremos após 1 minuto");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                //log.log(Level.FINE, null, ex);
            }
        }

        Connection con = null;
        boolean conectado = false;
        int tentativas = 0;
        while (conectado == false && tentativas <= 4) {
            try {
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TutorVirtual", "postgres", "go");
                conectado = true;
            } catch (SQLException ex) {
                tentativas++;

               // log.fine("Conexao falhou - problema rede ou senha");
                //log.fine(ex.getMessage());
                try {
                    //log.log(Level.FINE, "Tentaremos restabelecer em 30 segundos. Estamos na tentativa {0}.", tentativas);
                    Thread.sleep(30000);
                } catch (InterruptedException ex1) {
                    //log.fine("Problema de execução.");
                }

            }
        }
        quantidadeConexoes++;
        return con;
    }

    public static void encerraConexao() {
        quantidadeConexoes--;
    }
}
