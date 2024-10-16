// ProductController.java
package com.ccsd.biddingSystem.ProductSeller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") String productString,
            @RequestPart("images") MultipartFile[] images,
            HttpServletRequest request) {

        ObjectMapper objectMapper = new ObjectMapper();
        Product product;

        try {
            product = objectMapper.readValue(productString, Product.class);
            // For testing, using predefined sellerId. Later, replace with session-based sellerId.
            product.setSellerId("12345"); //TODO: Replace with session-based sellerId after login is implemented.
        } catch (Exception e) {
            logger.error("Error parsing product JSON", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Log the received sellerId for debugging purposes
        logger.info("Received product with sellerId: " + product.getSellerId()); // Debugging log

        try {
            Product createdProduct = productService.addProductWithImages(product, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            logger.error("Error adding product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable String sellerId) {
        List<Product> products = productService.getProductsBySeller(sellerId);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        Optional<Product> product = productService.getProductById(productId);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable String productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("startingBid") double startingBid,
            @RequestParam("sellerId") String sellerId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) { // Make imageFile optional
        try {
            // Create a new Product object with the updated details
            Product updatedProduct = new Product();
            updatedProduct.setProductId(productId); // Assuming you have an ID field in your Product class
            updatedProduct.setName(name);
            updatedProduct.setDescription(description);
            updatedProduct.setStartingBid(startingBid);
            updatedProduct.setSellerId(sellerId); // Replace with session-based sellerId later

            // Log the received sellerId and other details for debugging
            logger.info("Updating product with ID: " + productId + ", sellerId: " + updatedProduct.getSellerId());

            // Call your service method to update the product
            Product savedProduct = productService.updateProduct(productId, updatedProduct, imageFile); // You might need to adjust this method signature
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Error updating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product: " + e.getMessage());
        }
    }
}
