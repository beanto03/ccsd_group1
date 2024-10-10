// ProductController.java
package com.ccsd.biddingSystem.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add a new product
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // Update an existing product
    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable String productId, @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    // View all products
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // View products by seller ID
    @GetMapping("/seller/{sellerId}")
    public List<Product> getProductsBySeller(@PathVariable String sellerId) {
        return productService.getProductsBySeller(sellerId);
    }

    // Get a specific product by its ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
