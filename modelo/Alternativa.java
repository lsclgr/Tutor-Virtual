package modelo;

public class Alternativa {
    //atributos (Características).
    private String enunciado;
    private String dica;
    private int id;
    private int idQuestao;

    public Alternativa() {
    }

    public Alternativa(String enunc, String dica) {
        this.enunciado = enunc;
        this.dica = dica;
    }

    public Alternativa(String enunc, String dica, int id) {
        this.enunciado = enunc;
        this.dica = dica;
        this.id = id;
    }

    public Alternativa(String enunc, String dica, int id, int idQuestao) {
        this.enunciado = enunc;
        this.dica = dica;
        this.id = id;
        this.idQuestao = idQuestao;
    }

    //Métodos.
    public String getDica() {
        return dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }
    
    @Override
    public String toString(){
        return ("Id: " + id + "   ID Questão: " + idQuestao + "   Enunciado: " + enunciado + "   Dica: " + dica);
    }
         
}  