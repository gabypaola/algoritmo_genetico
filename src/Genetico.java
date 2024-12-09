import java.util.Arrays;
import java.util.Random;

public class Genetico {

    private double tasaMutacion;
    private int tamañoPoblacion;
    private int numGeneraciones;
    private double tasaCrossover;
    private int tamañoDatos;

    // mi constructor
    public Genetico(double tasaMutacion, int tamañoPoblacion, int numGeneraciones, double tasaCrossover, int tamañoDatos) {
        this.tasaMutacion = tasaMutacion;
        this.tamañoPoblacion = tamañoPoblacion;
        this.numGeneraciones = numGeneraciones;
        this.tasaCrossover = tasaCrossover;
        this.tamañoDatos = tamañoDatos;
    }

    // función de fitness (error cuadrático para lineal)
    public double evaluarFitness(double[] coeficientes, double[] X, double[] y) {
        double error = 0.0;
        for (int i = 0; i < X.length; i++) {
            double prediccion = coeficientes[0] + coeficientes[1] * X[i];
            error += Math.pow(y[i] - prediccion, 2);
        }
        return error / X.length;
    }
    // inicalizo mi poblacion
    public double[][] inicializarPoblacion() {
        Random rand = new Random();
        double[][] poblacion = new double[tamañoPoblacion][2];  //las dos betas
        for (int i = 0; i < tamañoPoblacion; i++) {
            // generamos valores aleatorios para b0 y b1
            poblacion[i][0] = rand.nextDouble();  // b0 aleatorio entre 0 y 1
            poblacion[i][1] = rand.nextDouble();  // b1 aleatorio entre 0 y 1
        }
        return poblacion;
    }

    // generamos los datos X e y aleatorios
    public void generarDatosAleatorios(double[] X, double[] y) {
        Random rand = new Random();
        double beta0 = rand.nextDouble();  // datos aleatorios b0 entre 0 y 1
        double beta1 = rand.nextDouble();  // datos aleatorios b1 entre 0 y 1

        for (int i = 0; i < tamañoDatos; i++) {
            X[i] = rand.nextDouble();
            y[i] = beta0 + beta1 * X[i] + (rand.nextGaussian() * 0.05);  // y con un pequeño ruido aleatorio
        }
    }

    // funcion crossover
    public double[][] crossover(double[][] poblacion, double[] fitness) {
        int n = poblacion.length;
        int m = poblacion[0].length;
        double[][] nuevaPoblacion = new double[n][m];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            if (rand.nextDouble() < tasaCrossover) {
                int padre1Index = SeleccionRuleta.seleccionar(fitness);
                int padre2Index = SeleccionRuleta.seleccionar(fitness);

                int puntoCrossover = rand.nextInt(m);

                for (int j = 0; j < m; j++) {
                    if (j < puntoCrossover) {
                        nuevaPoblacion[i][j] = poblacion[padre1Index][j];
                    } else {
                        nuevaPoblacion[i][j] = poblacion[padre2Index][j];
                    }
                }
            } else {
                nuevaPoblacion[i] = Arrays.copyOf(poblacion[i], m);
            }
        }

        return nuevaPoblacion;
    }

    // funcion de mutación
    public void mutar(double[][] poblacion) {
        Random rand = new Random();
        for (int i = 0; i < poblacion.length; i++) {
            for (int j = 0; j < poblacion[i].length; j++) {
                if (rand.nextDouble() < tasaMutacion) {
                    poblacion[i][j] += rand.nextGaussian() * 0.1;  // para hacer el cambio
                }
            }
        }
    }

    // funcion para calcular las probabilidades de cada cromosoma
    public double[] calcularProbabilidades(double[] fitness) {
        double totalFitness = 0.0;
        for (double f : fitness) {
            totalFitness += f;
        }

        double[] probabilidades = new double[fitness.length];
        for (int i = 0; i < fitness.length; i++) {
            probabilidades[i] = fitness[i] / totalFitness;
        }

        return probabilidades;
    }

    // funcion para calcular las probabilidades acumuladas
    public double[] calcularProbabilidadesAcumuladas(double[] probabilidades) {
        double[] probabilidadesAcumuladas = new double[probabilidades.length];
        probabilidadesAcumuladas[0] = probabilidades[0];
        for (int i = 1; i < probabilidades.length; i++) {
            probabilidadesAcumuladas[i] = probabilidadesAcumuladas[i - 1] + probabilidades[i];
        }
        return probabilidadesAcumuladas;
    }

    // ejecucion del algoritmo genético
    public void ejecutar() {
        // se inicializa los datos aleatorios X e y
        double[] X = new double[tamañoDatos];
        double[] y = new double[tamañoDatos];
        generarDatosAleatorios(X, y);

        // se inicializa la población aleatoria
        double[][] poblacion = inicializarPoblacion();

        for (int generacion = 0; generacion < numGeneraciones; generacion++) {

            double[] fitness = new double[poblacion.length];
            for (int i = 0; i < poblacion.length; i++) {
                fitness[i] = evaluarFitness(poblacion[i], X, y);
            }

            // calcula probabilidades
            double[] probabilidades = calcularProbabilidades(fitness);
            double[] probabilidadesAcumuladas = calcularProbabilidadesAcumuladas(probabilidades);

            // imprime la probabilidad de cada cromosoma y la probabilidad acumulada
            System.out.println("Generación " + generacion);
            for (int i = 0; i < poblacion.length; i++) {
                System.out.println("Cromosoma " + (i + 1) + ": Fitness = " + fitness[i] +
                        ", Probabilidad = " + probabilidades[i] +
                        ", Probabilidad acumulada = " + probabilidadesAcumuladas[i]);
            }

            // encontrar el índice del mejor fitness
            int mejorIndice = 0;
            for (int i = 1; i < fitness.length; i++) {
                if (fitness[i] < fitness[mejorIndice]) {
                    mejorIndice = i;
                }
            }

            // obtenemos las mejores betas correspondientes al mejor fitness
            double[] mejoresBetas = poblacion[mejorIndice];

            // imprime las mejores betas
            System.out.println("mejores betas de la generación:");
            System.out.println("B0 = " + mejoresBetas[0] + ", B1 = " + mejoresBetas[1]);

            // crossover
            poblacion = crossover(poblacion, fitness);

            // mutación
            mutar(poblacion);

            // imprimir el mejor fitness de la generación
            double mejorFitness = fitness[mejorIndice];
            System.out.println("mejor fitness de la generación: " + mejorFitness);
        }
    }

}