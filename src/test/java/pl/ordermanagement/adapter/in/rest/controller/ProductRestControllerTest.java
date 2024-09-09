package pl.ordermanagement.adapter.in.rest.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.ordermanagement.adapter.in.rest.mapper.ProductMapper;
import pl.ordermanagement.adapter.in.rest.mapper.generated.ProductMapperImpl;
import pl.ordermanagement.application.product.api.port.in.GetAllProductsQuery;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private static final String PRODUCT_CONTROLLER_API_URL = "/api/product";
    @Mock
    private  GetAllProductsQuery getAllProductsQuery;
    private final ProductMapper mapper = new ProductMapperImpl();

    private ProductRestController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new ProductRestController(getAllProductsQuery, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get(PRODUCT_CONTROLLER_API_URL))
                .andExpect(status().isOk());
        verify(getAllProductsQuery).execute();
    }
}
