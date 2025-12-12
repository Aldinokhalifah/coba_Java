import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Toko_Online {

    static class Product {
        enum Category { ELECTRONIC, FOOD, CLOTHES, STATIONARY }

        private Long id;
        private String name;
        private Category category;
        private double price;
        private int stock;
        private LocalDateTime createdAt, updatedAt;

        public Product(Long id, String name, Category category, double price, int stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.stock = stock;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public Category getCategory() { return category; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }

        public void setName(String name) { this.name = name; }
        public void setCategory(Category category) { this.category = category; }
        public void setPrice(double price) { this.price = price; }
        public void setStock(int stock) { this.stock = stock; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }

    static class ProductRepository {
        private final ArrayList<Product> products = new ArrayList<>();
        private final AtomicLong seq;

        public ProductRepository(AtomicLong seq) {
            this.seq = seq;
        }

        public Product createProduct(String name, Product.Category category, double price, int stock) {
            Product newProduct = new Product(seq.incrementAndGet(), name, category, price, stock, LocalDateTime.now(), LocalDateTime.now());

            products.add(newProduct);
            return newProduct;
        }

        public Optional<Product> findById(Long id) {
            return products.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
        }

        public List<Product> findByCategory(Product.Category category) {
            return products.stream().filter(p -> p.getCategory() == category).collect(Collectors.toList());
        }

        public List<Product> findAll() {
            return new ArrayList<>(products);
        }

        public boolean updateProduct(Long id, String newName, double newPrice, int newStock) {
            Optional<Product> optional = findById(id);
            if (optional.isEmpty()) return false;

            Product p = optional.get();
            p.setName(newName);
            p.setPrice(newPrice);
            p.setStock(newStock);
            p.setUpdatedAt(LocalDateTime.now());

            return true;
        }

        public boolean deleteProduct(Long id) {
            return products.removeIf(p -> Objects.equals(p.getId(), id));
        }
    }

    static class ProductService {
        private final ProductRepository repo;

        public ProductService(ProductRepository repo) {
            this.repo = repo;
        }

        public Product createProduct(String name, String category, double price, int stock) {
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama tidak boleh kosong");
            }

            Product.Category parsedCategory;
            try {
                parsedCategory = Product.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Kategori tidak valid");
            }

            if (price <= 0) {
                throw new IllegalArgumentException("Harga harus lebih dari 0");
            }

            if (stock < 0) {
                throw new IllegalArgumentException("Stok tidak boleh negatif");
            }

            Product product = repo.createProduct(name, parsedCategory, price, stock);
            System.out.println("Produk berhasil ditambahkan: ID = " + product.getId());

            return product;
        }

        public Product getById(Long id) {
            return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID: " + id + " tidak ditemukan"));
        }

        public List<Product> getAllProduct() {
            return repo.findAll();
        }

        public List<Product> getByCategory(String category) {
            try {
                Product.Category cat = Product.Category.valueOf(category.toUpperCase());
                return repo.findByCategory(cat);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Kategori tidak valid");
            }
        }

        public Product updateProduct(Long id, String newName, double newPrice, int newStock) {
            if (newName.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama tidak boleh kosong");
            }

            if (newPrice <= 0) {
                throw new IllegalArgumentException("Harga harus lebih dari 0");
            }

            if (newStock < 0) {
                throw new IllegalArgumentException("Stok tidak boleh negatif");
            }

            boolean updated = repo.updateProduct(id, newName, newPrice, newStock);
            if (!updated) {
                throw new IllegalArgumentException("ID: " + id + " tidak ditemukan");
            }

            return repo.findById(id).get();
        }

        public Product deleteProduct(Long id) {
            Product p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID: " + id + " tidak ditemukan"));

            repo.deleteProduct(id);
            return p;
        }
    }
}