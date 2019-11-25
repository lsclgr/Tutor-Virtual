package dao;

import ferramentas.FabricaConexao;
import ferramentas.LoggerCustomizado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Questao;

public class QuestaoDAO {
    
//private static Logger log = LoggerCustomizado.criaLog(QuestaoDAO.class.getName());
    
    public static List<Questao> buscaQuestoesNaoRespondidas(long idUsuario, int tipoQuestao) {
        List<Questao> retorno = new ArrayList<>();

        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "select * from questao where questao.tipo = ? EXCEPT "
                    + "(select questao.* from resposta, questao where questao.id = resposta.id_questao and resposta.id_usuario = ?)";
            PreparedStatement x = conexao.prepareStatement(sql);
            x.setInt(1, tipoQuestao);
            x.setLong(2, idUsuario);
            ResultSet info = x.executeQuery();

            while (info.next()) {
                Questao q = new Questao(info.getInt("id"), info.getString("enunciado"), info.getInt("nivel"), info.getInt("id_alternativa_correta"), info.getInt("tipo"));
                retorno.add(q);
            }

        } catch (Exception ex) {
            System.out.println("Erro na execução da sql - buscaQuestoesNaoRespondidas.");
            //log.log(Level.FINE, "Erro na execução da sql - buscaQuestoesNaoRespondidas.");
        }finally{
            FabricaConexao.encerraConexao();
        }
        return retorno;
    }

    public static Questao buscaQuestao(int idQuestao) {
        Questao q = null;
        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "select * from questao where questao.id = ?";
            PreparedStatement x = conexao.prepareStatement(sql);

            x.setInt(1, idQuestao);

            ResultSet info = x.executeQuery();

            while (info.next()) {
                q = new Questao(info.getInt("id"), info.getString("enunciado"), info.getInt("nivel"), info.getInt("id_alternativa_correta"), info.getInt("tipo"));
            }

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - buscaQuestao.");
            //log.log(Level.FINE, "Erro na execução da sql - buscaQuestao.");
        }finally{
            FabricaConexao.encerraConexao();
        }
        return q;
    }

    public static boolean cadastraQuestao(Questao q) {
        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "insert into questao (id_alternativa_correta, enunciado, nivel) "
                    + "values(?,?,?,?)";

            PreparedStatement x = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            x.setInt(1, q.getIdAlternativaCorreta());
            x.setString(2, q.getEnunciado());
            x.setInt(3, q.getNivel());

            x.execute();

            ResultSet info = x.getGeneratedKeys();

            boolean retorno = false;
            while (info.next()) {
                int idQuestao = info.getInt("id");
                q.setId(idQuestao);
                retorno = true;
            }
            return retorno;

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - cadastraQuestao.");
            //log.log(Level.FINE, "Erro na execução da sql - cadastraQuestao.");
            return false;
        }finally{
            FabricaConexao.encerraConexao();
        }
    }
}
