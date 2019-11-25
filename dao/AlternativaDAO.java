package dao;

import ferramentas.FabricaConexao;
import ferramentas.LoggerCustomizado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Alternativa;

public class AlternativaDAO {

//private static Logger log = LoggerCustomizado.criaLog(AlternativaDAO.class.getName());
    /**
     * Método que recupera uma alternativa pelo seu id.
     *
     * @author Iago Izidorio Lacerda
     * @param idAlternativa  int - id da alternativa que você vai recuperar.
     * @return Alternativa - alternativa recuperada.
     */
    public static Alternativa buscaAlternativaId(int idAlternativa) {
        Alternativa a = null;
        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "select * from alternativa where alternativa.id = ?";
            PreparedStatement x = conexao.prepareStatement(sql);
            x.setInt(1, idAlternativa);
            ResultSet info = x.executeQuery();

            info.next();
            a = new Alternativa(info.getString("enunciado"), info.getString("dica"), info.getInt("id"), info.getInt("id_questao"));

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - buscaAlternativaId.");
            //log.log(Level.FINE, null, ex);
        }finally{
            FabricaConexao.encerraConexao();
        }
        return a;
    }

    public static List<Alternativa> buscaAlternativasQuestao(int idQuestao) {
        List<Alternativa> listaAlternativas = new ArrayList<>();
        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "select * from alternativa where alternativa.id_questao = ?";
            PreparedStatement x = conexao.prepareStatement(sql);

            x.setInt(1, idQuestao);
            ResultSet info = x.executeQuery();

            while (info.next()) {
                Alternativa a = new Alternativa(info.getString("enunciado"), info.getString("dica"), info.getInt("id"), info.getInt("id_questao"));
                listaAlternativas.add(a);
                System.out.println(a.toString());
                //log.log(Level.FINE, a.toString());
            }

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - buscaAlternativasQuestao.");
            //ex.printStackTrace();
            //log.log(Level.FINE, null, ex);
            //log.log(Level.FINE, "Erro na execução da sql - buscaAlternativasQuestao.");
        }finally{
            FabricaConexao.encerraConexao();
        }
        
        return listaAlternativas;
    }

    public static boolean cadastraAlternativas(List<Alternativa> alternativas) {
        try (Connection conexao = FabricaConexao.criaConexao()) {

        } catch (SQLException ex) {
             System.out.println("Erro na execução da sql - cadastraAlternativas.");
            //log.log(Level.FINE, "Erro na execução da sql - cadastraAlternativas.");
        }finally{
            FabricaConexao.encerraConexao();
        }
        return false;
    }
}
