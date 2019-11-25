package tutorvirtual;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import hpa.Tarefa;
import hpa.telegram.TarefaTelegramCorrecao;
import hpa.telegram.TarefaTelegramQEscrita;
import hpa.telegram.TarefaTelegramQFalada;
import hpa.telegram.TarefaTelegramScore;
import hpa.telegram.TarefaTelegramStart;
import hpa.telegram.Trabalhador;
import java.util.Vector;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Classe para representar a execucao de um TelegramLongPollingBot
 *
 * @author Iago Izidorio Lacerda
 */
public class Execucao extends TelegramLongPollingBot {

    private static final String PERGUNTA_ESCRITA = "/pergunta_escrita";
    private static final String PERGUNTA_FALADA = "/pergunta_falada";
    private static final String SCORE = "/score";
    private static final String START = "/start";
    public static Vector<Tarefa> sacolaTarefas = new Vector<>();
    public static Vector<Trabalhador> trabalhadores = new Vector<>();
//    private static Logger log = LoggerCustomizado.criaLog(Execucao.class.getName());

    public static void criaWorkers() {
        int numCPU = (int) (Runtime.getRuntime().availableProcessors() * 1.5);
        //int numCPU = 1;
        System.out.println("Num CPU = " + numCPU);
        for (int i = 0; i < numCPU; i++) {
            trabalhadores.add(new Trabalhador(sacolaTarefas));
        }
    }

    public static void iniciaWorkers() {
        System.out.println("Workers iniciados.");
        for (int i = 0; i < trabalhadores.size(); i++) {
            trabalhadores.get(i).start();
        }
    }

    public static void acorda() {
        System.out.println("Acordando os workers para trabalhar.");
        for (int i = 0; i < trabalhadores.size(); i++) {
            trabalhadores.get(i).acorda();
        }
    }

    public Execucao() {
        criaWorkers();
        iniciaWorkers();
    }

    @Override
    public String getBotToken() {
        return "906530140:AAHbt4AB_-xTO8NXvwm8Eb5buUFYhALTUUc";
    }

    @Override
    public String getBotUsername() {
        return "BotTesteTutor";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message mensagem = update.getMessage();
            if (mensagem.hasText()) {
                String textoMensagem = mensagem.getText().toLowerCase();
                if (mensagem.isCommand()) {
                    executar(textoMensagem, mensagem);
                } else {
                    sacolaTarefas.add(new TarefaTelegramCorrecao(this, mensagem.getChatId(), mensagem));
                    acorda();
                }
            } else {
                sacolaTarefas.add(new TarefaTelegramCorrecao(this, mensagem.getChatId(), mensagem));
                acorda();
            }
        } else {
            if (update.hasCallbackQuery()) {
                CallbackQuery respostaBotaoInline = update.getCallbackQuery();
                String comandoEscolhido = respostaBotaoInline.getData();
                removerInline(respostaBotaoInline.getInlineMessageId(), respostaBotaoInline.getMessage().getChatId(), respostaBotaoInline.getMessage().getMessageId(), comandoEscolhido);
                executar(comandoEscolhido, respostaBotaoInline.getMessage());
            }
        }
        System.out.println("__________________________________________________________");
        //log.fine("______________________________________________");      
    }

    //Edita o teclado inline, nesse caso o removendo já que passamos null como o novo teclado inline.
    public void removerInline(String idMessageInline, long chatId, Integer messageId, String comandoEscolhido) {

        EditMessageReplyMarkup edicao = new EditMessageReplyMarkup();
        edicao.setInlineMessageId(idMessageInline);
        edicao.setMessageId(messageId);
        edicao.setChatId(chatId);
        edicao.setReplyMarkup(new InlineKeyboardMarkup());
        try {
            execute(edicao);
        } catch (TelegramApiException ex) {
            // log.info(ex.getMessage());
        }

        switch (comandoEscolhido) {
            case PERGUNTA_ESCRITA: {
                enviaMensagem(EmojiParser.parseToUnicode("Okay! :relaxed: \n Vou te mandar uma pergunta como pediu."), chatId, null);
            }
            break;
            case SCORE: {
                enviaMensagem(EmojiParser.parseToUnicode("Pode deixar! :wink: \n Vou te enviar o seu score."), chatId, null);
            }
        }

    }

    public void executar(String novoSMS, Message chegouMensagem) {
        switch (novoSMS) {
            case START: {
                sacolaTarefas.add(new TarefaTelegramStart(this, chegouMensagem.getFrom().getFirstName(), chegouMensagem.getChatId()));
            }
            break;

            case PERGUNTA_ESCRITA: {
                sacolaTarefas.add(new TarefaTelegramQEscrita(this, chegouMensagem.getChatId()));
            }
            break;

            case PERGUNTA_FALADA: {
                sacolaTarefas.add(new TarefaTelegramQFalada(this, chegouMensagem.getChatId()));
            }
            break;

            case SCORE: {
                sacolaTarefas.add(new TarefaTelegramScore(this, chegouMensagem.getChatId()));
            }
            break;
            /*case "/audio": {
                Usuario usuario = UsuarioService.recuperaUsuario(chegouMensagem.getChatId());
                enviaAudio((System.getProperty("user.dir") + "\\audiosTestes\\Audio.mp3"), usuario.getChatId());
            }*/
        }
        acorda();
    }

    /**
     * Método que envia a mensagem para o usuario.
     *
     * @author Iago Izidorio Lacerda
     * @param texto String - o que enviar para o usuario.
     * @param chatId long - id do usuario para quem vamos enviar a mensagem.
     * @param tecladoCustomizado ReplyKeyboard - teclado customizado.
     */
    public void enviaMensagem(String texto, long chatId, ReplyKeyboard tecladoCustomizado) {
        try {
            SendMessage mensagem = new SendMessage(); //Criamos o obtejo que representa a mensagem de texto a ser enviada.
            mensagem.enableHtml(true); //Permite que usemos tags Html (suportadas pelo Telegram) para formatar o texto.
            mensagem.setText(texto); //Passamos o texto a ser enviado para a mensagem de texto.
            mensagem.setChatId(chatId + ""); //Passamos o chatId para a mensagem a ser enviada (necessidade de saber para onde enviar).
            mensagem.setReplyMarkup(tecladoCustomizado); //Seta um teclado customizado que deve ser enviado para o cliente junto com a mensagem.
            execute(mensagem);//Envia a mensagem usando um método novo.
        } catch (TelegramApiException ex) {
//            log.info(ex.getMessage());
        }
    }

    /* public void enviaAudio(String referenciaAudio, long chatId) {
        try {
            SendAudio audio = new SendAudio();
            audio.setNewAudio(new File(referenciaAudio));
            audio.setChatId(chatId + "");

            sendAudio(audio);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Execucao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
