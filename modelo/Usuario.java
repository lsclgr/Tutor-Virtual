package modelo;

public class Usuario {
    private long chatId;
    private String nome;
    private int pontuacao;
    private int nivel;
    private int idUltimaQuestao;
    private boolean eNecessarioCorrigirResposta;
    
    public Usuario() {
    }

    public Usuario(long chatId, String nome){
        this.chatId = chatId;
        this.nome = nome;
        this.pontuacao = 0;
        this.nivel = 1;
        this.eNecessarioCorrigirResposta=false;
    }

    public Usuario(long chatId, String nome, int pontuacao, int nivel, int idUltimaQuestao, boolean eNecessarioCorrigirResposta) {
        this.chatId = chatId;
        this.nome = nome;
        this.pontuacao = pontuacao;
        this.nivel = nivel;
        this.idUltimaQuestao = idUltimaQuestao;
        this.eNecessarioCorrigirResposta=eNecessarioCorrigirResposta;
    }
    
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public int getPontuacao(){
        return pontuacao;
    }
    
    public void setPontuacao(int novaPontuacao){
        this.pontuacao = novaPontuacao;
    } 

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getIdUltimaQuestao() {
        return idUltimaQuestao;
    }

    public void setIdUltimaQuestao(int idUltimaQuestao) {
        this.idUltimaQuestao = idUltimaQuestao;
    }

    public boolean temQueCorrigirResposta() {
        return eNecessarioCorrigirResposta;
    }

    public void setCorrigirResposta(boolean eNecessarioCorrigirResposta) {
        this.eNecessarioCorrigirResposta = eNecessarioCorrigirResposta;
    }
    
    
    
    @Override
    public String toString(){
        return ("Nome: " + nome + "    Chat Id: " + chatId + "    Pontuação: " + pontuacao + "   Nivel: " + nivel + "   Id ultima questão: "+ idUltimaQuestao);
    }
}
