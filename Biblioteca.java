import javax.swing.JOptionPane;
/**
 *
 * @author Fernando Pachón
 */
public class Biblioteca {

    public static void main(String[] args) {
        String eleccion = "";
        while (!eleccion.equals("Salir")) {
            Object[] options = {"Gestión de Libros", "Gestión de Socios", "Gestión de Préstamos", "Salir"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Elige una opción:",
                    "Biblioteca Fernando Pachón",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch (result) {
                case 0:
                //opcion libros
                    gestionLibros();
                    break;
                case 1:
                 //opcion socios
                    gestionSocios();
                    break;
                case 2:
                 //opcion prestamos
                    gestionPrestamos();
                    break;
                case 3:
                //opcion salir
                    eleccion = "Salir";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private static void gestionLibros() {
        String eleccionLibros = "";
        while (!eleccionLibros.equals("Volver al menú principal")) {
            Object[] options = {"Registrar libro", "Ver libros", "Borrar libros","Editar libros", "Volver al menú principal"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Elige una opción para la gestión de libros:",
                    "Gestión de Libros",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch (result) {
                case 0:
                    //crear
                    ControladorLibros.solicitarDatosParaRegistrar();
                    break;
                case 1:
                //ver
                    ControladorLibros.imprimirLibros(ControladorLibros.obtener());
                    break;
                case 2:
                //borrar
                String codigoBorrar = JOptionPane.showInputDialog(null, "Ingrese el código del libro a borrar:");
                if (codigoBorrar != null && !codigoBorrar.isEmpty()) {
                    boolean borrado = ControladorLibros.borrarLibroPorCodigo(codigoBorrar);
                    if (borrado) {
                        JOptionPane.showMessageDialog(null, "Libro borrado correctamente.", "Borrar Libro", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró ningún libro con ese código.", "Borrar Libro", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un código válido.", "Borrar Libro", JOptionPane.WARNING_MESSAGE);
                }
                    break;
                case 3:
                //editar
                    ControladorLibros.solicitarDatosParaCambiarSignatura();
                    break;
                case 4:
                //salir
                    eleccionLibros = "Volver al menú principal";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private static void gestionSocios() {
        String eleccionSocios = "";
        while (!eleccionSocios.equals("Volver al menú principal")) {
            Object[] options = {"Registrar socio", "Ver socios", "Borrar socios","Editar socios", "Volver al menú principal"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Elige una opción para la gestión de socios:",
                    "Gestión de Socios",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch (result) {
                case 0:
                //crear
                    ControladorSocios.solicitarDatosParaRegistrar();
                    break;
                case 1:
                //ver
                    ControladorSocios.imprimirSocios(ControladorSocios.obtener());
                    break;
                case 2:
                    //borrar
                String codigoBorrar = JOptionPane.showInputDialog(null, "Ingrese el código del socio a borrar:");
                if (codigoBorrar != null && !codigoBorrar.isEmpty()) {
                    boolean borrado = ControladorSocios.borrarSocioPorNumero(codigoBorrar);
                    if (borrado) {
                        JOptionPane.showMessageDialog(null, "Socio borrado correctamente.", "Borrar Socio", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró ningún socio con ese código.", "Borrar Socio", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    
                    JOptionPane.showMessageDialog(null, "Debe ingresar un código válido.", "Editar Socio", JOptionPane.WARNING_MESSAGE);
                }
                    break;
                case 3:
                //editar
                    ControladorSocios.editarSocio();
                    break;
                case 4:
                    eleccionSocios = "Volver al menú principal";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private static void gestionPrestamos() {
        String eleccionPrestamos = "";
        while (!eleccionPrestamos.equals("Volver al menú principal")) {
            Object[] options = {"Registrar préstamo", "Ver préstamos", "Editar préstamo", "Volver al menú principal"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Elige una opción para la gestión de préstamos:",
                    "Gestión de Préstamos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch (result) {
                case 0:
                //crear prestamos
                    ControladorPrestamos.solicitarDatosYCrearPrestamo();
                    break;
                case 1:
                //ver prestamos
                    ControladorPrestamos.imprimirPrestamos(ControladorPrestamos.obtener());
                    break;
                case 2:
                //editar prestamos
                    ControladorPrestamos.editarPrestamo();
                    break;
                case 3:
                //volver al menu
                    eleccionPrestamos = "Volver al menú principal";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
    
}
