/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferramentas;

/**
 *
 * @author User
 */
public enum Plataformas {
    
    TELEGRAM("telegram");
    
    private String nomePlataforma;
    Plataformas(String nomePlataforma){
        this.nomePlataforma=nomePlataforma;
    }

    @Override
    public String toString() {
        return this.nomePlataforma;
    }
    
}
