/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import static clases.Aleatorios.redondear;

/**
 *
 * @author Emi
 */
class Persona implements Comparable {
    private Recorrido recorrido;
    private double tiempoSalida;
    private int salaActual;
    private int estado;
    private int col;
    private int id;
    public static final int VISITANDO = 0;
    public static final int ESPERANDO = 1;
    public static final int FIN = 2;
    private static final String[] NOMBRE_ESTADO = {"Visit.", "Esper.","FIN"};
    private static final char[] NOMBRE_SALA={' ','A','B','C','D'};
    
    public Persona()
    {
        
    }
    public Persona(Recorrido recorrido,int id)
    {
        this.recorrido = recorrido;
        salaActual = 1;
        this.id = id;
    }
    public Persona(Recorrido recorrido, double tiempoSalida, int id) {
        this.recorrido = recorrido;
        this.tiempoSalida = tiempoSalida;
        salaActual = 1;        
        this.id=id;
    }

    public Recorrido getRecorrido() {
        return recorrido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecorrido(Recorrido recorrido) {
        this.recorrido = recorrido;
    }

    public double getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(double tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public int getSalaActual() {
        return salaActual;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
    public int getProxSala() {
        return recorrido.getProximaSala(salaActual);
    }
    
    public void setSalaActual(int salaActual) {
        this.salaActual = salaActual;
    }    

    public int getEstado() {
        return estado;
    }
    public String getDescripcionEstado() {
        return NOMBRE_ESTADO[estado] + " "+ NOMBRE_SALA[salaActual];
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo(Object o) {
        Persona p = (Persona) o;
        if(this.tiempoSalida > p.tiempoSalida)
            return -1;
        return 1;
    }
    @Override
    public String toString()
    {
        if(tiempoSalida!=0)
            return "#"+id+": "+redondear(tiempoSalida);
        else
            return "#"+id+": "+recorrido;
    }
    public String toString3()
    {
       return "\nRecorrido: " + recorrido +  " - Salida: " + tiempoSalida + " - Sala: " + salaActual + " - Prox: " + recorrido.getProximaSala(salaActual);
    }
}
