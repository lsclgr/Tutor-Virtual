package modelo;

public class Resposta {
    private int idQuestao;
    private long chatIdUsuario;
    private int idAlternativaEscolhida;
    //private Date data;

    public Resposta() {
    }

    public Resposta(int idQuestao, long chatIdUsuario, int alternativaEscolhida) {
        this.idQuestao = idQuestao;
        this.chatIdUsuario = chatIdUsuario;
        this.idAlternativaEscolhida = alternativaEscolhida;
    }

    public int getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }

    public long getChatIdUsuario() {
        return chatIdUsuario;
    }

    public void setChatIdUsuario(long chatIdUsuario) {
        this.chatIdUsuario = chatIdUsuario;
    }

    public int getIdAlternativaEscolhida() {
        return idAlternativaEscolhida;
    }

    public void setIdAlternativaEscolhida(int idAlternativaEscolhida) {
        this.idAlternativaEscolhida = idAlternativaEscolhida;
    }
    @Override
    public String toString(){
        return ("Id questão: "+idQuestao + "  Id Alternativa: " + idAlternativaEscolhida + "Chat Id: " + chatIdUsuario);
    }
}
