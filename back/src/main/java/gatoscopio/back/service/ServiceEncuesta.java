package gatoscopio.back.service;

import java.util.List;
import gatoscopio.back.dto.EncuestaDetalleResponse;
import gatoscopio.back.dto.RespuestaUpsertRequest;

public interface ServiceEncuesta {
    EncuestaDetalleResponse getDetalle(Integer encuestaId);
    int upsertRespuestas(Integer encuestaId, List<RespuestaUpsertRequest> items);
    void updateEstado(Integer encuestaId, String estado);
}

