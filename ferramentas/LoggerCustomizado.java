/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferramentas;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import tutorvirtual.Execucao;

/**
 *
 * @author User
 */
public class LoggerCustomizado {
        public static Logger criaLog(String nomeClasse) {
        Logger log = Logger.getLogger(nomeClasse);
        log.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        FileHandler fh;
        try {
            //Iniciado no NetBeans...
            //File pastaLog = new File(System.getProperty("user.dir")+File.separator+"log"+File.separator);
            //Iniciado por script (Linux)... 
            File pastaLog = new File(System.getProperty("user.dir") + File.separator +"home" + File.separator +"iagoilacerda"+ File.separator 
                + "TutorVirtual"+File.separator+"log"+File.separator);
            if(!pastaLog.exists()){
                pastaLog.mkdir();
            }
            fh = new FileHandler(pastaLog.getAbsolutePath()+File.separator+
                    java.time.LocalDate.now()+".log",true);
            fh.setFormatter(new SimpleFormatter());
            log.addHandler(fh);
        } catch (IOException ex) {
            log.log(Level.INFO, null, ex);
        } catch (SecurityException ex) {
            log.log(Level.INFO, null, ex);
        }
        return log;
    }
}
