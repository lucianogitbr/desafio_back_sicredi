package br.com.sicredi.sincronizacao;

import br.com.sicredi.sincronizacao.service.SincronizacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class SincronizadorBacen {

	public static void main(String[] args) {
		SpringApplication.run(SincronizadorBacen.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(SincronizacaoService sincronizacaoService) {
		return args -> {
			if (args.length > 0)
				sincronizacaoService.syncAccounts(args[0]);
			else
				log.error("Por favor, forneça o caminho do arquivo CSV como argumento.");
		};
	}
}
