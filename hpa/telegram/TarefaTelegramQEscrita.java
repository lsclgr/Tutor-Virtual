/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.telegram;

import controler.QuestaoService;
import controler.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import modelo.Questao;
import modelo.Usuario;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import tutorvirtual.Execucao;

/**
 *
 * @author User
 */
public class TarefaTelegramQEscrita extends TarefaTelegram {

    public TarefaTelegramQEscrita(Execucao execucaoAtualBot, long chatId) {
        super(execucaoAtualBot, chatId);
    }

    @Override
    public String getNomeTarefa() {
        return PERGUNTA_ESCRITA; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(this.chatId);
        Questao q = QuestaoService.buscaQuestaoNaoRespondida(usuario.getChatId(), usuario.getNivel(), 0);
        if (q == null) {
            enviaMensagem("Você já realizou todas questões cadastradas até o momento.", usuario.getChatId(), null);
            return;
        }

        ReplyKeyboardMarkup tecladoCustomizado = criaTecladoAlternativas();
        enviaMensagem(q.toString(), usuario.getChatId(), tecladoCustomizado);
        usuario.setIdUltimaQuestao(q.getId());
        usuario.setCorrigirResposta(true);
        UsuarioService.atualizaUsuario(usuario);
    }

    private ReplyKeyboardMarkup criaTecladoAlternativas() {
        ReplyKeyboardMarkup tecladoCustomizado = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow linhaKeyboard = new KeyboardRow();
        KeyboardButton alternativa1 = new KeyboardButton("1");
        KeyboardButton alternativa2 = new KeyboardButton("2");
        KeyboardButton alternativa3 = new KeyboardButton("3");
        linhaKeyboard.add(alternativa1);
        linhaKeyboard.add(alternativa2);
        linhaKeyboard.add(alternativa3);
        keyboard.add(linhaKeyboard);
        tecladoCustomizado.setKeyboard(keyboard);
        tecladoCustomizado.setResizeKeyboard(true);
        tecladoCustomizado.setOneTimeKeyboard(true);
        return tecladoCustomizado;
    }

}
