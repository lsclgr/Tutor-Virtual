/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferramentas;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import modelo.Alternativa;
import modelo.Transcricao;

public class Recognition {

    //private static Logger log = LoggerCustomizado.criaLog(Recognition.class.getName());
    public static Transcricao transcreverVoz(String fileName, List<Alternativa> alternativas) {

        //programa com os respectivos parâmetros
        //log.fine(System.getProperty("user.dir"));
        /*String comando = "java -jar " + System.getProperty("user.dir") + File.separator +"home" + File.separator +"iagoilacerda"+ File.separator 
                + "TutorVirtual"+ File.separator + "TestRecognition.jar ";*/
        String comando = "java -jar \"" + System.getProperty("user.dir") + "\"" + File.separator + "TestRecognition.jar ";

        //parametros devem ser separados por espaco
        String parametros = fileName;

        try {
            Vector<String> resultsString = new Vector<>();
            Process novoprocesso = Runtime.getRuntime().exec(comando + "\"" + parametros + "\"");

            //leitura da saída padrão do processo
            BufferedReader bufferSaida = new BufferedReader(new InputStreamReader(novoprocesso.getInputStream()));
            String resultado;
            while ((resultado = bufferSaida.readLine()) != null) {
                //log.log(Level.FINE, "resultado = {0}", resultado);
                resultsString.add(resultado);
            }
            bufferSaida.close();

            //leitura da saída de erros do processo 
            bufferSaida = new BufferedReader(new InputStreamReader(novoprocesso.getErrorStream()));
            while ((resultado = bufferSaida.readLine()) != null) {
                System.out.println(resultado);
                //log.log(Level.FINE, "resultado = {0}", resultado);
            }
            bufferSaida.close();
            return getMelhorTranscricao(resultsString, alternativas);
        } catch (IOException ex) {
            //log.fine(ex.getMessage());
            return null;
        }
    }

    public static Transcricao getMelhorTranscricao(Vector<String> possiveisTranscricoes, List<Alternativa> alternativas) {
        int maiorPorcentagemAcertos = 0;
        Transcricao melhorTranscricao = possiveisTranscricoes.isEmpty() ? null : new Transcricao(possiveisTranscricoes.get(0), -1);

        for (int i = 0; i <= possiveisTranscricoes.size() - 1; i++) {
            String[] palavrasTranscricao = possiveisTranscricoes.get(i).split(" ");
            for (int alt = 0; alt <= 2; alt++) {
                String[] palavrasAlternativa = alternativas.get(alt).getEnunciado().split(" ");
                int acertos = 0;

                for (int iVerif = 0; iVerif < palavrasAlternativa.length; iVerif++) {

                    if ((iVerif < palavrasTranscricao.length) && (palavrasAlternativa[iVerif].equalsIgnoreCase(palavrasTranscricao[iVerif]) || 
                           ((iVerif - 1 >= 0) &&  (palavrasAlternativa[iVerif].equalsIgnoreCase(palavrasTranscricao[iVerif-1]))) || 
                            ((iVerif + 1 < palavrasTranscricao.length) && palavrasAlternativa[iVerif].equalsIgnoreCase(palavrasTranscricao[iVerif+1])))) {
                           
                        acertos++;
                    }
                }
                if (acertos > maiorPorcentagemAcertos) {
                    maiorPorcentagemAcertos = acertos;
                    melhorTranscricao.setTextoTranscricao(possiveisTranscricoes.get(i));
                    melhorTranscricao.setIndiceAlternativaReferente(alt);
                    melhorTranscricao.setPorcentagemAcertos(acertos / palavrasAlternativa.length * 100);
                }
            }
        }
        return melhorTranscricao ;
    }
}









/*public static Transcricao getMelhorTranscricao(Vector<String> possiveisTranscricoes, List<Alternativa> alternativas) {
        int maiorPorcentagemAcertos = 0;
        Transcricao melhorTranscricao = possiveisTranscricoes.isEmpty() ? null : new Transcricao(possiveisTranscricoes.get(0), -1);

        for (int i = 0; i <= possiveisTranscricoes.size() - 1; i++) {
            String[] palavrasTranscricao = possiveisTranscricoes.get(i).split(" ");
            for (int alt = 0; alt <= 2; alt++) {
                String[] palavrasAlternativa = alternativas.get(alt).getEnunciado().split(" ");
                int acertos = 0;
                int ultimoIndice = -1;

                for (int iPalavraAlternativa = 0; iPalavraAlternativa < palavrasAlternativa.length; iPalavraAlternativa++) {
                    if (ultimoIndice != palavrasTranscricao.length - 1) {
                        for (int iPalavraTranscricao = ultimoIndice + 1; iPalavraTranscricao < palavrasTranscricao.length; iPalavraTranscricao++) {
                            if (palavrasAlternativa[iPalavraAlternativa].equalsIgnoreCase(palavrasTranscricao[iPalavraTranscricao])) {
                                ultimoIndice = iPalavraTranscricao;
                                acertos++;
                                break;
                            } else if (iPalavraTranscricao == palavrasTranscricao.length - 1) {
                                ultimoIndice = palavrasTranscricao.length - 1;
                            }
                        }
                    } else {
                        break;
                    }
                }

                if (acertos > maiorPorcentagemAcertos) {
                    maiorPorcentagemAcertos = acertos;
                    melhorTranscricao.setTextoTranscricao(possiveisTranscricoes.get(i));
                    melhorTranscricao.setIndiceAlternativaReferente(alt);
                    melhorTranscricao.setPorcentagemAcertos(acertos/palavrasAlternativa.length*100);
                }
            }
        }
        
        return melhorTranscricao;
    }*/

