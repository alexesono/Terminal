
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TerminalClases1 {
	public static Path rutabase = Paths.get(System.getProperty("user.dir"));

	public static void main(String[] args) throws IOException {
		Scanner entrada = new Scanner(System.in);
		boolean close = false;
		do {
			System.out.print(rutabase.toAbsolutePath() + "$" + " ");
			String comando = entrada.nextLine();
			String[] valores = comando.split(" ");
			String opcion = valores[0];

			// System.out.println(opcion);
			switch (opcion) {
			case "help":
				if (valores.length != 1) {
					System.out.println("El comando Help requiere un solo parametro");
					continue;
				}
				help(comando);
				break;
			case "cd":
					
				cd(valores);
				break;

			case "mkdir":

				if (valores.length != 2) {
					System.out.println(
							"Error: el comando mkfile requiere dos parámetros: el nombre del fichero y el contenido del fichero");
					continue;
				}
				mkdir(valores[1]);

				break;
			case "info":
				info(valores);

				break;
			case "cat":
				if (valores.length != 2) {
					System.out.println("Error: el comando 'cat' requiere un parametro");
					break;
				}

				cat(valores[1]);
				break;
			case "top":

				if (valores.length < 3) {
					System.out.println("Error: numero de parametros incorrecto");
					continue;
				}
				try {
					int numLineas = Integer.parseInt(valores[1]);
					String nombreFichero = valores[2];
					top(numLineas, nombreFichero);
				} catch (NumberFormatException e) {
					System.out.println("Error: el numero de lineas debe ser un entero");
				} catch (IOException e) {
					System.out.println("Error: no se puede leer el fichero");
				}
				break;
			case "mkfile":

				// El comando mkfile requiere dos parámetros: el nombre del fichero y el
				// contenido del fichero
				if (valores.length != 3) {
					System.out.println(
							"Error: el comando mkfile requiere dos parámetros: el nombre del fichero y el contenido del fichero");
					continue;
				}
				// Llamamos al método que implementa el comando mkfile
				mkfile(valores[1], valores[2]);
				break;

			case "write":
				if (valores.length != 2) {
					System.out.println("Error: numero de parametros incorrecto");
					continue;
				}
				write(valores);
				break;
				
			case "dir":
				dir(valores);
				break;
			case "delete":
				delete(valores);
				break;
			case "close":
				close = true;
				break;
			case "clear":
				clear();
				break;
			default:
				System.out.println("Este comando no existe");
				break;
			}

		} while (!close);
	}

	public static void help(String help) {
		System.out.println("\n.●help-> Lista los comandos con una breve definiciónde lo que hacen."
				+ "\n.●cd-> Muestra el directorio actual.▪[..] -> Accede al directorio padre.▪[<nombreDirectorio>]-> Accede a un directorio dentrodel directorioactual.▪[<rutaAbsoluta]-> Accede a la ruta absoluta del sistema."
				+ "\n.●mkdir<nombre_directorio>-> Crea un directorio enla ruta actual."
				+ "\n.●info <nombre>-> Muestra la información del elemento.Indicando FileSystem,Parent, Root, Nº of elements, FreeSpace, TotalSpace y UsableSpace."
				+ "\n.●cat <nombreFichero>-> Muestra el contenido de unfichero."
				+ "\n.●top <numeroLineas> <nombreFichero>-> Muestra laslíneas especificadas deun fichero."
				+ "\n.●mkfile <nombreFichero> <texto>-> Crea un fichero con ese nombre y elcontenido de texto."
				+ "\n.●write <nombreFichero> <texto>-> Añade 'texto' alfinal del ficheroespecificado."
				+ "\n.●dir-> Lista los archivos o directorios de la rutaactual.▪[<nombreDirectorio>]-> Lista los archivos o directoriosdentro de esedirectorio.▪[<rutaAbsoluta]-> Lista los archivos o directoriosdentro de esa ruta."
				+ "\n.●delete <nombre>-> Borra el fichero, si es un directorioborra todo su contenidoy a si mismo.");
	}

	public static boolean cd(String[] valores) {
		boolean salida = true;
		if (valores.length == 1) {

			System.out.println(rutabase);
		}

		if (valores.length == 2) {
			// tenemos que ver qué ha metido
			if (valores[1].equals("..")) {
				// ruta padre - getParent()
				rutabase = rutabase.getParent();

			} else {
				Path nuevaruta = Paths.get(valores[1]);
				if (nuevaruta.isAbsolute()) {
					// decir que rutabase es nuevaruta
					rutabase = nuevaruta;
				} else {
					// resolve de rutabase+nuevaruta
					rutabase = rutabase.resolve(nuevaruta);
				}
			}
			System.out.println(rutabase);

		} else if (valores.length > 2) {
			System.out.println("Parámetros incorrectos");
			salida = false;
		}
		return salida;
	}

	public static boolean mkdir(String nombre) {

		boolean salida = true;
		File fichero = new File(rutabase.resolve(nombre).toString());

		if (!fichero.exists()) {
			salida = fichero.mkdir();
		} else {
			System.out.println("Este directorio ya existe");
			salida = false;
		}
		return salida;
	}

	public static void info(String[] valores1) {

		if (valores1.length == 1) {
			System.out.println("No has introducido ningun nombre");
		} else {
			String nombre = valores1[1];
			File fichero = new File(rutabase.resolve(nombre).toString());
			// File d = null;
			Path ruta = Paths.get(fichero.getPath());

			if (!fichero.exists()) {
				System.out.println("El fichero o directorio no existe");
			} else {
				System.out.println("Informacion del elemento:");
				System.out.println("Nombre: " + ruta.getFileName());
				System.out.println("Sistema creador de la ruta: " + ruta.getFileSystem());
				System.out.println("Ruta padre: " + ruta.getParent());
				System.out.println("Raiz de la ruta: " + ruta.getRoot());
				System.out.println("Numero de elementos de la ruta: " + ruta.getNameCount());
				System.out.println("Espacio libre en la ruta: " + ruta.toFile().getFreeSpace() + " bytes");
				System.out.println("Espacio total en la ruta: " + ruta.toFile().getTotalSpace() + " bytes");
				System.out.println("Espacio utilizable en la ruta: " + ruta.toFile().getUsableSpace() + " bytes");
			}
		}
	}

	public static void cat(String fileName) {

		try {
			File fichero = new File(rutabase.resolve(fileName).toString());
			BufferedReader reader = new BufferedReader(new FileReader(fichero.toString()));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			reader.close();
		} catch (IOException e) {
			System.out.println("Error al leer el archivo '" + fileName + "'");
		}
	}

	public static void top(int numLineas, String nombreFichero) throws IOException {

		File fichero = new File(rutabase.resolve(nombreFichero).toString());
		try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
			String line;
			int lineaActual = 0;
			while ((line = reader.readLine()) != null && lineaActual < numLineas) {
				System.out.println(line);
				lineaActual++;
			}
		}
	}

	public static boolean mkfile(String nombreFichero, String contenidoFichero) {
		
		try {
			File fichero = new File(rutabase.resolve(nombreFichero).toString());

			fichero.createNewFile();
			// Creamos un objeto de FileWriter
			FileWriter fileWriter = new FileWriter(fichero);

			// Creamos un objeto de BufferedWriter para escribir en el archivo
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Escribimos el contenido en el archivo
			bufferedWriter.write(contenidoFichero);

			// Cerramos el archivo
			bufferedWriter.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void write(String[] array) {
		// no consigo que recoja correctamente el numero de argumentos.
		
		if (array.length== 3) {
			
		}
		String contenido = "";
		String añadido = array[2];
		String fichero = array[1];
		File fichero_1 = new File(rutabase.resolve(fichero).toString());
	
			try {
				FileReader lectura = new FileReader(fichero_1);
				BufferedReader bfLectura = new BufferedReader(lectura);
				int i = 0;
				while ((i = bfLectura.read()) != -1) {
					char c = (char) i;
					contenido += c + "";
				}
				FileWriter escritura = new FileWriter(fichero_1);
				BufferedWriter bfEscritura = new BufferedWriter(escritura);
				bfEscritura.write(contenido + " " + añadido);
				bfEscritura.flush();
				bfEscritura.close();
				System.out.println("Completado con exito");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	public static void dir(String[] array) {

		if (array.length == 1) {

			File archivo = new File(rutabase.toString());
			logicaDir(archivo);
		} else if (array.length == 2) {

			Path nuevaruta = Paths.get(array[1]);
			if (nuevaruta.isAbsolute()) {

				File nuevoArchivo = nuevaruta.toFile();
				logicaDir(nuevoArchivo);

			} else {

				File nombreRuta = new File(rutabase.resolve(array[1]).toString());
				logicaDir(nombreRuta);

			}

		}

	}

	public static void logicaDir(File archivo) {

		File[] archivos = archivo.listFiles();

		if (archivos != null) {

			System.out.println("Ruta: " + rutabase);

			for (File i : archivos) {
				System.out.println("Nombre: " + i.getName());
				System.out.println("Tamaño: " + i.length() + "Bytes");
				System.out.println("Es un directorio: " + i.isDirectory());
				System.out.println("");
			}
		} else {
			System.out.println("La ruta no es un directorio");
		}
	}

	public static void delete(String[] args) {

		if (args.length != 2) {

			System.out.println("Error:el comando delete requiere dos parametros: el nombre del comando y el archivo/directorio a borrar");
		} else {
			File file = new File(rutabase.resolve(args[1]).toString());
			if (file.exists()) {

				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
				System.out.println("Fichero eliminado con exito");

			} else {
				System.out.println("El archivo o directorio " + file + " no existe ");
			}
			
		}

	}

	public static void deleteDirectory(File nombre) {
		if (nombre.exists()) {

			File[] directorio = nombre.listFiles();
			if (directorio != null) {

				for (File i : directorio) {

					if (i.isDirectory()) {

						deleteDirectory(i);
					} else {

						i.delete();
					}
				}
			}
			nombre.delete();
		}

	}

	public static void clear() {
		String clear = "\n";
		for (int i = 0; i < 1000; i++) {
			System.out.println(clear);
		}
	}

}
