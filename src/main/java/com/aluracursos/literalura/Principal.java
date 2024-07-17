package com.aluracursos.literalura;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Idioma;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("Bienvenido al Catálogo de Libros");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un determinado año");
            System.out.println("5 - Listar libros por idiomas");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listLivingAuthorsInYear();
                    break;
                case 5:
                    listBooksByLanguages();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("-------------------------------");
                    System.out.println("Opción no válida, intente de nuevo.");
                    System.out.println("-------------------------------");
            }
        } while (option != 0);

        scanner.close();
    }

    private void searchBookByTitle() {
        System.out.println("-------------------------------");
        System.out.println("Función: Buscar libro por título");
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        String url = URL_BASE + titulo.replace(" ", "+");
        String jsonResponse = consumoApi.obtenerDatos(url);

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<DatosLibro> datosLibros = mapper.readValue(
                    mapper.readTree(jsonResponse).get("results").toString(),
                    new TypeReference<List<DatosLibro>>() {}
            );

            List<Libro> libros = new ArrayList<>();
            for (DatosLibro datosLibro : datosLibros) {
                Libro libro = new Libro(datosLibro);
                libros.add(libro);
                libroRepository.save(libro);
                System.out.println("Libro guardado: " + libro);
            }

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros con el título especificado.");
            } else {
                System.out.println("Libros descargados y guardados en la base de datos:");
                for (Libro libro : libros) {
                    System.out.println(libro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al procesar la respuesta de la API.");
        }
        System.out.println("-------------------------------");
    }

    private void listRegisteredBooks() {
        System.out.println("-------------------------------");
        System.out.println("Función: Listar libros registrados");
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        }
        System.out.println("-------------------------------");
    }

    private void listRegisteredAuthors() {
        System.out.println("-------------------------------");
        System.out.println("Función: Listar autores registrados");
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            for (Autor autor : autores) {
                System.out.println("Nombre: " + autor.getNombre() + ", Nacimiento: " + autor.getNacimiento() + ", Deceso: " + (autor.getDeceso() != null ? autor.getDeceso() : "N/A"));
            }
        }
        System.out.println("-------------------------------");
    }

    private void listLivingAuthorsInYear() {
        System.out.println("-------------------------------");
        System.out.println("Función: Listar autores vivos en un determinado año");
        System.out.print("Ingrese el año: ");
        int year = scanner.nextInt();
        List<Autor> autores = autorRepository.findAll();
        List<Autor> autoresVivos = new ArrayList<>();

        for (Autor autor : autores) {
            if (autor.getNacimiento().getYear() <= year && (autor.getDeceso() == null || autor.getDeceso().getYear() >= year)) {
                autoresVivos.add(autor);
            }
        }

        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos en el año especificado.");
        } else {
            for (Autor autor : autoresVivos) {
                System.out.println("Nombre: " + autor.getNombre() + ", Nacimiento: " + autor.getNacimiento() + ", Deceso: " + (autor.getDeceso() != null ? autor.getDeceso() : "N/A"));
            }
        }
        System.out.println("-------------------------------");
    }

    private void listBooksByLanguages() {
        System.out.println("-------------------------------");
        System.out.println("Función: Listar libros por idiomas");
        System.out.print("Ingrese el idioma (es, en, fr, pt): ");
        String idiomaStr = scanner.nextLine();
        Idioma idioma = Idioma.fromString(idiomaStr);

        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma especificado.");
        } else {
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        }
        System.out.println("-------------------------------");
    }
}
