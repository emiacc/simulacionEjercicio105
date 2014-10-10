/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;
/**
 *
 * @author Emiliano
 */
public class Principal {
        
        
        
        
    public static void main(String[] args) {
        
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Ventana vp = new Ventana();
                vp.setVisible(true);
            }
        }); 
        
        /*
        double llegada;
        int lote;
        int recorrido;
        
       
               
        
        salaA.addInOrder(new Persona(dos, 5.21));
        salaA.addInOrder(new Persona(uno, 6.21));
        salaA.addInOrder(new Persona(dos, 8.10));
        salaA.addInOrder(new Persona(dos, 9.21));
        salaA.addInOrder(new Persona(dos, 9.71));
        salaA.addInOrder(new Persona(uno, 10));
        if(!salaA.addInOrder(new Persona(uno, 9.5)))
            colaA.add(new Persona(uno, 9.5));
        if(!salaA.addInOrder(new Persona(tres, 12)))
            colaA.add(new Persona(tres, 12));
        if(!salaA.addInOrder(new Persona(tres, 13)))
            colaA.add(new Persona(tres, 13));
        if(!salaA.addInOrder(new Persona(tres, 14)))
            colaA.add(new Persona(tres, 14));
        System.out.println(salaA);
        System.out.println(colaA);
        
        
        Persona ultima = (Persona) salaA.removeLast();
        ultima.setSalaActual(ultima.getProxSala());
        salaB.addInOrder(ultima);
        if(!colaA.isEmpty())
        {   ultima = (Persona) colaA.remove();
            salaA.addInOrder(ultima);
        }
        
        System.out.println(salaA);
        System.out.println(colaA);
        
        System.out.println(salaB);         
        
        
          */     
        
    }
}
