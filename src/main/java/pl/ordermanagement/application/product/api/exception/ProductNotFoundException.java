package pl.ordermanagement.application.product.api.exception;

import static java.lang.String.format;

import java.util.NoSuchElementException;

import pl.ordermanagement.application.shared.datatype.ProductIdentifier;


public class ProductNotFoundException extends NoSuchElementException {
    private static final String PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE = "Product with identifier: %s has not been found";
    public ProductNotFoundException(ProductIdentifier productIdentifier) {
        super(format(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE, productIdentifier.getValue()));}
}
