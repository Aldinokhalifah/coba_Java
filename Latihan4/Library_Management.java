
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Library_Management {
    
    static class Book {
        enum Category {NOVEL, HISTORY, TECHNOLOGY, MAGAZINE}

        private Long id;
        private String title;
        private String author;
        private Category category;
        private boolean available;
        private LocalDateTime createdAt, updatedAt;

        public Book(Long id, String title, String author, Category category, boolean available, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.available = available;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Category getCategory() {
            return category;
        }

        public boolean getAvailable() {
            return available;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getupdatedAt() {
            return updatedAt;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public void setupdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    static class BookRepository {
        private final ArrayList<Book> books = new ArrayList<>();
        private final AtomicLong seq;

        public BookRepository(AtomicLong seq) {
            this.seq = seq;
        }

        public Book createBook(String title, String author, Book.Category category) {
            Book newBook = new Book(seq.incrementAndGet(), title, author, category, true, LocalDateTime.now(), LocalDateTime.now());

            books.add(newBook);
            return newBook;
        }

        public Optional<Book> findById(Long id) {
            return books.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
        }

        public List<Book> findAll() {
            return new ArrayList<>(books);
        }

        public List<Book> findByCategory(Book.Category category) {
            return books.stream().filter(b -> b.getCategory() == category).collect(Collectors.toList());
        }

        public boolean updateBook(Long id, String newTitle, String newAuthor, Book.Category newCategory) {
            Optional<Book> book = findById(id);

            if(book.isEmpty()) return false;

            Book b = book.get();
            b.setTitle(newTitle);
            b.setAuthor(newAuthor);
            b.setCategory(newCategory);
            b.setupdatedAt(LocalDateTime.now());

            return true;
        }

        public boolean deleteBook(Long id) {
            return books.removeIf(b -> Objects.equals(b.getId(), id));
        }
    }

    static class BookService {
        private final BookRepository repo;

        public BookService(BookRepository repo) {
            this.repo = repo;
        }

        public Book addBook(String title, String author, Book.Category category) {
            if(title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title tidak boleh kosong");
            }

            if(author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author tidak boleh kosong");
            }

            
            if (category == null) {
                throw new IllegalArgumentException("Kategori tidak boleh null");
            }

            Book book = repo.createBook(title, author, category);
            System.out.println("Berhasil menambahkan buku dengan ID: " + book.getId());

            return book;
        }

        public List<Book> findAll() {
            List<Book> books = repo.findAll();

            if(books.isEmpty()) {
                throw new IllegalAccessError("Tidak ada buku yang tersimpan");
            } 

            return books;
        }

        public Book borrowBook(Long id) {
            Optional<Book> book = repo.findById(id);

            if(book.isPresent()) {
                Book b = book.get();

                if(b.getAvailable() == false) {
                    System.out.println("Buku sedang dipinjam");
                } else {
                    b.setAvailable(false);
                    System.out.println("Buku dengan ID: " + id + " berhasil dipinjam");
                }

                return b;
            } else {
                throw new IllegalArgumentException("Buku tidak ditemukan");
            }
        }

        public Book returnBook(Long id) {
            Optional<Book> book = repo.findById(id);

            if(book.isPresent()) {
                Book b = book.get();

                if(b.getAvailable() == true) {
                    System.out.println("Buku sedang tidak dipinjam");
                } else {
                    b.setAvailable(true);
                    System.out.println("Buku dengan ID: " + id + " berhasil dikembalikan");
                }

                return b;
            } else {
                throw new IllegalArgumentException("Buku tidak ditemukan");
            }
        }

        public Book updateBook(Long id, String newTitle, String newAuthor, Book.Category newCategory) {
            Optional<Book> book = repo.findById(id);

            if(newTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Title tidak boleh kosong");
            }

            if(newAuthor.trim().isEmpty()) {
                throw new IllegalArgumentException("Author tidak boleh kosong");
            }

            
            if (newCategory == null) {
                throw new IllegalArgumentException("Kategori tidak boleh null");
            }

            if(book.isPresent()) {
                boolean updated = repo.updateBook(id, newTitle, newAuthor, newCategory);
                System.out.println("Berhasil update buku dengan ID: " + id);

                return book.get();
            } else {
                throw new IllegalArgumentException("Buku tidak ditemukan");
            }
        }

        public Book deleteBook(Long id) {
            Book b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID: " + id + " tidak ditemukan"));

            repo.deleteBook(id);

            return b;
        }
    }
}