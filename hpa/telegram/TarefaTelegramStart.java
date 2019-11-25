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
public class TarefaTelegramStart extends TarefaTelegram {

    private String nomeUser;

    public TarefaTelegramStart(Execucao execucaoAtualBot, String nome, long chatId) {
        super(execucaoAtualBot,chatId);
        this.nomeUser = nome;
    }

    @Override
    public String getNomeTarefa() {
        return START; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        Usuario usuario = new Usuario(this.chatId, this.nomeUser);
        UsuarioService.cadastraUsuario(usuario);
        InlineKeyboardMarkup tecladoCustomizado = criaTecladoInlineComandos();
        enviaMensagem("Oi, " + (usuario.getNome()) + "! Em que posso te ajudar? \n"
                + "<i>Hi, " + (usuario.getNome()) + "! May I help you?</i>", usuario.getChatId(), tecladoCustomizado);

    }

    
    
}
