package clases;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Emiliano
 */
public final class Aleatorios {
    private Aleatorios(){}
    
    public static double random() {
        return Math.random();
    }

    public static double uniforme(double inferior, double superior) {
        return inferior + (superior - inferior) * random();
    }

    public static double exponencial(double media) {
        return (-media) * Math.log(1 - random());
    }

    public static double normal(double media, double desviacion) {
        double[] aux = new double[12];
        double numero = 0;
        double acumula = 0;
        do{
            for (int i = 0; i < aux.length; i++) {
                aux[i] = random();
                acumula += aux[i];
                if ((i + 1) % 12 == 0) {
                    double valor = ((acumula - 6) * desviacion) + media;
                    numero = valor;
                    acumula = 0;
                }
            }  
        }while(numero <= 0);  
        return numero;
    }

    public static int poisson(double lambda) {
        double p = 1;
        int x = 0;
        double a = Math.exp((-1) * lambda);
        do {
            p = p * random();
            x += 1;
        } while (p >= a);
        return x;
    }
        
    public static double redondear(double nro)
    {
        return Math.rint(nro * 100) / 100;
    }
}
