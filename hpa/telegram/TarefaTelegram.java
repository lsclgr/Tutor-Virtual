/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.telegram;

import ferramentas.Plataformas;
import hpa.Tarefa;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tutorvirtual.Execucao;

/**
 * Classe para representar as tarefas do Telegram
 *
 * @author Iago Izidorio Lacerda
 */
public class TarefaTelegram implements Tarefa {
    protected static final String PERGUNTA_ESCRITA = "/pergunta_escrita";
    protected static final String PERGUNTA_FALADA = "/pergunta_falada";
    protected static final String SCORE = "/score";
    protected static final String START = "/start";
    protected Execucao execucaoAtualBot;
    protected long chatId;

    public TarefaTelegram(Execucao execucaoAtualBot, long chatId) {
        this.execucaoAtualBot = execucaoAtualBot;
        this.chatId=chatId;
    }
    
    @Override
    public void execute() {
        System.out.println("Ainda não foi implementado"); 
    }
    
    /**
     * Método que envia a mensagem para o usuario.
     *
     * @author Iago Izidorio Lacerda
     * @param texto String - o que enviar para o usuario.
     * @param chatId long - id do usuario para quem vamos enviar a mensagem.
     * @param tecladoCustomizado ReplyKeyboard - teclado customizado.
     */
    protected void enviaMensagem(String texto, long chatId, ReplyKeyboard tecladoCustomizado) {
        try {
            SendMessage mensagem = new SendMessage(); //Criamos o obtejo que representa a mensagem de texto a ser enviada.
            mensagem.enableHtml(true); //Permite que usemos tags Html (suportadas pelo Telegram) para formatar o texto.
            mensagem.setText(texto); //Passamos o texto a ser enviado para a mensagem de texto.
            mensagem.setChatId(chatId + ""); //Passamos o chatId para a mensagem a ser enviada (necessidade de saber para onde enviar).
            mensagem.setReplyMarkup(tecladoCustomizado); //Seta um teclado customizado que deve ser enviado para o cliente junto com a mensagem.
            this.execucaoAtualBot.execute(mensagem);//Envia a mensagem usando um método novo.
        } catch (TelegramApiException ex) {
            
        }
    }

                        /**Método que cria um teclado inline (flutuante) customizado com os comandos que o usuário pode escolher.
 *
 * @author Iago Izidorio Lacerda
 * @return InlineKeyboardMarkup - teclado inline criado.
 */
        //
    protected InlineKeyboardMarkup criaTecladoInlineComandos() {
        InlineKeyboardMarkup tecladoCustomizado = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList();

        InlineKeyboardButton botaoPergunta = new InlineKeyboardButton();
        botaoPergunta.setCallbackData(PERGUNTA_ESCRITA);
        botaoPergunta.setText("Quero treinar minha gramática.");
        List<InlineKeyboardButton> linha1 = new ArrayList();
        linha1.add(botaoPergunta);
        keyboard.add(linha1);

        InlineKeyboardButton botaoPerguntaFalada = new InlineKeyboardButton();
        botaoPerguntaFalada.setCallbackData(PERGUNTA_FALADA);
        botaoPerguntaFalada.setText("Quero treinar minha pronúncia.");
        List<InlineKeyboardButton> linha2 = new ArrayList();
        linha2.add(botaoPerguntaFalada);
        keyboard.add(linha2);

        InlineKeyboardButton botaoScore = new InlineKeyboardButton();
        botaoScore.setCallbackData(SCORE);
        botaoScore.setText("Quero ver o meu score.");
        List<InlineKeyboardButton> linha3 = new ArrayList();
        linha3.add(botaoScore);
        keyboard.add(linha3);

        tecladoCustomizado.setKeyboard(keyboard);
        return tecladoCustomizado;
    }
    
    @Override
    public String getNomeTarefa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPlataforma() {
        return Plataformas.TELEGRAM.toString();
    }

    public static String getPERGUNTA_ESCRITA() {
        return PERGUNTA_ESCRITA;
    }

    public static String getPERGUNTA_FALADA() {
        return PERGUNTA_FALADA;
    }

    public static String getSCORE() {
        return SCORE;
    }

    public static String getSTART() {
        return START;
    }

}
