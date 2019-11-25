/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.telegram;

import com.vdurmont.emoji.EmojiParser;
import controler.QuestaoService;
import controler.RespostaService;
import controler.UsuarioService;
import ferramentas.Recognition;
import static hpa.telegram.TarefaTelegram.SCORE;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import modelo.Questao;
import modelo.Transcricao;
import modelo.Usuario;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Voice;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tutorvirtual.Execucao;

/**
 * Classe para representar a tarefa de correcao do Telegram
 *
 * @author Iago Izidorio Lacerda
 */
public class TarefaTelegramCorrecao extends TarefaTelegram {

    private Message mensagem;

    public TarefaTelegramCorrecao(Execucao execucaoAtualBot, long chatId, Message mensagem) {
        super(execucaoAtualBot, chatId);
        this.mensagem = mensagem;
    }

    @Override
    public String getNomeTarefa() {
        return SCORE; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(this.chatId);
        if (usuario.temQueCorrigirResposta()) {
            usuario.setCorrigirResposta(avaliarAlternativaEscolhida(mensagem, usuario));
        }
    }

    /**
     * Método que avalia a alternativa escolhida
     *
     * @author Iago Izidorio Lacerda
     * @param mensagem Message - Mensagem enviada pelo usuario.
     * @param usuario Usuario - Usuario que enviou a mensagem.
     * @return Boolean - Necessidade de corrigir a questao novamente.
     */
    private boolean avaliarAlternativaEscolhida(Message mensagem, Usuario usuario) {
        int indiceAlternativaEscolhida = 0;
        Questao q = QuestaoService.buscaQuestaoId(usuario.getIdUltimaQuestao());

        try {
            if (q.getTipo() == 0) {
                String textoMensagem = mensagem.getText();
                indiceAlternativaEscolhida = Integer.parseInt(textoMensagem) - 1;
                System.out.println("-----" + indiceAlternativaEscolhida);
                //log.log(Level.FINE, "----- {0}", indiceAlternativaEscolhida);
            } else {
                if (!mensagem.hasText()) {
                    //avaliar alternativa escolhida no áudio.
                    System.out.println("Chegou voz.");
                    //log.fine("Chegou voz.");
                    String caminhoArquivo = downloadAudio(mensagem); //Baixamos o arquivo do telegram.
                    Transcricao transcricao = Recognition.transcreverVoz(caminhoArquivo, q.getAlternativas());
                    //teste
                    if (transcricao != null) {
                        //avaliar a possibilidade de uma segunda chance de envio
                        //enviaMensagem("Você quis dizer: " + transcricao, mensagem.getChatId(), null);
                        enviaMensagem("Você acertou: " + (int) transcricao.getPorcentagemAcertos() + "%", mensagem.getChatId(), null);
                        indiceAlternativaEscolhida = transcricao.getIndiceAlternativaReferente();
                    } else {
                        enviaMensagem("Não consegui entender o que você disse...", mensagem.getChatId(), null);
                        return true;
                    }
                } else {
                    enviaMensagem("Oh! Eu te pedi um áudio! \n"
                            + "Fale para mim, por favor. \n \n"
                            + "<i>(Grave um áudio lendo completamente a alternativa escolhida)</i>", mensagem.getChatId(), null);
                    return true;
                }

            }

            System.out.println("-----" + indiceAlternativaEscolhida);
            int idAlternativaEscolhida = q.getAlternativas().get(indiceAlternativaEscolhida).getId();
            System.out.println("+++" + idAlternativaEscolhida);
//            log.log(Level.FINE, "-----{0}", indiceAlternativaEscolhida);
//            log.log(Level.FINE, "+++{0}", idAlternativaEscolhida);

            //Avalia se a pessoa acertou ou não.
            boolean retorno = (idAlternativaEscolhida == q.getIdAlternativaCorreta());
            enviaAvaliacao(retorno, usuario, q, indiceAlternativaEscolhida);
            //Cadastra a resposta escolhida no banco de dados.
            RespostaService.cadastraResposta(q.getId(), usuario.getChatId(), idAlternativaEscolhida);
            return false;

        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            //ex.printStackTrace();
//            log.info(ex.getMessage());

            if (q.getTipo() == 0) {
                enviaMensagem("Alternativa inválida. Envie outra alternativa, por favor.", mensagem.getChatId(), null);
            } else {
                enviaMensagem("Alternativa inválida. \n"
                        + "Não consegui encontrar o que você disse entre as alternativas. "
                        + "Pronuncie corretamente para que eu possa entender.", mensagem.getChatId(), null);
            }
            return true;
        }
    }

