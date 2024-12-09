import java.util.Random;

public class SeleccionRuleta {

    public static int seleccionar(double[] fitness) {
        int n = fitness.length;
        double fitnessTotal = 0.0;

        // sumamos los valores de fitness
        for (double f : fitness) {
            fitnessTotal += f;
        }

        // para que no halla una división por cero
        if (fitnessTotal == 0) {
            throw new IllegalArgumentException("El fitness total no puede ser cero");
        }

        // calculamos probabilidades acumuladas
        double[] probabilidadesAcumuladas = new double[n];
        probabilidadesAcumuladas[0] = fitness[0] / fitnessTotal;

        for (int i = 1; i < n; i++) {
            probabilidadesAcumuladas[i] = probabilidadesAcumuladas[i - 1] + (fitness[i] / fitnessTotal);
        }

        // seleccionar aleatoriamente basado en la probabilidad acumulada
        double r = Math.random();

        // para encontrar el índice del cromosoma seleccionado
        for (int i = 0; i < n; i++) {
            if (r <= probabilidadesAcumuladas[i]) {
                return i;
            }
        }

        return n - 1;  //si no se encontro pasar el cromosoma
    }
}
