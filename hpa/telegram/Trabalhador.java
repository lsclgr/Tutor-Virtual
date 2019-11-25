package hpa.telegram;

import hpa.Tarefa;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Trabalhador extends Thread {

    private static Vector<Tarefa> sacolaTarefas;
    private boolean alive;
    
    public Trabalhador(Vector<Tarefa> sacola) {
        sacolaTarefas = sacola;
        this.alive = true;
    }

    public void kill(){
        try {
            this.alive = false;
            this.interrupt();
        } catch (Throwable ex) {
            System.err.println("erro ao encerrar as thread's");
        }
    }
    
    public void acorda(){
        synchronized(this){
            this.notify();
        }
    }
    
    @Override
    public void run() {
        while (this.alive) {
            //synchronized(sacolaTarefas){
            if (sacolaTarefas.isEmpty()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    System.err.println("Thread em espera encerrada!!!");
                }
            } else {
                //podem chagar neste ponto duas threads para um recurso apenas
                Tarefa novaTarefa = null;
                synchronized(sacolaTarefas){
                    if (!sacolaTarefas.isEmpty()){
                        novaTarefa = sacolaTarefas.remove(0);
                    }
                }
                if(novaTarefa != null){
                    novaTarefa.execute();
                }
            }
        }
    }
}
