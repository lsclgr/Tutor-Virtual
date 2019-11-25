/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.telegram;

import controler.UsuarioService;
import modelo.Usuario;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import tutorvirtual.Execucao;

/**
 *
 * @author User
 */
public class TarefaTelegramScore extends TarefaTelegram {

    public TarefaTelegramScore(Execucao execucaoAtualBot, long chatId) {
        super(execucaoAtualBot, chatId);
    }

    @Override
    public String getNomeTarefa() {
        return SCORE; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(this.chatId);
        enviaMensagem("<pre>| Pontuação | Nível |\n"
                + "|-----------|-------|\n"
                + "|    " + (usuario.getPontuacao() < 10 ? " " + usuario.getPontuacao()
                + " " : usuario.getPontuacao() < 100 ? usuario.getPontuacao() + " " : usuario.getPontuacao())
                + "    |   " + usuario.getNivel() + "   |</pre>",
                usuario.getChatId(), null);

        InlineKeyboardMarkup tecladoCustomizado = criaTecladoInlineComandos();
        enviaMensagem("E agora? \n"
                + "O que eu faço, " + usuario.getNome() + "?", usuario.getChatId(), tecladoCustomizado);
    }

}
