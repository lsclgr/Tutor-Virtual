package controler;

import hpa.facebook.TarefaFacebookCorrecao;
import hpa.facebook.TarefaFacebookScore;
import hpa.facebook.TarefaFacebookStart;
import hpa.facebook.TarefaFacebookQEscrita;
import hpa.facebook.TarefaFacebookQFalada;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import modelo.Usuario;
import tutorvirtual.Execucao;
import static tutorvirtual.Execucao.trabalhadores;

/**
 *
 * @author Luísa
 */
public class FacebookServer extends Thread {

    public static void acorda() {
        System.out.println("Acordando os workers para trabalhar.");
        for (int i = 0; i < trabalhadores.size(); i++) {
            trabalhadores.get(i).acorda();
        }
    }

    @Override
    public void run() {
        ServerSocket server;
        Socket client;
        InputStream input;

        try {
            //porta 1010
            server = new ServerSocket(1010);
            while (true) {
                //conexão com o webhook do facebook chegou
                client = server.accept();

                //lendo a informação que veio da rede
                input = client.getInputStream();
                String infoFacebook = transformaTexto(input);

                //aaqui vamos repassar para o Tutor Virtual
                System.out.println(infoFacebook);

                String tokensInfo[] = infoFacebook.split("<>");
                String mens[] = tokensInfo[1].split("\n");

                Usuario consulta = UsuarioService.recuperaUsuario(Long.parseLong(tokensInfo[0]));
                System.out.println(consulta);

                if (consulta == null) {
                    System.out.println("consuta nula");
                    TarefaFacebookStart novaTarefa = new TarefaFacebookStart(tokensInfo[0], mens[0], "my friend", client);
                    novaTarefa.execute();
                    //Execucao.sacolaTarefas.add(novaTarefa);
                    //acorda();

                } else {

                    //System.out.println(tokensInfo[1]);
                    switch (mens[0]) {

                        case "Write Question": {
                            TarefaFacebookQEscrita qescrita = new TarefaFacebookQEscrita(tokensInfo[0], mens[0], "my friend", client);
                            qescrita.execute();
//Execucao.sacolaTarefas.add(qescrita);
                            //acorda();
                        }
                        break;

                        case "pergunta_falada": {

                        }
                        break;

                        case "Score": {
                            TarefaFacebookScore score = new TarefaFacebookScore(tokensInfo[0], mens[0], "my friend", client);
                            score.execute();
//Execucao.sacolaTarefas.add(score);
                            //acorda();
                        }
                        break;

                        default: {
                            if (consulta != null) {
                                TarefaFacebookCorrecao corrige = new TarefaFacebookCorrecao(tokensInfo[0], mens[0], "my friend", client);
                                corrige.execute();
//Execucao.sacolaTarefas.add(corrige);
                                //acorda();
                            }
                        }
                    }
                }

                //chamada de algum método do Tutor para que possamos enviar como resposta
                //teste para enviar pelo mesmo socket
                //respostaTutor(client, "Chegou no servidor e voltou para o cliente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String transformaTexto(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder info = new StringBuilder();
        String line = null;

        info.append(br.readLine() + "\n");

        return info.toString();
    }

   

    @Deprecated
    public static void enviaWebhookFacebook(String retornoTxt) {
        try {
            Socket conexao = new Socket("localhost", 1011);

            OutputStream outstream = conexao.getOutputStream();
            PrintWriter out = new PrintWriter(outstream);

            out.print(retornoTxt);
            out.flush();

        } catch (java.net.ConnectException ex) {
            System.err.println("Checar o servidor que comunica com o Facebook...");
        } catch (IOException ex) {
            System.err.println("Dados corrompidos ou não entregues...");
            ex.printStackTrace();
        }
    }
}
