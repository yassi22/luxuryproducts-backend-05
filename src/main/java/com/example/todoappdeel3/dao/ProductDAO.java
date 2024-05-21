package com.example.todoappdeel3.dao;


import com.example.todoappdeel3.dto.ProductDTO;
import com.example.todoappdeel3.models.Category;
import com.example.todoappdeel3.models.Options;
import com.example.todoappdeel3.models.Product;
import com.example.todoappdeel3.models.ProductVariant;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OptionsRepository optionsRepository;



    public ProductDAO(ProductRepository repository, CategoryRepository category, ProductVariantRepository productVariantRepository, OptionsRepository optionsRepository) {
        this.productRepository = repository;
        this.categoryRepository = category;
        this.productVariantRepository = productVariantRepository;
        this.optionsRepository = optionsRepository;

    }

    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public List<Product> getProducts(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }

    public List<Product> getAllProductsByCategory(long id){
        Optional<List<Product>> products =this.productRepository.findByCategoryId(id);

        if (products.get().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }

        return products.get();
    }

    public Product getProduct(long id){
        Optional<Product> product = this.productRepository.findById(id);

        if(product.isPresent()) {
            return product.get();
        }  else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "De gevraagde product is niet gevonden"
            );
        }

    }

    public List<Product> getProductVariants(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }

    public List<Product> getOptions(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }



//    @Transactional
//    public void createProduct(ProductDTO productDTO){
//        Optional<Category> category = this.categoryRepository.findById(productDTO.categoryId);
//        if (category.isPresent()){
//            Product product = new Product(
//                    productDTO.name,
//                    productDTO.description,
//                    productDTO.price,
//                    category.get(),
//                    productDTO.durability,
//                    productDTO.fitting,
//                    productDTO.imageUrl,
//                    productDTO.quantity
//            );
//            this.productRepository.save(product);
//            return;
//        }
//
//        throw new ResponseStatusException(
//                HttpStatus.NOT_FOUND, "Category not found"
//        );
//    }

    @Transactional
    public void createProduct(@NotNull Product product){
        this.categoryRepository.save(product.getCategory());
        this.productRepository.save(product);
    }

    public void updateProduct(ProductDTO productDTO, Long id){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setDescription(productDTO.description);
            product.get().setName(productDTO.name);

            this.productRepository.save(product.get());
        }
    }

    public void checkProduct(Long id) {
        this.toggleProduct(id, true);
    }

    public void uncheckProduct(Long id) {
        this.toggleProduct(id, false);
    }

    private void toggleProduct(Long id, boolean value){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setFinished(value);
            this.productRepository.save(product.get());
        }
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
