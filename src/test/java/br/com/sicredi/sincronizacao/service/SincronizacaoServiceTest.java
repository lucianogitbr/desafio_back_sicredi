package br.com.sicredi.sincronizacao.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.sicredi.sincronizacao.dto.ContaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class SincronizacaoServiceTest {

    @Mock
    private BancoCentralService bancoCentralService;

    @InjectMocks
    private SincronizacaoService sincronizacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void syncAccounts_SuccessfulUpdate() throws IOException {
        String filePath = "src/test/resources/CONTAS.csv";
        when(bancoCentralService.atualizaConta(any(ContaDTO.class))).thenReturn(true);

        sincronizacaoService.syncAccounts(filePath);

        Path outputPath = Path.of("../OUTPUT.csv");
        assertTrue(Files.exists(outputPath));
        List<String> outputLines = Files.readAllLines(outputPath);
        assertTrue(outputLines.contains("9444,21382-2,880.2,true"));

        Path errorPath = Path.of("../ERROS.csv");
        assertTrue(Files.exists(errorPath));
    }

    @Test
    void isValidLine_ValidData_ReturnsTrue() {
        assertTrue(sincronizacaoService.isValidLine("1234,5678,90.0"));
    }

    @Test
    void isValidLine_MissingData_ReturnsFalse() {
        assertFalse(sincronizacaoService.isValidLine("1234,5678"));
    }

    @Test
    void isValidLine_BlankAgency_ReturnsFalse() {
        assertFalse(sincronizacaoService.isValidLine(",5678,90.0"));
    }

    @Test
    void isValidLine_BlankAccount_ReturnsFalse() {
        assertFalse(sincronizacaoService.isValidLine("1234,,90.0"));
    }

}