import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
/**
 *
 * @author Fernando Pachón
 */
public class ControladorLibros {
    private static final int CAPACIDAD_INICIAL = 10; // Capacidad inicial del array
    private static final int FACTOR_CRECIMIENTO = 2; // Factor de crecimiento del array

    private Libro[] libros; // Array para almacenar los libros
    private int cantidadLibros; // Cantidad actual de libros en el array

    public ControladorLibros() {
        libros = new Libro[CAPACIDAD_INICIAL];
        cantidadLibros = 0;
    }
    private static final String NOMBRE_ARCHIVO = "libros.txt";
    private static final String SEPARADOR_CAMPO = ";";
    private static final String SEPARADOR_REGISTRO = "\n";

    public static void solicitarDatosParaRegistrar() {
        String codigo = JOptionPane.showInputDialog(null, "Ingrese el código del libro:");
        String titulo = JOptionPane.showInputDialog(null, "Ingrese el título del libro:");
        String autor = JOptionPane.showInputDialog(null, "Ingrese el autor del libro:");
        
        // Preguntar si el libro está disponible
        String disponibleStr = JOptionPane.showInputDialog(null, "El libro está disponible? [true/false]");
        boolean disponible = Boolean.valueOf(disponibleStr);
    
        String localizacion = JOptionPane.showInputDialog(null, "Ingrese la localización del libro:");
        String signatura = JOptionPane.showInputDialog(null, "Ingrese la signatura del libro:");
    
        ControladorLibros.registrar(new Libro(codigo, titulo, autor, localizacion, signatura, disponible));
    
        JOptionPane.showMessageDialog(null, "Libro registrado correctamente", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
    }
    public void agregarLibro(Libro libro) {
        if (cantidadLibros == libros.length) {
            // Si el array está lleno, se duplica su tamaño
            aumentarCapacidad();
        }
        libros[cantidadLibros++] = libro;
    }
    private void aumentarCapacidad() {
        int nuevaCapacidad = libros.length * FACTOR_CRECIMIENTO;
        Libro[] nuevoArray = new Libro[nuevaCapacidad];
        for (int i = 0; i < cantidadLibros; i++) {
            nuevoArray[i] = libros[i];
        }
        libros = nuevoArray;
    }
    public static void registrar(Libro libro) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, true));
            bufferedWriter.write(libro.getCodigo()
                    + SEPARADOR_CAMPO + libro.getTitulo()
                    + SEPARADOR_CAMPO + libro.getAutor()
                    + SEPARADOR_CAMPO + String.valueOf(libro.isDisponible())
                    + SEPARADOR_CAMPO + libro.getLocalizacion()
                    + SEPARADOR_CAMPO + libro.getSignatura() + SEPARADOR_REGISTRO);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    public static void guardarLibros(ArrayList<Libro> libros) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, false));
            for (int x = 0; x < libros.size(); x++) {
                Libro libro = libros.get(x);
                bufferedWriter.write(libro.getCodigo()
                        + SEPARADOR_CAMPO + libro.getTitulo()
                        + SEPARADOR_CAMPO + libro.getAutor()
                        + SEPARADOR_CAMPO + String.valueOf(libro.isDisponible())
                        + SEPARADOR_CAMPO + libro.getLocalizacion()
                        + SEPARADOR_CAMPO + libro.getSignatura() + SEPARADOR_REGISTRO);

            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    public static ArrayList<Libro> obtener() {
        ArrayList<Libro> libros = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(NOMBRE_ARCHIVO);
            bufferedReader = new BufferedReader(fileReader);
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] libroComoArreglo = linea.split(SEPARADOR_CAMPO);
                libros.add(new Libro(libroComoArreglo[0], libroComoArreglo[1], libroComoArreglo[2], libroComoArreglo[4],
                        libroComoArreglo[5], Boolean.valueOf(libroComoArreglo[3])));
            }
        } catch (IOException e) {
            System.out.println("Excepción leyendo archivo: " + e.getMessage());
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.out.println("Excepción cerrando: " + e.getMessage());
            }
            return libros;
        }
    }

    public static int buscarPorCodigo(String codigo, ArrayList<Libro> libros) {
        for (int x = 0; x < libros.size(); x++) {
            Libro libroActual = libros.get(x);
            if (libroActual.getCodigo().equals(codigo)) {
                return x;
            }
        }
        return -1;
    }

    public static void marcarComoPrestado(String codigoLibro) {
        ArrayList<Libro> libros = ControladorLibros.obtener();
        int indice = ControladorLibros.buscarPorCodigo(codigoLibro, libros);
        if (indice == -1) {
            return;
        }
        libros.get(indice).setDisponible(false);
        ControladorLibros.guardarLibros(libros);
    }

    public static void cambiarSignatura(String codigoLibro,String nuevoTitulo,String nuevoAutor,String nuevaLocalizacion, String nuevaSignatura) {
        ArrayList<Libro> libros = ControladorLibros.obtener();
        int indice = ControladorLibros.buscarPorCodigo(codigoLibro, libros);
        if (indice == -1) {
            return;
        }
        libros.get(indice).setTitulo(nuevoTitulo);
        libros.get(indice).setAutor(nuevoAutor);
        libros.get(indice).setLocalizacion(nuevaLocalizacion);
        libros.get(indice).setSignatura(nuevaSignatura);
        
        
        ControladorLibros.guardarLibros(libros);
    }

   public static void solicitarDatosParaCambiarSignatura() {
    Libro libro = ControladorLibros.imprimirLibrosYPedirSeleccion();
    if (!libro.isDisponible()) {
        JOptionPane.showMessageDialog(null, "No puede cambiar un libro que no está disponible", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String nuevoTitulo = JOptionPane.showInputDialog(null, "Ingrese nuevo título:");
    if (nuevoTitulo == null) {
        return; // El usuario canceló la operación
    }
    
    String nuevoAutor = JOptionPane.showInputDialog(null, "Ingrese nuevo autor:");
    if (nuevoAutor == null) {
        return; // El usuario canceló la operación
    }
    
    String nuevaLocalizacion = JOptionPane.showInputDialog(null, "Ingrese nueva localización:");
    if (nuevaLocalizacion == null) {
        return; // El usuario canceló la operación
    }

    String nuevaSignatura = JOptionPane.showInputDialog(null, "Ingrese nueva signatura:");
    if (nuevaSignatura == null) {
        return; // El usuario canceló la operación
    }

    ControladorLibros.cambiarSignatura(libro.getCodigo(), nuevoTitulo, nuevoAutor, nuevaLocalizacion, nuevaSignatura);
    JOptionPane.showMessageDialog(null, "Información del libro actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
}

    public static void imprimirLibros(ArrayList<Libro> libros) {
        StringBuilder output = new StringBuilder();
        output.append(
            "+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");
        output.append(
            "|%-5s|%-10s|%-40s|%-20s|%-10s|%-30s|%-30s|\n".formatted("No", "Código", "Título", "Autor", "Disponible",
                    "Localización", "Signatura"));
        output.append(
            "+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");
    
        for (int x = 0; x < libros.size(); x++) {
            Libro libro = libros.get(x);
            output.append(
                String.format("|%-5d|%-10s|%-40s|%-20s|%-10s|%-30s|%-30s|\n", x + 1, libro.getCodigo(),
                        libro.getTitulo(),
                        libro.getAutor(), libro.isDisponible() ? "Si" : "No", libro.getLocalizacion(),
                        libro.getSignatura()));
            output.append(
                "+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");
        }
    
        JOptionPane.showMessageDialog(null, output.toString(), "Listado de Libros", JOptionPane.PLAIN_MESSAGE);
    }
    public static boolean borrarLibroPorCodigo(String codigo) {
    ArrayList<Libro> libros = obtener();
    int indice = buscarPorCodigo(codigo, libros);
    if (indice != -1) {
        libros.remove(indice);
        guardarLibros(libros);
        System.out.println("Libro borrado correctamente.");
        return true; // Devolver verdadero si se encontró y eliminó el libro
    } else {
        System.out.println("No se encontró ningún libro con ese código.");
        return false; // Devolver falso si no se encontró el libro
    }
}
public static Libro imprimirLibrosYPedirSeleccion() {
    ArrayList<Libro> libros = ControladorLibros.obtener();

    while (true) {
        StringBuilder message = new StringBuilder();
        message.append("+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");
        message.append(String.format("|%-5s|%-10s|%-40s|%-20s|%-10s|%-30s|%-30s|\n", "No", "Codigo", "Titulo", "Autor", "Disponible", "Localizacion", "Signatura"));
        message.append("+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");

        for (int x = 0; x < libros.size(); x++) {
            Libro libro = libros.get(x);
            message.append(String.format("|%-5d|%-10s|%-40s|%-20s|%-10s|%-30s|%-30s|\n", x + 1, libro.getCodigo(), libro.getTitulo(), libro.getAutor(), libro.isDisponible() ? "Si" : "No", libro.getLocalizacion(), libro.getSignatura()));
            message.append("+-----+----------+----------------------------------------+--------------------+----------+------------------------------+------------------------------+\n");
        }

        String codigo = JOptionPane.showInputDialog(null, message.toString() + "Ingrese el código del libro:", "Selección de Libro", JOptionPane.PLAIN_MESSAGE);

        if (codigo != null && !codigo.isEmpty()) {
            int indice = ControladorLibros.buscarPorCodigo(codigo, libros);
            if (indice == -1) {
                JOptionPane.showMessageDialog(null, "No existe libro con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Libro libro = libros.get(indice);
                if (libro.isDisponible()) {
                    return libro;
                } else {
                    JOptionPane.showMessageDialog(null, "El libro está ocupado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            break;
        }
    }
    return null;
}
public static void marcarComoDisponible(String codigoLibro) {
    throw new UnsupportedOperationException("Unimplemented method 'marcarComoDisponible'");
}
} 