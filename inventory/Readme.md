# 🏪 Inventory Warung API

REST API untuk manajemen inventaris warung — mencatat stok produk, transaksi masuk/keluar, dan dilengkapi autentikasi JWT dengan role-based access control.

## Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA** + **Hibernate**
- **Spring Security** + **JWT (jjwt)**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Fitur

- CRUD produk (nama, satuan, harga beli, harga jual, stok minimum)
- Catat transaksi masuk/keluar stok secara otomatis
- Deteksi stok menipis (stok di bawah minimum)
- Validasi stok — transaksi keluar ditolak kalau stok tidak mencukupi
- Autentikasi JWT (register & login)
- Role-based access control (USER & ADMIN)

## Struktur Project

```
src/main/java/com/warung/inventory/
├── controller/
│   ├── AuthController.java
│   ├── ProdukController.java
│   └── TransaksiController.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── ProdukRequest.java
│   │   ├── RegisterRequest.java
│   │   └── TransaksiRequest.java
│   └── response/
│       ├── ApiResponse.java
│       ├── AuthResponse.java
│       ├── ProdukResponse.java
│       └── TransaksiResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── InsufficientStockException.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── Produk.java
│   ├── Role.java
│   ├── Transaksi.java
│   └── User.java
├── repository/
│   ├── ProdukRepository.java
│   ├── TransaksiRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   ├── SecurityConfig.java
│   └── UserDetailsServiceConfig.java
└── service/
    ├── AuthService.java
    ├── ProdukService.java
    └── TransaksiService.java
```

## Instalasi & Menjalankan

### Prasyarat

- JDK 21+
- Maven
- PostgreSQL

### Langkah-langkah

**1. Clone repository**
```bash
git clone https://github.com/Aldinokhalifah/coba_Java/inventory.git
cd src
```

**2. Buat database PostgreSQL**
```sql
CREATE DATABASE warung_inventory;
```

**3. Konfigurasi `application.properties`**
```properties
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/warung_inventory
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

application.security.jwt.secret=your_secret_key
application.security.jwt.expiration=86400000
```

**4. Jalankan aplikasi**
```bash
mvn spring-boot:run
```

Aplikasi berjalan di `http://localhost:8080`.

## API Endpoints

### Auth
| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| POST | `/api/auth/register` | Public | Registrasi user baru |
| POST | `/api/auth/login` | Public | Login dan dapatkan token |

### Produk
| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| GET | `/api/produk` | USER, ADMIN | List semua produk |
| GET | `/api/produk/{id}` | USER, ADMIN | Detail produk |
| GET | `/api/produk/stok-menipis` | USER, ADMIN | Produk dengan stok di bawah minimum |
| POST | `/api/produk` | ADMIN | Tambah produk baru |
| PUT | `/api/produk/{id}` | ADMIN | Update produk |
| DELETE | `/api/produk/{id}` | ADMIN | Hapus produk |

### Transaksi
| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| GET | `/api/transaksi` | USER, ADMIN | List semua transaksi |
| GET | `/api/transaksi/produk/{produkId}` | USER, ADMIN | Transaksi by produk |
| POST | `/api/transaksi` | USER, ADMIN | Catat transaksi baru |

## Contoh Request & Response

### Register
```json
POST /api/auth/register
{
  "nama": "Aldino",
  "email": "aldino@gmail.com",
  "password": "123456"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "aldino@gmail.com",
  "password": "123456"
}

// Response
{
  "success": true,
  "message": "Login berhasil",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### Tambah Produk
```json
POST /api/produk
Authorization: Bearer <token>
{
  "nama": "Indomie Goreng",
  "satuan": "pcs",
  "hargaBeli": 2500,
  "hargaJual": 3500,
  "stokMinimum": 10
}
```

### Catat Transaksi
```json
POST /api/transaksi
Authorization: Bearer <token>
{
  "produkId": "uuid-produk",
  "jenis": "MASUK",
  "jumlah": 50,
  "keterangan": "Stok awal"
}
```

## Format Response

Semua endpoint menggunakan format response yang konsisten:

```json
{
  "success": true,
  "message": "Pesan deskriptif",
  "data": { ... }
}
```

Untuk error:
```json
{
  "success": false,
  "message": "Produk tidak ditemukan",
  "data": null
}
```

## HTTP Status Codes

| Status | Keterangan |
|--------|-----------|
| 200 | OK — request berhasil |
| 201 | Created — data berhasil dibuat |
| 400 | Bad Request — validasi gagal / stok tidak cukup |
| 401 | Unauthorized — token tidak valid |
| 403 | Forbidden — tidak punya akses |
| 404 | Not Found — data tidak ditemukan |
| 500 | Internal Server Error |

## Assign Role Admin

Secara default semua user yang register mendapat role `USER`. Untuk assign role `ADMIN`, update manual di database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```
