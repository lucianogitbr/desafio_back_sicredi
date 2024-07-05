package br.com.sicredi.sincronizacao.dto;

public record ContaAtualizadaDTO(String agencia, String conta, Double saldo, Boolean status) {
    public String toCsvLine() {
        return agencia + "," + conta + "," + saldo + "," + status;
    }
}
