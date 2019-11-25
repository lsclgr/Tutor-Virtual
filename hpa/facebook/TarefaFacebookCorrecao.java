/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpa.facebook;

import com.vdurmont.emoji.EmojiParser;
import controler.QuestaoService;
import controler.RespostaService;
import controler.UsuarioService;
import java.net.Socket;
import modelo.Questao;
import modelo.Usuario;

/**
 *
 * @author Luísa
 */
public class TarefaFacebookCorrecao extends TarefaFacebook{

    
    public TarefaFacebookCorrecao(String psid_, String mens_, String nome_, Socket resposta) {
        super(psid_, mens_, nome_, resposta);
    }

    
    
    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        if (usuario.temQueCorrigirResposta()) {
            usuario.setCorrigirResposta(avaliarAlternativaEscolhida(super.mens, usuario));
        }
    }
    
    private boolean avaliarAlternativaEscolhida(String mensagem, Usuario usuario) {
        usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        int indiceAlternativaEscolhida = 0;
        Questao q = QuestaoService.buscaQuestaoId(usuario.getIdUltimaQuestao());

        try {
            if (q.getTipo() == 0) {
                String textoMensagem = mensagem;
                indiceAlternativaEscolhida = Integer.parseInt(textoMensagem) - 1;
                System.out.println("-----" + indiceAlternativaEscolhida);
                //log.log(Level.FINE, "----- {0}", indiceAlternativaEscolhida);
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
            RespostaService.cadastraResposta(q.getId(), Long.parseLong(super.psid), idAlternativaEscolhida);
            return false;

        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            //ex.printStackTrace();
//            log.info(ex.getMessage());

            if (q.getTipo() == 0) {
                super.respostaTutor(canalResposta, "This is not an avaliable answer at the moment. Please, try it again!");
            }
            return true;
        }
    }

    
    private void enviaAvaliacao(boolean acertou, Usuario usuario, Questao q, int indiceAlternativaEscolhida) {
        if (acertou) {
            
            UsuarioService.alteraPontuacao(usuario, true);
            super.respostaTutor(canalResposta, EmojiParser.parseToUnicode("You got it right! :stuck_out_tongue_closed_eyes: \n"+"I'm enjoying talking to you, " + usuario.getNome() + " :blush: . \n"
                                                                           + "What may I do for you now?"));

        } else {
            String mensagemRetorno = q.getAlternativas().get(indiceAlternativaEscolhida).getDica();

            //Caso por algum motivo a questão esteja sem a dica.
            if (mensagemRetorno.isEmpty()) {
                mensagemRetorno = "You missed this question, the correct answer was: "
                        + q.getAlternativaCorreta().getEnunciado();
            }

            
            mensagemRetorno = EmojiParser.parseToUnicode("Oh-oh! :sweat_smile: \n" + mensagemRetorno);
            UsuarioService.alteraPontuacao(usuario, false);
            super.respostaTutor(canalResposta, mensagemRetorno + EmojiParser.parseToUnicode("\nA challenge can't make you give up, " + usuario.getNome() + " :blush: . \n"
                    + "So I want to do more for you. \n"
                    + "What do you want me to do?"));
        }

    }


    @Override
    public String getNomeTarefa() {
        return "Correcao Facebook";
    }
    
}
