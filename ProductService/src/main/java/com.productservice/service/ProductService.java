package com.productservice.service;

import com.productservice.dto.ProductAvailabilityResponse;
import com.productservice.exception.ResourceNotFoundException;
import com.productservice.model.Product;
import com.productservice.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public Product getProduct(Integer id){
        Product byId = productRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Resource not found with ID : " + id));
        return byId;
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepo.save(product);
        return getProduct(savedProduct.getProductId());
    }

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Product updateProduct(Integer id, Product updateProduct) {

        Product existingProduct = getProduct(id);

        existingProduct.setProductName(
                updateProduct.getProductName() == null
                        ? existingProduct.getProductName()
                        : updateProduct.getProductName());

        existingProduct.setAvailableQuantity(
                updateProduct.getAvailableQuantity() <= 0
                        ? existingProduct.getAvailableQuantity()
                        : updateProduct.getAvailableQuantity());

        existingProduct.setDescription(
                updateProduct.getDescription() == null
                        ? existingProduct.getDescription()
                        : updateProduct.getDescription());

        existingProduct.setPrice(
                updateProduct.getPrice() == null
                        ? existingProduct.getPrice()
                        : updateProduct.getPrice());

        return productRepo.save(existingProduct);
    }

    public String deleteProduct(Integer id) {
        productRepo.deleteById(id);
        return "Product deleted with ID : " + id;
    }

    public ProductAvailabilityResponse checkAvailability(Integer productId, Integer requestedQty){

        Product product = getProduct(productId);

        ProductAvailabilityResponse response = new ProductAvailabilityResponse();

        response.setProductId(productId);
        response.setQuantity(product.getAvailableQuantity());
        response.setAvailable(product.getAvailableQuantity() >= requestedQty);

        return response;
    }

    public String updateAvailabilityAfterOrder(Integer id, Integer quantity) {

        Product existingProduct = getProduct(id);
        existingProduct.setAvailableQuantity(existingProduct.getAvailableQuantity()-quantity);
        productRepo.save(existingProduct);
        return "Available quantity updated post order creation";
    }
}
