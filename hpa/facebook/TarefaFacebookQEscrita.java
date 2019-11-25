/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.facebook;

import controler.QuestaoService;
import controler.UsuarioService;
import java.net.Socket;
import modelo.Questao;
import modelo.Usuario;

/**
 *
 * @author Luísa
 */
public class TarefaFacebookQEscrita extends TarefaFacebook{

    public TarefaFacebookQEscrita(String psid_, String mens_, String nome_, Socket resposta) {
        super(psid_, mens_, nome_, resposta);
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        Questao q = QuestaoService.buscaQuestaoNaoRespondida(Long.parseLong(super.psid), usuario.getNivel(), 0);
        if (q == null) {
            respostaTutor(super.canalResposta, "You have already completed all registered questions so far.");
            return;
        }

        respostaTutor(super.canalResposta, q.toString());
        usuario.setIdUltimaQuestao(q.getId());
        usuario.setCorrigirResposta(true);
        UsuarioService.atualizaUsuario(usuario);

        //adicionar os botões
    }

    @Override
    public String getNomeTarefa() {
        return "/pergunta_escrita";
    }
    
}
