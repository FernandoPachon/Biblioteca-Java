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

public class ControladorSocios {

    private static final String NOMBRE_ARCHIVO = "socios.txt";
    private static final String SEPARADOR_CAMPO = ";";
    private static final String SEPARADOR_REGISTRO = "\n";

    public static void solicitarDatosParaRegistrar() {
        Scanner sc = new Scanner(System.in);
        String numero = JOptionPane.showInputDialog(null, "Ingrese número de socio:");
        String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre de socio:");
        String direccion = JOptionPane.showInputDialog(null, "Ingrese dirección de socio:");
    
        if (numero != null && nombre != null && direccion != null) {
            ControladorSocios.registrar(new Socio(numero, nombre, direccion));
            JOptionPane.showMessageDialog(null, "Registrado exitosamente", "Registro de Socio", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Se canceló el registro", "Registro de Socio", JOptionPane.WARNING_MESSAGE);
        }
    }
    public static void registrar(Socio socio) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, true));
            bufferedWriter.write(socio.getNumero() + SEPARADOR_CAMPO + socio.getNombre() + SEPARADOR_CAMPO
                    + socio.getDireccion() + SEPARADOR_REGISTRO);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    public static ArrayList<Socio> obtener() {
        ArrayList<Socio> socios = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(NOMBRE_ARCHIVO);
            bufferedReader = new BufferedReader(fileReader);
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] socioComoArreglo = linea.split(SEPARADOR_CAMPO);
                socios.add(new Socio(socioComoArreglo[0], socioComoArreglo[1], socioComoArreglo[2]));
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
            return socios;
        }
    }

    public static void imprimirSocios(ArrayList<Socio> socios) {
        ArrayList<Prestamo> prestamos = ControladorPrestamos.obtener();
    
        StringBuilder message = new StringBuilder();
        message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
        message.append(String.format("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", "#", "No. socio", "Nombre", "Direccion", "Libros prestados"));
        message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
    
        for (int x = 0; x < socios.size(); x++) {
            Socio socio = socios.get(x);
            int librosPrestados = ControladorPrestamos.cantidadLibrosPrestados(socio.getNumero(), prestamos);
            message.append(String.format("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", x + 1, socio.getNumero(), socio.getNombre(), socio.getDireccion(), librosPrestados));
            message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
        }
    
        JOptionPane.showMessageDialog(null, message.toString(), "Listado de Socios", JOptionPane.PLAIN_MESSAGE);
    }
    public static void imprimirSociosNoFiables(ArrayList<Socio> socios) {
        ArrayList<Prestamo> prestamos = ControladorPrestamos.obtener();
        System.out.println(
                "+-----+----------+----------------------------------------+----------------------------------------+--------------------+");
        System.out.printf("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", "#", "No. socio", "Nombre", "Direccion",
                "Libros prestados");
        System.out.println(
                "+-----+----------+----------------------------------------+----------------------------------------+--------------------+");
        for (int x = 0; x < socios.size(); x++) {
            Socio socio = socios.get(x);
            int librosPrestados = ControladorPrestamos.cantidadLibrosPrestados(socio.getNumero(), prestamos);
            if (librosPrestados < 10) {
                continue;
            }
            System.out.printf("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", x + 1, socio.getNumero(), socio.getNombre(),
                    socio.getDireccion(), librosPrestados);
            System.out.println(
                    "+-----+----------+----------------------------------------+----------------------------------------+--------------------+");
        }
    }

    public static int buscarSocioPorNumero(String numero, ArrayList<Socio> socios) {
        for (int x = 0; x < socios.size(); x++) {
            Socio socio = socios.get(x);
            if (socio.getNumero().equals(numero)) {
                return x;
            }
        }
        return -1;
    }

    public static Socio imprimirSociosYPedirSeleccion() {
        ArrayList<Socio> socios = ControladorSocios.obtener();
    
        while (true) {
            StringBuilder message = new StringBuilder();
            message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
            message.append(String.format("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", "#", "No. socio", "Nombre", "Direccion", "Libros prestados"));
            message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
    
            for (int x = 0; x < socios.size(); x++) {
                Socio socio = socios.get(x);
                int librosPrestados = ControladorPrestamos.cantidadLibrosPrestados(socio.getNumero(), ControladorPrestamos.obtener());
                message.append(String.format("|%-5s|%-10s|%-40s|%-40s|%-20s|\n", x + 1, socio.getNumero(), socio.getNombre(), socio.getDireccion(), librosPrestados));
                message.append("+-----+----------+----------------------------------------+----------------------------------------+--------------------+\n");
            }
    
            String numero = JOptionPane.showInputDialog(null, message.toString() + "Ingrese el número de socio:", "Selección de Socio", JOptionPane.PLAIN_MESSAGE);
    
            if (numero != null && !numero.isEmpty()) {
                int indice = ControladorSocios.buscarSocioPorNumero(numero, socios);
                if (indice == -1) {
                    JOptionPane.showMessageDialog(null, "No existe socio con ese número.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    return socios.get(indice);
                }
            } else {
                break;
            }
        }
        return null;
    }
    public static boolean borrarSocioPorNumero(String numero) {
        ArrayList<Socio> socios = obtener();
        boolean indice = borrarSocioPorNumero(numero);
        if (indice ) {
            socios.remove(indice);
            guardarSocios(socios);
            System.out.println("Libro borrado correctamente.");
            return true; // Devolver verdadero si se encontró y eliminó el libro
        } else {
            System.out.println("No se encontró ningún libro con ese código.");
            return false; // Devolver falso si no se encontró el libro
        }
    }
    private static void guardarSocios(ArrayList<Socio> socios) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, false));
            for (Socio socio : socios) {
                bufferedWriter.write(socio.getNumero() + SEPARADOR_CAMPO + socio.getNombre() + SEPARADOR_CAMPO + socio.getDireccion() + SEPARADOR_REGISTRO);
                bufferedWriter.newLine(); // Agregar un salto de línea al final de cada registro
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }
    public static void editarSocio() {
        ArrayList<Socio> socios = obtener();
        
        if (socios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay socios para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Socio socio = imprimirSociosYPedirSeleccion();
        
        if (socio == null) {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún socio para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nuevoNombre = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre del socio:", socio.getNombre());
        String nuevaDireccion = JOptionPane.showInputDialog(null, "Ingrese la nueva dirección del socio:", socio.getDireccion());
        
        if (nuevoNombre != null && nuevaDireccion != null) {
            socio.setNombre(nuevoNombre);
            socio.setDireccion(nuevaDireccion);
            actualizarSocio(socios);
            JOptionPane.showMessageDialog(null, "Socio editado correctamente.", "Edición de Socio", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Se canceló la edición del socio.", "Edición de Socio", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void actualizarSocio(ArrayList<Socio> socios) {
        guardarSocios(socios);
    }

} 