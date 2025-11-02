package gatoscopio.back.service;

import java.util.List;
import gatoscopio.back.dto.EncuestaDetalleResponse;
import gatoscopio.back.dto.RespuestaUpsertRequest;

public interface ServiceEncuesta {
    EncuestaDetalleResponse getDetalle(Integer encuestaId);
    int upsertRespuestas(Integer encuestaId, List<RespuestaUpsertRequest> items, Integer userId);
    void updateEstado(Integer encuestaId, String estado, Integer userId);
    org.springframework.data.domain.Page<gatoscopio.back.model.Encuesta> list(String estado, String pacienteCodigo, org.springframework.data.domain.Pageable pageable);
}
