package modelo;

import java.util.List;

public class Questao {

    private int id;
    private String enunciado;
    private List<Alternativa> alternativas;
    private int idAlternativaCorreta;
    private int nivel;
    private int tipo;

    public Questao() {
    }

    public Questao(int id, String enunciado, List<Alternativa> alternativas, int alternativaCorreta, int nivel, int tipo) {
        this.id = id;
        this.enunciado = enunciado;
        this.alternativas = alternativas;
        this.idAlternativaCorreta = alternativaCorreta;
        this.nivel = nivel;
        this.tipo = tipo;
    }

    public Questao(String enunciado, List<Alternativa> alternativas, int alternativaCorreta, int nivel, int tipo) {
        this.enunciado = enunciado;
        this.alternativas = alternativas;
        this.idAlternativaCorreta = alternativaCorreta;
        this.nivel = nivel;
        this.tipo = tipo;
    }

    public Questao(int id, String enunciado, int nivel, int idAlternativaCorreta, int tipo) {
        this.id = id;
        this.enunciado = enunciado;
        this.nivel = nivel;
        this.idAlternativaCorreta = idAlternativaCorreta;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int codigo) {
        this.id = codigo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public int getIdAlternativaCorreta() {
        return idAlternativaCorreta;
    }

    public void setIdAlternativaCorreta(int idAlternativaCorreta) {
        this.idAlternativaCorreta = idAlternativaCorreta;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        String resultado = enunciado;
        for (int i = 0; i < alternativas.size(); i++) {
            resultado += "\n" + Integer.toString(i + 1) + " - " + alternativas.get(i).getEnunciado();
        }
        return resultado;
    }

    public String alternativasToString() {
        String resultado = "";
        for (int i = 0; i < alternativas.size(); i++) {
            resultado += "\n" + Integer.toString(i + 1) + " - " + alternativas.get(i).getEnunciado();
        }
        return resultado;
    }

    public Alternativa getAlternativaCorreta() {
        for (int i = 0; i <=2; i++) {
            if (alternativas.get(i).getId() == idAlternativaCorreta) {
                System.out.println(alternativas.get(i).getId() + " == " + idAlternativaCorreta + "?");
                return alternativas.get(i);
            }
            System.out.println("N");
            System.out.println("");
        }
        return null;
    }

}
