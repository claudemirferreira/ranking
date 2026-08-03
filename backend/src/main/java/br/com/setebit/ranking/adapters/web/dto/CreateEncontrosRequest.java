package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.Min;

public record CreateEncontrosRequest(@Min(1) int quantidade) {
}