    /**
     * Método que envia uma mensagem de correcao ao usuario.
     *
     * @author Iago Izidorio Lacerda
     * @param acertou boolean - Diz se o usuario acertou a questao.
     * @param usuario Usuario - Usuario que enviou a mensagem.
     * @param q Questao - Questao que foi corrigida.
     * @param indiceAlternativaEscolhida int - Indice da alternativa que o
     * usuario escolheu.
     */
    private void enviaAvaliacao(boolean acertou, Usuario usuario, Questao q, int indiceAlternativaEscolhida) {
        if (acertou) {
            ReplyKeyboardRemove tecladoRemover = new ReplyKeyboardRemove(); //cria um objeto que remove o teclado (não inline) customizado.
            enviaMensagem(EmojiParser.parseToUnicode("Acertou! :stuck_out_tongue_closed_eyes:"), usuario.getChatId(), tecladoRemover);

            UsuarioService.alteraPontuacao(usuario, true);

            InlineKeyboardMarkup tecladoCustomizado = criaTecladoInlineComandos();
            enviaMensagem(EmojiParser.parseToUnicode("Estou gostando de conversar com você, " + usuario.getNome() + " :blush: . \n"
                    + "O que posso fazer por você agora?"), usuario.getChatId(), tecladoCustomizado);

        } else {
            String mensagemRetorno = q.getAlternativas().get(indiceAlternativaEscolhida).getDica();

            //Caso por algum motivo a questão esteja sem a dica.
            if (mensagemRetorno.isEmpty()) {
                mensagemRetorno = "Você errou essa questão. A alternativa correta era: "
                        + q.getAlternativaCorreta().getEnunciado();
            }

            ReplyKeyboardRemove tecladoRemover = new ReplyKeyboardRemove(); //cria um objeto que remove o teclado (não inline) customizado.
            mensagemRetorno = EmojiParser.parseToUnicode("Oh-oh! :sweat_smile: \n" + mensagemRetorno);
            enviaMensagem(mensagemRetorno, usuario.getChatId(), tecladoRemover);

            UsuarioService.alteraPontuacao(usuario, false);

            InlineKeyboardMarkup tecladoCustomizado = criaTecladoInlineComandos();
            enviaMensagem(EmojiParser.parseToUnicode("Um desafio não pode te fazer desistir, " + usuario.getNome() + " :blush: . \n"
                    + "Por isso, quero fazer mais por você. \n"
                    + "O que você quer que eu faça?"), usuario.getChatId(), tecladoCustomizado);
        }

    }

    /**
     * Método que faz o download do audio enviado pelo usuario.
     *
     * @author Iago Izidorio Lacerda
     * @param chegouMensagem Message - Objeto que contem a mensagem recebida do
     * usuario.
     * @return String - Caminho do arquivo que acabou de ser baixado.
     */
    private String downloadAudio(Message chegouMensagem) {
        Voice vozUsuario = chegouMensagem.getVoice();
        GetFile cascaArquivo = new GetFile(); //Criamos a casca do arquivo, ou seja, aquilo que nos dará o arquivo.
        cascaArquivo.setFileId(vozUsuario.getFileId()); //Passamos a referência, ou seja, o identificador do arquivo do Telegram.
        try {
            //org.telegram.telegrambots.api.objects.File arquivoVoz = getFile(cascaArquivo);//Pegamos o arquivo do Telegram (forma antiga).
            org.telegram.telegrambots.api.objects.File arquivoVoz = this.execucaoAtualBot.execute(cascaArquivo);   //Pegamos o arquivo do Telegram (forma nova).

            System.out.println("Baixando " + arquivoVoz.getFilePath());
            //log.fine("Baixando "+arquivoVoz.getFilePath());

            File arquivoBaixado = this.execucaoAtualBot.downloadFile(arquivoVoz); //Baixamos o arquivo.
            System.out.println("Baixado");
            //log.fine("Baixado");

            String extensao = "." + (vozUsuario.getMimeType().split("/")[1].trim());
            return salvarVoz(Paths.get(arquivoBaixado.getPath()), extensao, chegouMensagem);
        } catch (TelegramApiException ex) {
//            log.info(ex.getMessage());
        }
        return null;
    }

    /**
     * Método que salva o arquivo na maquina.
     *
     * @author Iago Izidorio Lacerda
     * @param fonteDados Path - path de onde serao lido os dados.
     * @param extensao String - extensao do arquivo.
     * @param chegouMensagem Message - objeto mensagem recabida do usuario.
     * @return String - Diz onde o arquivo foi salvo (caminho do arquivo).
     */
    private String salvarVoz(Path fonteDados, String extensao, Message chegouMensagem) {
        try {
            System.out.println("Gravando");
            //log.fine("Gravando.");

            String caminhoArquivo = acharPastaUser(chegouMensagem);//Definimos o caminho da pasta do usuario.
            caminhoArquivo += extensao;//Add a extensão no fim do arquivo.
            File arquivoNovo = new File(caminhoArquivo);
            arquivoNovo.createNewFile();//Criamos um novo arquivo (com o caminho que definimos).
            FileOutputStream escritor = new FileOutputStream(arquivoNovo);
            escritor.write(Files.readAllBytes(fonteDados)); //Escrevemos os bytes do arquivo baixado no novo arquivo que criamos.
            escritor.flush();
            escritor.close();

            System.out.println("Gravado");
            //log.fine("Gravado.");

            return caminhoArquivo;
        } catch (IOException ex) {
//            log.info(ex.getMessage());
        }
        return null;
    }

    /**
     * Método que acha a pasta de audios do usuario na maquina.
     *
     * @author Iago Izidorio Lacerda
     * @param chegouMensagem Message - objeto mensagem recabida do usuario.
     * @return String - caminho da pasta do usuario.
     */
    private String acharPastaUser(Message chegouMensagem) {
        //Iniciado no NetBeans
        String caminhoPastaUsuario = System.getProperty("user.dir") + File.separator + "audiosUsers" + File.separator
                + chegouMensagem.getFrom().getId() + File.separator;
        //Iniciado por script
        /*String caminhoPastaUsuario = System.getProperty("user.dir") + File.separator +"home" + File.separator +"iagoilacerda"+ File.separator 
                + "TutorVirtual"+ File.separator+"audiosUsers" + File.separator
                + chegouMensagem.getFrom().getId() + File.separator;*/

        File pastaUsuario = new File(caminhoPastaUsuario);
        if (!pastaUsuario.exists()) { //Se o usuário ainda não tiver uma pasta, 
            pastaUsuario.mkdir();//Criamos uma para ele.           
        }
        String caminhoNovoArquivo = caminhoPastaUsuario + "audio" + (pastaUsuario.listFiles().length + 1);

        return caminhoNovoArquivo;
    }

}
