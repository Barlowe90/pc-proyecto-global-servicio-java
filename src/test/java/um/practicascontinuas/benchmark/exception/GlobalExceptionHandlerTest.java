package um.practicascontinuas.benchmark.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

  /** Controlador mínimo que lanza cada excepción para probar el handler aislado. */
  @RestController
  static class ThrowingController {

    @PostMapping("/not-found")
    void notFound() {
      throw new AlgorithmNotFoundException("bogosort");
    }

    @PostMapping("/invalid")
    void invalid() {
      throw new InvalidRequestException("size inválido");
    }

    @PostMapping("/boom")
    void boom() {
      throw new IllegalStateException("secreto interno");
    }

    @PostMapping("/json")
    void json(@RequestBody Map<String, Object> body) {}
  }

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(new ThrowingController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void algoritmoNoEncontradoDevuelve404() throws Exception {
    mockMvc
        .perform(post("/not-found"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Algoritmo no soportado: bogosort"));
  }

  @Test
  void peticionInvalidaDevuelve400() throws Exception {
    mockMvc
        .perform(post("/invalid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("size inválido"));
  }

  @Test
  void jsonMalFormadoDevuelve400() throws Exception {
    mockMvc
        .perform(post("/json").contentType(MediaType.APPLICATION_JSON).content("{no es json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void errorInesperadoDevuelve500SinFiltrarDetalles() throws Exception {
    mockMvc
        .perform(post("/boom"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Error interno del servidor"));
  }
}
