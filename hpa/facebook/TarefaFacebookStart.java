package hpa.facebook;

import controler.UsuarioService;
import modelo.Usuario;
import java.net.Socket;
/**
 *
 * @author Luísa
 */
public class TarefaFacebookStart extends TarefaFacebook {

    public TarefaFacebookStart(String psid_, String mens_, String nome_, Socket resposta) {
        super(psid_, mens_, nome_, resposta);
    }

    @Override
    public void execute() {
        System.out.println("Executando a tarefa Start");
        Usuario user = new Usuario(Long.parseLong(super.psid), "my friend", 0, 1, 0, false);
        respostaTutor(super.canalResposta, "Welcome " +  user.getNome() + ", how can I help you?");
        UsuarioService.cadastraUsuario(user);
        

        //colocar os botoes
    }

    @Override
    public String getNomeTarefa() {
        return "/start";
    }

}
