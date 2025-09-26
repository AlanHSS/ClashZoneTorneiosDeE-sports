package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.Torneio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class TorneioController {

    public String criartorneio(@RequestBody Torneio torneio){
        return "torneio criado";
    }

}
