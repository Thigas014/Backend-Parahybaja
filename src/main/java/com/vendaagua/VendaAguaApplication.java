package com.vendaagua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class VendaAguaApplication {
    public static void main(String[] args) {
        // O servidor (Render) roda em UTC por padrao. Sem isso, LocalDate.now()
        // e LocalDateTime.now() usam o horario do servidor, nao o de Brasilia —
        // causando datas erradas (ex: virando o dia de madrugada) em qualquer
        // lancamento feito a noite.
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(VendaAguaApplication.class, args);
    }
}
