package hpa.facebook;

import controler.UsuarioService;
import java.net.Socket;
import modelo.Usuario;
/**
 *
 * @author Luísa
 */
public class TarefaFacebookScore extends TarefaFacebook{

    public TarefaFacebookScore(String psid_, String mens_, String nome_, Socket resposta) {
        super(psid_, mens_, nome_, resposta);
    }

    @Override
    public void execute() {
        Usuario usuario = UsuarioService.recuperaUsuario(Long.parseLong(super.psid));
        UsuarioService.atualizaUsuario(usuario);
//        respostaTutor(super.canalResposta, "| Pontuação | Nível |\n"
//               // + "|------------------| -------- |\n"
//                + "|         " + (usuario.getPontuacao() < 10 ? " " + usuario.getPontuacao()
//                + " " : usuario.getPontuacao() < 100 ? usuario.getPontuacao() + " " : usuario.getPontuacao())
//                + "         |     " + usuario.getNivel() + "    |\n \n "
//                +"E agora? \n"
//                + "O que eu faço, " + usuario.getNome() + "?");

                respostaTutor(super.canalResposta, "Score : "+ (usuario.getPontuacao() < 10 ? " " + usuario.getPontuacao()
                + " " : usuario.getPontuacao() < 100 ? usuario.getPontuacao() + " " : usuario.getPontuacao())
                + "\nLevel : " + usuario.getNivel() + "\n \n"
                +"Is everything okay? \n"
                + "What may I do for you now, " + usuario.getNome() + "?");

    }

    @Override
    public String getNomeTarefa() {
         return "/score";
    }
    
}
