package com.vendaagua.model;

public enum StatusPresenca {
    /** Participou do dia de venda. */
    PRESENTE,
    /** Faltou e o admin aceitou a justificativa (taxa reduzida). */
    JUSTIFICADO,
    /** Faltou sem justificativa aceita pelo admin (taxa cheia). */
    AUSENTE
}
