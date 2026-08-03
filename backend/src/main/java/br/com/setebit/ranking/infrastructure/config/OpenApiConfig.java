package br.com.setebit.ranking.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI rankingOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Ranking Salinha API")
						.description("API de pontuação da classe dos Juvenis — salinha da Igreja Adventista")
						.version("0.0.1"));
	}
}
