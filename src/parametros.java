import jade.core.Agent;


public class parametros extends Agent {
    public void setup(){
        // Parámetros del algoritmo genético
        double tasaMutacion = 0.05;// es el 5% por gen para mutarlo
        int tamañoPoblacion = 50; // tamaño de poblacion
        int numGeneraciones = 20;// numero de generaciones 50
        double tasaCrossover = 0.7;
        int tamañoDatos = 100;  // número de datos generados

        //creacion del algortmo y su ejecucion
        Genetico ga = new Genetico(tasaMutacion, tamañoPoblacion, numGeneraciones, tasaCrossover, tamañoDatos);
        ga.ejecutar();

        //doDelete();
    }
}
