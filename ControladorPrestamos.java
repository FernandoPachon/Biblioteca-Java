import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 *
 * @author Fernando Pachón
 */
public class ControladorPrestamos {

    private static final String NOMBRE_ARCHIVO = "prestamos.txt";
    private static final String SEPARADOR_CAMPO = ";";
    private static final String SEPARADOR_REGISTRO = "\n";

    public static void registrar(Prestamo prestamo) {
        ControladorLibros.marcarComoPrestado(prestamo.getCodigoLibro());
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, true));
            bufferedWriter.write(prestamo.getCodigoLibro() + SEPARADOR_CAMPO + prestamo.getNumeroSocio()
                    + SEPARADOR_CAMPO + prestamo.getFechaFormateada() + SEPARADOR_REGISTRO);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    public static ArrayList<Prestamo> obtener() {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(NOMBRE_ARCHIVO);
            bufferedReader = new BufferedReader(fileReader);
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] prestamoComoArreglo = linea.split(SEPARADOR_CAMPO);
                prestamos.add(new Prestamo(prestamoComoArreglo[0], prestamoComoArreglo[1],
                        LocalDateTime.parse(prestamoComoArreglo[2],
                                new DateTimeFormatterBuilder().parseCaseInsensitive()
                                        .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter())));
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
        }
        return prestamos;
    }

    public static int cantidadLibrosPrestados(String numeroSocio, ArrayList<Prestamo> prestamos) {
        int cantidad = 0;
        for (int x = 0; x < prestamos.size(); x++) {
            Prestamo prestamo = prestamos.get(x);
            if (prestamo.getNumeroSocio().equals(numeroSocio)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public static void imprimirPrestamos(ArrayList<Prestamo> prestamos) {
        StringBuilder message = new StringBuilder();
        message.append("+-----+------------------------------+------------------------------+--------------------+\n");
        message.append(String.format("|%-5s|%-30s|%-30s|%-20s|\n", "No", "Codigo libro", "No. Socio", "Fecha"));
        message.append("+-----+------------------------------+------------------------------+--------------------+\n");

        for (int x = 0; x < prestamos.size(); x++) {
            Prestamo prestamo = prestamos.get(x);
            message.append(
                    String.format("|%-5d|%-30s|%-30s|%-20s|\n", x, prestamo.getCodigoLibro(), prestamo.getNumeroSocio(),
                            prestamo.getFechaFormateada()));
            message.append(
                    "+-----+------------------------------+------------------------------+--------------------+\n");
        }

        JOptionPane.showMessageDialog(null, message.toString(), "Listado de Préstamos", JOptionPane.PLAIN_MESSAGE);
    }

    public static void solicitarDatosYCrearPrestamo() {
        Libro libro = ControladorLibros.imprimirLibrosYPedirSeleccion();
        Socio socio = ControladorSocios.imprimirSociosYPedirSeleccion();
        ControladorPrestamos.registrar(new Prestamo(libro.getCodigo(), socio.getNumero(), LocalDateTime.now()));
        System.out.println("Prestamo registrado correctamente");
    }

    public static void editarPrestamo() {
        ArrayList<Prestamo> prestamos = obtener();
        Prestamo prestamoSeleccionado = seleccionarPrestamo(prestamos);
        if (prestamoSeleccionado != null) {
            // Mostrar opciones para editar o eliminar el préstamo seleccionado
            String[] opcionesEditar = { "Editar fecha del préstamo", "Eliminar préstamo", "Cancelar" };
            int opcionEditar = JOptionPane.showOptionDialog(
                    null,
                    "Selecciona una opción para editar el préstamo:",
                    "Editar Préstamo",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcionesEditar,
                    opcionesEditar[0]);
            switch (opcionEditar) {
                case 0:
                    editarFechaPrestamo(prestamoSeleccionado);
                    break;
                case 1:
                    // Eliminar el préstamo
                    int confirmacion = JOptionPane.showConfirmDialog(
                            null,
                            "¿Estás seguro de que deseas eliminar el préstamo?",
                            "Eliminar Préstamo",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        eliminarPrestamo(prestamoSeleccionado);
                        JOptionPane.showMessageDialog(null, "Préstamo eliminado correctamente", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                case 2:
                    // Cancelar la edición
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
    private static void eliminarPrestamo(Prestamo prestamo) {
        // Eliminar el préstamo de la lista de préstamos
       /*  ArrayList<Prestamo> prestamos = obtener();
        prestamos.remove(prestamo);
        guardarPrestamos(prestamos); */
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            ControladorLibros.marcarComoDevuelto(prestamo.getCodigoLibro());
            fileReader = new FileReader(NOMBRE_ARCHIVO);
            bufferedReader = new BufferedReader(fileReader);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("prestamos_temp.tmp", true));
            BufferedWriter bufferedWriterr = new BufferedWriter(bufferedWriter);
            String linea;
            
            while ((linea = bufferedReader.readLine()) != null) {
                System.out.println(linea);
               // linea=linea.replace("500;1;2024-04-07 18:50:48","0");
               // String lineaActualizar="1;1;2024-04-08 00:27:43";
                System.out.println(prestamo.prestamoPorLista());
                if (linea.equals(prestamo.prestamoPorLista())) {
                    bufferedWriterr.write("");
                } else {
                    bufferedWriterr.write(linea); 
                }
                //linea = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedWriterr.close();
            File archivoOriginal = new File(NOMBRE_ARCHIVO);
            File archivoTemporal = new File("prestamos_temp.tmp");
            archivoOriginal.delete();
            archivoTemporal.renameTo(archivoOriginal);

            System.out.println("El archivo se ha actualizado correctamente.");

        } catch (IOException e) {
            System.out.println("Error al leer/escribir el archivo: " + e.getMessage());
        }
        // Mostrar mensaje de confirmación
        JOptionPane.showMessageDialog(null, "Préstamo eliminado correctamente", "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static void guardarPrestamos(ArrayList<Prestamo> prestamos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO))) {
            for (Prestamo prestamo : prestamos) {
                writer.write(prestamo.getCodigoLibro() + SEPARADOR_CAMPO + prestamo.getNumeroSocio() + SEPARADOR_CAMPO
                        + prestamo.getFechaFormateada() + SEPARADOR_REGISTRO);
               // writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    private static void editarFechaPrestamo(Prestamo prestamo) {
        String nuevaFechaStr = JOptionPane.showInputDialog(null,
                "Ingrese la nueva fecha del préstamo (yyyy-MM-dd HH:mm:ss):");
        if (nuevaFechaStr != null && !nuevaFechaStr.isEmpty()) {
            try {
                LocalDateTime nuevaFecha = LocalDateTime.parse(nuevaFechaStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                prestamo.setFecha(nuevaFecha);
                guardarPrestamos();
                JOptionPane.showMessageDialog(null, "Fecha del préstamo actualizada correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void guardarPrestamos() {
        ArrayList<Prestamo> prestamos = obtener();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO))) {
            for (Prestamo prestamo : prestamos) {
                writer.write(prestamo.getCodigoLibro() + SEPARADOR_CAMPO + prestamo.getNumeroSocio() + SEPARADOR_CAMPO
                        + prestamo.getFechaFormateada() + SEPARADOR_REGISTRO);
                //writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }
    
    private static Prestamo seleccionarPrestamo(ArrayList<Prestamo> prestamos) {
        Prestamo[] arrayPrestamos = prestamos.toArray(new Prestamo[0]);
        Prestamo prestamoSeleccionado = (Prestamo) JOptionPane.showInputDialog(
                null,
                "Selecciona un préstamo:",
                "Editar Préstamo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayPrestamos,
                arrayPrestamos[0]);
        return prestamoSeleccionado;
    }
}