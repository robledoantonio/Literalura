package com.aluracursos.literalura.model;

public enum Idioma {
    ESPANOL("es"),
    INGLES("en"),
    FRANCES("fr"),
    PORTUGUES("pt");

    private String idiomaCorto;

    Idioma(String idiomaCorto) {
        this.idiomaCorto = idiomaCorto;
    }

    public String getIdiomaCorto() {
        return idiomaCorto;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaCorto.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma no válido: " + text);
    }
}
