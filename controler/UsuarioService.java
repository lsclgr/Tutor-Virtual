package controler;

import dao.UsuarioDAO;
import modelo.Usuario;

public class UsuarioService {

    private static final int NIVEL1 = 49;
    //até 149 acertos
    private static final int NIVEL2 = 149;

    public static boolean cadastraUsuario(Usuario usuario) {

        if (UsuarioDAO.recuperaUsuarioId(usuario.getChatId()) == null) {
            return UsuarioDAO.cadastraUsuario(usuario);
        }
        return false;

    }

    public static void atualizaUsuario(Usuario usuario) {
        if (usuario.getPontuacao() > NIVEL2) {
            usuario.setNivel(3);
        } else {
            if (usuario.getPontuacao() > NIVEL1) {
                usuario.setNivel(2);
            } else {
                usuario.setNivel(1);
            }
        }
        UsuarioDAO.atualizaUsuario(usuario);
    }

    public static Usuario recuperaUsuario(long chatId) {
        return UsuarioDAO.recuperaUsuarioId(chatId);
    }

    public static void alteraPontuacao(Usuario usuario, boolean acertou) {
        switch (usuario.getNivel()) {
            case 1: {
                if (acertou) {
                    usuario.setPontuacao(usuario.getPontuacao() + 3);
                } else {
                    if (usuario.getPontuacao() != 0) {
                        usuario.setPontuacao(usuario.getPontuacao() - 1);
                    }
                }
            }break;
            case 2:{
                if (acertou) {
                    usuario.setPontuacao(usuario.getPontuacao() + 2);
                } else {
                    if (usuario.getPontuacao() != 0) {
                        if(usuario.getPontuacao() >= 2){
                           usuario.setPontuacao(usuario.getPontuacao() - 2); 
                        }else{
                            usuario.setPontuacao(0);
                        }  
                    }
                }
            }break;
            case 3:{
                if (acertou) {
                    usuario.setPontuacao(usuario.getPontuacao() + 2);
                } else {
                    if (usuario.getPontuacao() != 0) {
                        if(usuario.getPontuacao() >= 3){
                           usuario.setPontuacao(usuario.getPontuacao() - 3); 
                        }else{
                            usuario.setPontuacao(0);
                        }  
                    }
                }
            }
        }
        atualizaUsuario(usuario);        
    }

}
