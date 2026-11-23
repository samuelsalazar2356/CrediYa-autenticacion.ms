package co.com.crediya.api.dto;

import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.SolicitudRevisionDetalle;

import java.util.List;

/**
 * Contrato de respuesta para la consulta paginada de solicitudes de revision.
 */
public class SolicitudListadoPaginadoDTO {

    private List<SolicitudItemRevisionDTO> contenido;
    private int pagina;
    private int tamanoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean esPrimera;
    private boolean esUltima;

    public SolicitudListadoPaginadoDTO() {
    }

    public SolicitudListadoPaginadoDTO(List<SolicitudItemRevisionDTO> contenido, int pagina,
                                      int tamanoPagina, long totalElementos, int totalPaginas,
                                      boolean esPrimera, boolean esUltima) {
        this.contenido = contenido;
        this.pagina = pagina;
        this.tamanoPagina = tamanoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
        this.esPrimera = esPrimera;
        this.esUltima = esUltima;
    }

    public static SolicitudListadoPaginadoDTO desdeDominio(Pagina<SolicitudRevisionDetalle> paginaDominio) {
        List<SolicitudItemRevisionDTO> items = paginaDominio.getContenido().stream()
                .map(SolicitudItemRevisionDTO::desdeDominio)
                .toList();

        return new SolicitudListadoPaginadoDTO(
                items,
                paginaDominio.getNumeroPagina(),
                paginaDominio.getTamanoPagina(),
                paginaDominio.getTotalElementos(),
                paginaDominio.getTotalPaginas(),
                paginaDominio.isEsPrimera(),
                paginaDominio.isEsUltima()
        );
    }

    public List<SolicitudItemRevisionDTO> getContenido() {
        return contenido;
    }

    public void setContenido(List<SolicitudItemRevisionDTO> contenido) {
        this.contenido = contenido;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTamanoPagina() {
        return tamanoPagina;
    }

    public void setTamanoPagina(int tamanoPagina) {
        this.tamanoPagina = tamanoPagina;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(long totalElementos) {
        this.totalElementos = totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public boolean isEsPrimera() {
        return esPrimera;
    }

    public void setEsPrimera(boolean esPrimera) {
        this.esPrimera = esPrimera;
    }

    public boolean isEsUltima() {
        return esUltima;
    }

    public void setEsUltima(boolean esUltima) {
        this.esUltima = esUltima;
    }
}
