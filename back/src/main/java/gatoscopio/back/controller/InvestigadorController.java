package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import gatoscopio.back.service.impl.ServiceInvestigadorImpl;


@RestController
public class InvestigadorController {
    @Autowired
    private ServiceInvestigadorImpl service;
}
