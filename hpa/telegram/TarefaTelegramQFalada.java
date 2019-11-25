/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.telegram;

import com.vdurmont.emoji.EmojiParser;
import controler.QuestaoService;
import controler.UsuarioService;
import java.io.File;
import modelo.Questao;
import modelo.Usuario;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tutorvirtual.Execucao;

/**
 *
 * @author User
 */
public class TarefaTelegramQFalada extends TarefaTelegram {
    
    public TarefaTelegramQFalada(Execucao execucaoAtualBot, long chatId) {
        super(execucaoAtualBot,chatId);
    }

    @Override
    public String getNomeTarefa() {
        return PERGUNTA_FALADA; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(this.chatId);
        Questao q = QuestaoService.buscaQuestaoNaoRespondida(usuario.getChatId(), usuario.getNivel(), 1);
        if (q == null) {
            enviaMensagem("Você já realizou todas questões cadastradas até o momento.", usuario.getChatId(), null);
            return;
        }
        //Iniciado NetBeans...
        enviaVoz((System.getProperty("user.dir") + File.separator + "audiosQuestions" + File.separator + q.getEnunciado()), usuario.getChatId());
        //Iniciado por script...
        /*enviaVoz((System.getProperty("user.dir") + File.separator +"home"+ File.separator +"iagoilacerda"+ File.separator 
                + "TutorVirtual"+ File.separator + "audiosQuestions" + File.separator + q.getEnunciado()), usuario.getChatId());*/

        enviaMensagem("A alternativa que melhor responde ao que foi pedido no áudio é:" + q.alternativasToString() + " \n \n"
                + "<i>(Por favor, leia a alternativa por completo)</i>", usuario.getChatId(), null);
        usuario.setIdUltimaQuestao(q.getId());
        usuario.setCorrigirResposta(true);
        UsuarioService.atualizaUsuario(usuario);
    }

    private void enviaVoz(String referencia, long chatId) {
        enviaMensagem(EmojiParser.parseToUnicode("Vou exercitar minhas cordas vocais "
                + "para te mandar um áudio perfeito. :relieved:"), chatId, null);
        try {
            SendVoice mensagemVoz = new SendVoice(); //Criamos o obtejo que representa a mensagem de voz a ser enviada.
            mensagemVoz.setNewVoice(new File(referencia)); //Passamos a referência do arquivo a ser enviado.
            mensagemVoz.setChatId(chatId + ""); //Passamos o chatId para a mensagem de voz a ser enviada (necessidade de saber para onde enviar).
            this.execucaoAtualBot.sendVoice(mensagemVoz); //Envia a mensagem de voz.
        } catch (TelegramApiException ex) {
//            log.info(ex.getMessage());
        }
    }
}
