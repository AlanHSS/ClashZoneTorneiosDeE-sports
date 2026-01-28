package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioStatusScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("clashzone/admin/")
@RequiredArgsConstructor
public class TestsController {

    private final TorneioStatusScheduler scheduler;

    @PostMapping("executar-atualizacao-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> executarManualmente() {
        scheduler.atualizarStatusTorneiosAutomaticamente();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mensagem", "Atualização de status executada manualmente");
        response.put("Timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}
