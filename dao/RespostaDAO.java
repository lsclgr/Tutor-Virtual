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
import modelo.Resposta;

public class RespostaDAO {
//private static Logger log = LoggerCustomizado.criaLog(RespostaDAO.class.getName());

    public static boolean cadastraResposta(Resposta novaResposta) {
        try (Connection conexao = FabricaConexao.criaConexao()) {

            String sql = "insert into resposta(id_questao, id_usuario, id_alternativa_escolhida) values(?,?,?)";
            PreparedStatement x = conexao.prepareStatement(sql);

            x.setInt(1, novaResposta.getIdQuestao());
            x.setLong(2, novaResposta.getChatIdUsuario());
            x.setInt(3, novaResposta.getIdAlternativaEscolhida());

            return x.execute();

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - cadastraResposta.");
            //log.log(Level.FINE, "Erro na execução da sql - cadastraResposta.");
            return false;
        }finally{
            FabricaConexao.encerraConexao();
        }
    }

    public static List<Resposta> recuperaRespostasChatId(long chatid) {
        List<Resposta> respostas = new ArrayList<>();
        try (Connection conexao = FabricaConexao.criaConexao()) {
            String sql = "select * from resposta where id_usuario = ?";
            PreparedStatement x = conexao.prepareStatement(sql);

            x.setLong(1, chatid);
            ResultSet info = x.executeQuery();

            while (info.next()) {
                Resposta r = new Resposta(info.getInt("id_questao"), info.getInt("id_usuario"), info.getInt("id_alternativa_escolhida"));
                respostas.add(r);
            }

        } catch (SQLException ex) {
            System.out.println("Erro na execução da sql - recuperaRespostasChatId.");
            //log.log(Level.FINE, "Erro na execução da sql - recuperaRespostasChatId.");

        }finally{
            FabricaConexao.encerraConexao();
        }
        return respostas;
    }
}
