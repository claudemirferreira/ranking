package br.com.setebit.ranking.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TurmaController.class)
class TurmaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ManageTurmaUseCase manageTurmaUseCase;

	@Test
	void shouldCreateTurma() throws Exception {
		when(manageTurmaUseCase.create(eq("Juvenis"), any()))
				.thenReturn(new TurmaResponse(1L, "Juvenis", "desc", true, Instant.now(), Instant.now()));

		mockMvc.perform(post("/api/turmas")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nome":"Juvenis","descricao":"desc"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Juvenis"));
	}
}
