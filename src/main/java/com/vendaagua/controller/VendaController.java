package com.vendaagua.controller;

import com.vendaagua.dto.VendaDtos.RegistrarFechamentoRequest;
import com.vendaagua.dto.VendaResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.model.Venda;
import com.vendaagua.service.UsuarioService;
import com.vendaagua.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;
    private final UsuarioService usuarioService;

    /** Apenas admin registra o fechamento de caixa do dia (moedas + notas + pix). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VendaResponse registrar(@Valid @RequestBody RegistrarFechamentoRequest request, Authentication authentication) {
        Usuario admin = usuarioService.buscarPorEmail(authentication.getName());
        Venda venda = vendaService.registrarFechamento(admin, request);
        return VendaResponse.from(venda);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VendaResponse atualizar(@PathVariable Long id, @Valid @RequestBody RegistrarFechamentoRequest request) {
        return VendaResponse.from(vendaService.atualizarFechamento(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        vendaService.excluir(id);
    }

    /** Todos podem ver o historico de fechamentos (semana/mes filtrados no frontend). */
    @GetMapping
    public List<VendaResponse> listar(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim) {

        List<Venda> vendas = (inicio != null && fim != null)
                ? vendaService.listarPorPeriodo(inicio, fim)
                : vendaService.listarTodas();

        return vendas.stream().map(VendaResponse::from).toList();
    }
}
