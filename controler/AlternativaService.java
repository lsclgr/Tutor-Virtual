package controler;

import dao.AlternativaDAO;
import java.util.List;
import modelo.Alternativa;

public class AlternativaService {
    
    public static Alternativa buscaAlternativaId(int idAlternativa){
        return AlternativaDAO.buscaAlternativaId(idAlternativa);
    }
    
    public static List<Alternativa> buscaAlternativasQuestao(int idQuestao){
        return AlternativaDAO.buscaAlternativasQuestao(idQuestao);
    }
    
}
