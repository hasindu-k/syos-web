// src/service/ProductService.java
package service;

import dao.ProductDao;
import factory.ModelFactory;
import model.Product;

import java.util.List;

/**
 * Service class for Product operations.
 */
public class ProductService {
    private ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    /**
     * Adds a new product with stock.
     * @param name Product name.
     * @param unitId Unit ID.
     * @param categoryId Category ID.
     * @param status Product status.
     * @param warehouseId Warehouse ID.
     * @param note Additional notes.
     * @param stockAlert Stock alert level.
     * @param supplierId Supplier ID.
     * @return Product ID if successful, else -1.
     */
    public int addProduct(String name, int unitId, int categoryId, String status,
                          int warehouseId, String note, int stockAlert, int supplierId) {
        Product product = ModelFactory.createProduct(name, unitId, categoryId, status,
                warehouseId, note, stockAlert, supplierId);
        return productDao.addProduct(product);
    }

    /**
     * Retrieves all products.
     * @return List of products.
     */
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    /**
     * Updates an existing product.
     * @param id Product ID.
     * @param name New name.
     * @param unitId New Unit ID.
     * @param categoryId New Category ID.
     * @param status New status.
     * @param warehouseId New Warehouse ID.
     * @param note New notes.
     * @param stockAlert New stock alert level.
     * @param supplierId New Supplier ID.
     * @return True if successful, else False.
     */
    public boolean updateProduct(int id, String name, int unitId, int categoryId,
                                 String status, int warehouseId, String note, int stockAlert, int supplierId) {
        Product product = ModelFactory.createProduct(name, unitId, categoryId, status,
                warehouseId, note, stockAlert, supplierId);
        product.setId(id);
        return productDao.updateProduct(product);
    }

    /**
     * Deletes a product by ID.
     * @param id Product ID.
     * @return True if successful, else False.
     */
    public boolean deleteProduct(int id) {
        return productDao.deleteProduct(id);
    }

    /**
     * Retrieves a product by its ID.
     * @param id Product ID to search for.
     * @return Product if found, null otherwise.
     */
    public Product getProductById(int id) {
        return productDao.getProductById(id);
    }

    /**
     * Searches for products by name or code.
     * @param query The search query (product name or code)
     * @return List of matching products
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return productDao.searchProducts(query.trim());
    }
}
