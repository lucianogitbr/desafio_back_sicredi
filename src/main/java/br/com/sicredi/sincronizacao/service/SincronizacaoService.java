package br.com.sicredi.sincronizacao.service;

import br.com.sicredi.sincronizacao.dto.ContaAtualizadaDTO;
import br.com.sicredi.sincronizacao.dto.ContaDTO;
import br.com.sicredi.sincronizacao.timer.MeasuredExecutionTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SincronizacaoService {

    @NonNull
    private BancoCentralService bancoCentralService;

    /**
     * Sincroniza contas a partir de um arquivo especificado.
     *
     * @param filePath o caminho do arquivo contendo os dados das contas.
     */
    @MeasuredExecutionTime
    public void syncAccounts(String filePath) {
        var contasAtualizadas = new ArrayList<ContaAtualizadaDTO>();
        var linhasComFalha = new ArrayList<ContaDTO>();

        try {
            Files.readAllLines(Path.of(filePath))
                .stream()
                .skip(1)
                .forEach(line -> {
                    if (!isValidLine(line)) {
                        linhasComFalha.add(toContaDto(line));
                        return;
                    }

                    ContaDTO contaRecebida = toContaDto(line);
                    boolean statusAtualizacao = bancoCentralService.atualizaConta(contaRecebida);
                    contasAtualizadas.add(toContaAtualizadaDto(contaRecebida, statusAtualizacao));
                });

            exportarContasAtualizadasParaCSV(contasAtualizadas, "../OUTPUT.csv");
            exportLinhasComFalhaParaCsv(linhasComFalha, "../ERROS.csv");
        } catch (NoSuchFileException | SecurityException e) {
            log.error("Erro ao abrir e ler o arquivo de contas, mensagem: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Erro ao exportar contas atualizadas e/ou contas com falha, mensagem: {}", e.getMessage());
        }
    }

    /**
     * Exporta uma lista de contas atualizadas para um arquivo CSV.
     *
     * @param contasAtualizadas a lista de contas atualizadas.
     * @param caminhoArquivoSaida o caminho do arquivo de saída.
     * @throws IOException se ocorrer um erro durante a exportação.
     */
    private void exportarContasAtualizadasParaCSV(List<ContaAtualizadaDTO> contasAtualizadas, String caminhoArquivoSaida) throws IOException {
        Path caminho = Path.of(caminhoArquivoSaida);

        List<String> linhas = new ArrayList<>();

        linhas.add("agencia,conta,saldo,status");

        linhas.addAll(contasAtualizadas.stream()
                .map(ContaAtualizadaDTO::toCsvLine)
                .toList());

        Files.write(caminho, linhas, StandardOpenOption.CREATE);
    }

    /**
     * Exporta uma lista de contas com erro para um arquivo CSV.
     *
     * @param contasComErro a lista de contas com erro.
     * @param caminhoArquivo o caminho do arquivo de saída.
     * @throws IOException se ocorrer um erro durante a exportação.
     */
    private void exportLinhasComFalhaParaCsv(List<ContaDTO> contasComErro, String caminhoArquivo) throws IOException {
        Path caminho = Path.of(caminhoArquivo);
        List<String> linhas = new ArrayList<>();

        linhas.add("agencia,conta,saldo,erro(s)");

        contasComErro.stream().map(conta -> {
            String line = conta.toCsvLine() + ",[";
            if(conta.agencia().isBlank())
                line += "Agencia vazia;";

            if(conta.conta().isBlank())
                line += "Conta vazia;";

            if(conta.saldo() == null)
                line += "Saldo vazio;";

            return line + "]";
        }).forEach(linhas::add);

        Files.write(caminho, linhas, StandardOpenOption.CREATE);
    }

    /**
     * Converte um objeto ContaDTO e o status de atualização para um objeto ContaAtualizadaDTO.
     *
     * @param conta a conta recebida.
     * @param status o status da atualização.
     * @return o objeto ContaAtualizadaDTO correspondente.
     */
    private ContaAtualizadaDTO toContaAtualizadaDto(ContaDTO conta, Boolean status) {
        return new ContaAtualizadaDTO(conta.agencia(), conta.conta(), conta.saldo(), status);
    }

    /**
     * Converte uma linha de texto do arquivo para um objeto ContaDTO.
     *
     * @param line a linha a ser convertida.
     * @return o objeto ContaDTO correspondente.
     */
    private ContaDTO toContaDto(String line) {
        String[] data = line.split(",");
        return new ContaDTO(data[0], data[1], Double.parseDouble(data[2]));
    }

    /**
     * Verifica se uma linha do arquivo é válida.
     *
     * @param line a linha a ser verificada.
     * @return true se a linha for válida, false caso contrário.
     */
    public boolean isValidLine(String line) {
        String[] data = line.split(",");
        return data.length == 3 && !data[0].isBlank() && !data[1].isBlank() && !data[2].isBlank();
    }
}
