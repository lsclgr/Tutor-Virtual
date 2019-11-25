package dao;

import ferramentas.FabricaConexao;
import ferramentas.LoggerCustomizado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Usuario;

public class UsuarioDAO {
//    private static Logger log = LoggerCustomizado.criaLog(UsuarioDAO.class.getName());
    
    public static boolean cadastraUsuario(Usuario usuario){
        try(Connection conexao = FabricaConexao.criaConexao()){
            
            String sql = "insert into usuario(chat_id, nome, nivel, pontuacao,corrigir) values(?,?,?,?,?)";
            PreparedStatement x = conexao.prepareStatement(sql);
            
            x.setLong(1, usuario.getChatId());
            x.setString(2, usuario.getNome());
            x.setInt(3, usuario.getNivel());
            x.setInt(4, usuario.getPontuacao());
            x.setBoolean(5, usuario.temQueCorrigirResposta());
            System.out.println("insert user..");
            return !x.execute();
            
        }catch(SQLException ex){
            ex.printStackTrace();
           System.out.println("Erro na execução da sql - cadastraUsuario.");
//            log.log(Level.FINE, "Erro na execução da sql - cadastraUsuario.");
            return false;
        }finally{
            FabricaConexao.encerraConexao();
        }
    }
    
    public static Usuario recuperaUsuarioId(long chatid){
        Usuario usuario = null;
        try(Connection conexao = FabricaConexao.criaConexao()){
            String sql = "select * from usuario where chat_id = ?";
            PreparedStatement x = conexao.prepareStatement(sql);
            
            x.setLong(1, chatid);
            ResultSet info = x.executeQuery();
            
            while (info.next()){
                usuario = new Usuario(chatid, info.getString("nome"), info.getInt("pontuacao"), info.getInt("nivel"), info.getInt("id_ultima_questao"),info.getBoolean("corrigir"));
            }
            
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("Erro na execução da sql - recuperaUsuarioId.");
//            log.log(Level.FINE, "Erro na execução da sql - recuperaUsuarioId.");            
        }finally{
            FabricaConexao.encerraConexao();
        }    
        return usuario;
    }
    
    public static boolean atualizaUsuario(Usuario usuario){
        
        try(Connection conexao = FabricaConexao.criaConexao()){
            
            String sql = "update usuario set nome = ?, pontuacao = ?, nivel = ?, id_ultima_questao = ?, corrigir = ? where chat_id = ?";
            PreparedStatement x = conexao.prepareStatement(sql);
            
            x.setString(1, usuario.getNome());
            x.setInt(2, usuario.getPontuacao());
            x.setInt(3, usuario.getNivel());
            x.setInt(4, usuario.getIdUltimaQuestao());
            x.setBoolean(5, usuario.temQueCorrigirResposta());
            x.setLong(6, usuario.getChatId());
            
            return x.execute();
            
        }catch(SQLException ex){
            System.out.println("Erro na execução da sql - atualizaUsuario.");
            //ex.printStackTrace();
//            log.log(Level.FINE, null,ex);            
            return false;
        }finally{
            FabricaConexao.encerraConexao();
        }
    }
    
}
