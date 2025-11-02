package gatoscopio.back.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gatoscopio.back.dto.EncuestaDetalleResponse;
import gatoscopio.back.dto.RespuestaUpsertRequest;
import gatoscopio.back.model.Encuesta;
import gatoscopio.back.model.Pregunta;
import gatoscopio.back.model.Respuesta;
import gatoscopio.back.model.ValorValidoPregunta;
import gatoscopio.back.repository.EncuestaRepository;
import gatoscopio.back.repository.PreguntaRepository;
import gatoscopio.back.repository.RespuestaRepository;
import gatoscopio.back.repository.ValorValidoPreguntaRepository;
import gatoscopio.back.service.ServiceEncuesta;

@Service
public class ServiceEncuestaImpl implements ServiceEncuesta {

    @Autowired private EncuestaRepository encuestaRepository;
    @Autowired private RespuestaRepository respuestaRepository;
    @Autowired private PreguntaRepository preguntaRepository;
    @Autowired private ValorValidoPreguntaRepository valorValidoRepository;

    @Override
    @Transactional(readOnly = true)
    public EncuestaDetalleResponse getDetalle(Integer encuestaId) {
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        var resp = new EncuestaDetalleResponse();
        resp.setId(e.getId());
        resp.setEstado(e.getEstado());
        resp.setPacienteCodigo(e.getPacienteCodigo());
        resp.setCreatedAt(e.getCreatedAt());
        var respuestas = new ArrayList<EncuestaDetalleResponse.ItemRespuesta>();
        for (Respuesta r : respuestaRepository.findByEncuestaId(encuestaId)) {
            respuestas.add(new EncuestaDetalleResponse.ItemRespuesta(r.getPreguntaId(), r.getValor()));
        }
        resp.setRespuestas(respuestas);
        return resp;
    }

    @Override
    @Transactional
    public int upsertRespuestas(Integer encuestaId, List<RespuestaUpsertRequest> items) {
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("lista de respuestas requerida");
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        if (e.getEstado() != null && "Completo".equalsIgnoreCase(e.getEstado())) {
            throw new IllegalStateException("encuesta completa no se puede editar");
        }

        int updated = 0;
        for (RespuestaUpsertRequest it : items) {
            Integer pid = it.getPreguntaId();
            String valor = it.getValor();
            if (pid == null) throw new IllegalArgumentException("preguntaId requerido");
            Pregunta p = preguntaRepository.findById(pid)
                    .orElseThrow(() -> new java.util.NoSuchElementException("pregunta no existe: " + pid));

            // Validar valor con banco de válidos (si existen)
            List<ValorValidoPregunta> validos = valorValidoRepository.findByPreguntaId(pid);
            if (!validos.isEmpty()) {
                boolean ok = validos.stream().map(ValorValidoPregunta::getValor).anyMatch(v -> v.equalsIgnoreCase(valor));
                if (!ok) throw new IllegalArgumentException("valor inválido para la pregunta: " + pid);
            }
            // Validación simple por tipo_dato
            if (p.getTipoDato() != null) {
                String t = p.getTipoDato().toLowerCase();
                switch (t) {
                    case "numero":
                        try { new BigDecimal(valor); } catch (Exception ex) { throw new IllegalArgumentException("valor no numérico"); }
                        break;
                    case "fecha":
                        try { LocalDate.parse(valor); } catch (Exception ex) { throw new IllegalArgumentException("fecha inválida (ISO yyyy-MM-dd)"); }
                        break;
                    case "bool":
                    case "boolean":
                        Set<String> allowed = Set.of("true","false","si","no","0","1");
                        if (!allowed.contains(valor.toLowerCase())) throw new IllegalArgumentException("boolean inválido");
                        break;
                    default: // texto u otros
                        break;
                }
            }

            Respuesta r = respuestaRepository.findByEncuestaIdAndPreguntaId(encuestaId, pid).orElse(null);
            if (r == null) {
                r = new Respuesta();
                r.setEncuestaId(encuestaId);
                r.setPreguntaId(pid);
            }
            r.setValor(valor);
            respuestaRepository.save(r);
            updated++;
        }
        return updated;
    }

    @Override
    @Transactional
    public void updateEstado(Integer encuestaId, String estado) {
        if (estado == null || estado.isBlank()) throw new IllegalArgumentException("estado requerido");
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        e.setEstado(estado.trim());
        encuestaRepository.save(e);
    }
}

