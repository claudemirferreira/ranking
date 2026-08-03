package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record NotaLoteRequest(@NotEmpty List<@Valid NotaRequest> itens) {
}
