# TrafficProject

Este proyecto es una aplicación escrita en **Java** que utiliza **JavaFX** para mostrar una cuadrícula de carreteras por la que se pueden mover vehículos de manera simulada. El código forma parte de un trabajo universitario (módulo `Algoritmo_Project_2_v2`).

## Características principales

- Generación dinámica de una cuadrícula de carreteras en la interfaz.
- Representación de intersecciones y carreteras mediante estructuras de grafos.
- Implementación del algoritmo de Dijkstra para calcular rutas más cortas.
- Posibilidad de añadir "autos" que se desplazan por la cuadrícula.
- Opciones en la interfaz para mostrar información del grafo y de las carreteras.

## Estructura del proyecto

- `src/` contiene el código fuente en paquetes como `business`, `domain`, `Structures` y `LogicStructures`.
- `presentation/Main.fxml` define la interfaz gráfica principal.
- `business/Main.java` es el punto de entrada de la aplicación.

## Compilación y ejecución

Este proyecto se desarrolló con Java 17 y requiere las librerías de **JavaFX**. Si
se intenta ejecutar la aplicación sin compilarla como módulo, la JVM mostrará
el error `Module Algoritmo_Project_2_v2 not found`. Compílalo de la siguiente
forma:

```bash
javac \
  --module-path "ruta/a/javafx/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -d bin $(find src -name '*.java')
```

Y para iniciar la aplicación utiliza también el `--module-path`:

```bash
java \
  --module-path "ruta/a/javafx/lib:bin" \
  --add-modules javafx.controls,javafx.fxml \
  -m Algoritmo_Project_2_v2/business.Main
```

Ajusta `ruta/a/javafx/lib` a la ubicación donde tengas instaladas las librerías
de JavaFX en tu sistema.

## Estado actual

El código contiene clases y utilidades básicas para la simulación de tráfico y su visualización. Algunas clases (por ejemplo `Incident` y `Road`) se encuentran vacías o en desarrollo. Este repositorio sirve de base para extender la simulación con más funcionalidades.

