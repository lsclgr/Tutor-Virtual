/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.facebook;

import com.vdurmont.emoji.EmojiParser;
import controler.QuestaoService;
import controler.UsuarioService;
import static hpa.facebook.TarefaFacebook.respostaTutor;
import java.io.File;
import java.net.Socket;
import modelo.Questao;
import modelo.Usuario;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 *
 * @author Luísa
 */
public class TarefaFacebookQFalada extends TarefaFacebook{

    public TarefaFacebookQFalada(String psid_, String mens_, String nome_, Socket resposta) {
        super(psid_, mens_, nome_, resposta);
    }

    @Override
    public void execute() {
        
        Usuario usuario = new Usuario(Long.parseLong(super.psid), "my friend", 0, 1, 0, false);
        usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        Questao q = QuestaoService.buscaQuestaoNaoRespondida(usuario.getChatId(), usuario.getNivel(), 0);
        if (q == null) {
            respostaTutor(super.canalResposta, "Você já realizou todas questões cadastradas até o momento.");
            return;
        }
        
    
    }
    
    private void enviaVoz(String referencia, long chatId) {
        Usuario usuario = new Usuario(Long.parseLong(super.psid), "my friend", 0, 1, 0, false);
        usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        respostaTutor(super.canalResposta, "Vou exercitar minhas cordas vocais para te mandar um áudio perfeito. :relieved:");
        
        
    }

    @Override
    public String getNomeTarefa() {
         return "/pergunta_falada";
    }
    
}
