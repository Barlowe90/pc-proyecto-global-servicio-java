package um.practicascontinuas.benchmark.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/** Prueba los endpoints con el contexto completo: controlador, servicio, MergeSort y errores. */
@SpringBootTest
@AutoConfigureMockMvc
class BenchmarkControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  private ResultActions postBenchmark(String json) throws Exception {
    return mockMvc.perform(
        post("/api/v1/benchmark").contentType(MediaType.APPLICATION_JSON).content(json));
  }

  @Test
  void benchmarkDeMergesortDevuelveElContratoComun() throws Exception {
    postBenchmark("{\"algorithm\":\"mergesort\",\"size\":10000}")
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.language").value("java"))
        .andExpect(jsonPath("$.algorithm").value("mergesort"))
        .andExpect(jsonPath("$.size").value(10000))
        .andExpect(jsonPath("$.runs").value(5))
        .andExpect(jsonPath("$.timeMs").value(greaterThanOrEqualTo(0.0)));
  }

  @Test
  void elNombreDelAlgoritmoNoDistingueMayusculasNiEspacios() throws Exception {
    postBenchmark("{\"algorithm\":\" MergeSort \",\"size\":100}")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.algorithm").value("mergesort"));
  }

  @Test
  void algoritmoNoSoportadoDevuelve404() throws Exception {
    postBenchmark("{\"algorithm\":\"bogosort\",\"size\":100}")
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Algoritmo no soportado: bogosort"));
  }

  @Test
  void algoritmoVacioOAusenteDevuelve400() throws Exception {
    postBenchmark("{\"algorithm\":\" \",\"size\":100}")
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("El campo 'algorithm' es obligatorio"));
    postBenchmark("{\"size\":100}").andExpect(status().isBadRequest());
  }

  @Test
  void tamanoFueraDeRangoOAusenteDevuelve400() throws Exception {
    postBenchmark("{\"algorithm\":\"mergesort\",\"size\":0}")
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
    postBenchmark("{\"algorithm\":\"mergesort\",\"size\":1000001}")
        .andExpect(status().isBadRequest());
    postBenchmark("{\"algorithm\":\"mergesort\"}").andExpect(status().isBadRequest());
  }

  @Test
  void tamanoMaximoPermitidoDevuelve200() throws Exception {
    postBenchmark("{\"algorithm\":\"mergesort\",\"size\":1000000}")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size").value(1000000));
  }

  @Test
  void cuerpoInvalidoDevuelve400() throws Exception {
    postBenchmark("{no es json")
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Cuerpo de la petición inválido"));
    postBenchmark("{\"algorithm\":\"mergesort\",\"size\":\"abc\"}")
        .andExpect(status().isBadRequest());
    postBenchmark("").andExpect(status().isBadRequest());
  }

  @Test
  void listaLosAlgoritmosDisponibles() throws Exception {
    mockMvc
        .perform(get("/api/v1/algorithms"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.algorithms").isArray())
        .andExpect(jsonPath("$.algorithms", hasItem("mergesort")));
  }
}
