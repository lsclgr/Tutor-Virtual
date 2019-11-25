package controler;

import dao.QuestaoDAO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import modelo.Questao;

public class QuestaoService {
    
    public static Questao buscaQuestaoNaoRespondida(long idUsuario, int nivelUsuario,int tipoQuestao){
        Questao q = null;
        
        List<Questao> questoes = QuestaoDAO.buscaQuestoesNaoRespondidas(idUsuario,tipoQuestao);
        
        if (!questoes.isEmpty()){
            for (int i = 0; i < questoes.size() ;i++){
                if (questoes.get(i).getNivel() == nivelUsuario){
                    q = questoes.get(i);
                    break;
                }
            }
            
            //se uma questão do nível do usuário conseguir ser encontrada
            if (q != null){
                q.setAlternativas(AlternativaService.buscaAlternativasQuestao(q.getId()));
            }
        }
        return q;
    }
    
    public static Questao buscaQuestaoId(int idQuestao){
        Questao q = QuestaoDAO.buscaQuestao(idQuestao);
        q.setAlternativas(AlternativaService.buscaAlternativasQuestao(idQuestao));
        System.out.println(q.getAlternativas().size());
        return q;
    }
    
}
