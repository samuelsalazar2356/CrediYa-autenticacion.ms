package co.com.crediya.model.comun;

import java.util.Collections;
import java.util.List;

/**
 * Contenedor inmutable de paginacion agnostico a frameworks externos.
 */
public class Pagina<T> {

    private final List<T> contenido;
    private final int numeroPagina;
    private final int tamanoPagina;
    private final long totalElementos;
    private final int totalPaginas;

    public Pagina(List<T> contenido, int numeroPagina, int tamanoPagina, long totalElementos) {
        this.contenido = contenido != null ? Collections.unmodifiableList(contenido) : Collections.emptyList();
        this.numeroPagina = numeroPagina;
        this.tamanoPagina = tamanoPagina > 0 ? tamanoPagina : 10;
        this.totalElementos = totalElementos;
        this.totalPaginas = (int) Math.ceil((double) totalElementos / this.tamanoPagina);
    }

    public List<T> getContenido() {
        return contenido;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public int getTamanoPagina() {
        return tamanoPagina;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public boolean isEsPrimera() {
        return numeroPagina == 0;
    }

    public boolean isEsUltima() {
        return numeroPagina >= totalPaginas - 1;
    }
}
