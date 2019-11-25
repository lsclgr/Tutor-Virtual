/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.facebook;

import hpa.Tarefa;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class TarefaFacebook implements Tarefa{

    protected String psid;
    protected String mens;
    protected String nome;
    protected Socket canalResposta;

    public TarefaFacebook(String psid_, String mens_, String nome_, Socket resposta) {
        this.psid = psid_;
        this.mens = mens_;
        this.nome = nome_;
        this.canalResposta = resposta;
    }

    public void setPsid(String psid) {
        this.psid = psid;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPsid() {
        return this.psid;
    }

    public String getMens() {
        return this.mens;
    }

    public String getNome() {
        return this.nome;
    }


    @Override
    public String getPlataforma() {
        return "Facebook";
    }
    
     public static void respostaTutor(Socket con, String resposta) {
        OutputStream outstream;
        try {
            outstream = con.getOutputStream();

            PrintWriter out = new PrintWriter(outstream);

            out.print(resposta + "\n");
            out.flush();
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

}
