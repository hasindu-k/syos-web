package model;

public class ProductView {
	
	private Product product;
    private String categoryName;
    private int stockQuantity;

	public ProductView(Product product, String categoryName, int stockQuantity) {
        this.product = product;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
	}
	
	public Product getProduct() {
        return product;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

}

