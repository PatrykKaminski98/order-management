package pl.ordermanagement.adapter.in.rest.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ordermanagement.adapter.in.rest.mapper.ProductMapper;
import pl.ordermanagement.adapter.in.rest.model.Product;
import pl.ordermanagement.application.product.api.port.in.GetAllProductsQuery;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ProductRestController {
    private final GetAllProductsQuery getAllProductsQuery;
    private final ProductMapper mapper;

    @GetMapping(path = "/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<pl.ordermanagement.application.product.domain.model.Product> products = getAllProductsQuery.execute();
        return ResponseEntity.ok(mapper.map(products));
    }
}
