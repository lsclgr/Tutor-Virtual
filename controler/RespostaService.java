package controler;

import dao.RespostaDAO;
import modelo.Resposta;

public class RespostaService {
    
    public static void cadastraResposta(int idQuestao, long idUsuario, int alternativaEscolhida){
        Resposta r = new Resposta(idQuestao, idUsuario, alternativaEscolhida);
        RespostaDAO.cadastraResposta(r);
    }
    
}
