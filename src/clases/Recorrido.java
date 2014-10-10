/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

/**
 *
 * @author Emi
 */
public class Recorrido {
    private final int nro;
    private final int v[];

    public Recorrido(int nro, int[] v) {
        this.nro = nro;
        this.v = v;        
    }    
    
    public int getProximaSala(int n){
        if(n==4) return 0;
        for (int i = 0; i < v.length; i++) {
            if(v[i]==n)
                return v[i+1];
        }
        return 0;
    }

    public int getNro() {
        return nro;
    }
    
    public String toString(){
        if(nro == 1) return "A-C-D";
            else if (nro == 2) return "A-B-C-D";
                else return "A-D";
                
    }
    
}
