package br.com.sicredi.sincronizacao.dto;

public record ContaDTO(String agencia, String conta, Double saldo) {
    public String toCsvLine() {
        return agencia + "," + conta + "," + saldo;
    }
}
