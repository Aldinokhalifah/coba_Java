# Sistem Subscription Digital - Complete Testing Suite

Semua file testing telah berhasil dibuat dan dikompilasi tanpa error!

## ✅ File-File Testing yang Dibuat

### 1. **MainUserServiceTest.java** (196 baris)
Testing untuk UserService dengan 12 skenario test:
- ✓ Create User (Valid)
- ✗ Null/Blank Name Validation
- ✗ Invalid Email Format  
- ✗ Duplicate Email Prevention
- ✓ Get User By ID
- ✓ Get User By Email
- ✓ Get All Users

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.MainUserServiceTest
```

---

### 2. **MainPlanServiceTest.java** (283 baris)
Testing untuk PlanService dengan 17 skenario test:
- ✓ Create Plan (Valid)
- ✗ Null/Blank Name Validation
- ✗ Duplicate Code Prevention
- ✗ Invalid/Zero Price
- ✗ Null Period
- ✓ Get Plan By ID & Code
- ✓ Update Plan (Name Change)
- ✗ Code Change Prevention
- ✓ Change Price
- ✗ Same Price Prevention
- ✗ Negative/Past Date Validation

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.MainPlanServiceTest
```

---

### 3. **MainPaymentServiceTest.java** (283 baris)
Testing untuk PaymentService dengan 12 skenario test:
- ✓ Attempt Payment (Valid)
- ✗ Null Invoice/Method Validation
- ✗ Already Paid Prevention
- ✗ Expired Invoice Handling
- ✗ Invalid Amount
- ✗ Duplicate Payment Prevention
- ✓ Process Payment For Invoice
- ✗ Non-Pending Invoice Rejection

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.MainPaymentServiceTest
```

---

### 4. **MainInvoiceServiceTest.java** (263 baris)
Testing untuk InvoiceService dengan 13 skenario test:
- ✓ Create Initial Invoice (Valid)
- ✗ Null Subscription
- ✗ Inactive Subscription
- ✗ Null Plan
- ✗ Duplicate Invoice
- ✓ Create Renewal Invoice (Valid)
- ✗ Null Billing Date
- ✗ Future Billing Date
- ✗ Past Billing Date
- ✗ Before Start Date
- ✗ Previous Unpaid Invoice
- ✓ Mark Invoice As Paid

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.MainInvoiceServiceTest
```

---

### 5. **MainSubscriptionServiceTest.java** (267 baris)
Testing untuk SubscriptionService dengan 14 skenario test:
- ✓ Create Subscription (Valid)
- ✗ Null User ID
- ✗ Null Plan ID
- ✗ User Not Found
- ✗ Plan Not Found
- ✗ Duplicate Active Subscription
- ✓ Cancel Subscription
- ✗ Already Cancelled
- ✓ Suspend Subscription
- ✓ Find Active Subscription
- ✓ Auto Renew Testing

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.MainSubscriptionServiceTest
```

---

### 6. **Main.java** (117 baris)
Integration test menyeluruh yang menguji semua services dalam satu flow:
- Membuat Users
- Membuat Plans
- Membuat Subscriptions
- Membuat Invoices
- Memproses Payments
- Summary statistik

**Jalankan:**
```bash
java -cp bin Sistem_Subcription_Digital.app.Main
```

---

## 🛠️ Cara Menjalankan Tests

### Dari folder project:
```bash
cd "c:\Users\Aldino\OneDrive\Desktop\OneDrive\Java\Sistem_Subcription_Digital"

# Compile (sudah dilakukan)
javac -encoding UTF-8 -d bin app/*.java service/*.java model/*.java repository/*.java

# Run one specific test
java -cp bin Sistem_Subcription_Digital.app.MainUserServiceTest

# Run all tests (jalankan satu per satu)
java -cp bin Sistem_Subcription_Digital.app.MainUserServiceTest
java -cp bin Sistem_Subcription_Digital.app.MainPlanServiceTest
java -cp bin Sistem_Subcription_Digital.app.MainPaymentServiceTest
java -cp bin Sistem_Subcription_Digital.app.MainInvoiceServiceTest
java -cp bin Sistem_Subcription_Digital.app.MainSubscriptionServiceTest
java -cp bin Sistem_Subcription_Digital.app.Main
```

---

## 📊 Test Coverage Summary

| Service | Test Scenarios | Valid Cases | Invalid Cases |
|---------|---|---|---|
| UserService | 12 | 3 | 9 |
| PlanService | 17 | 5 | 12 |
| PaymentService | 12 | 3 | 9 |
| InvoiceService | 13 | 4 | 9 |
| SubscriptionService | 14 | 5 | 9 |
| **TOTAL** | **68** | **20** | **48** |

---

## ✨ Fitur Testing

Setiap test file mencakup:

✅ **Positive Testing** - Test skenario yang valid/sukses
❌ **Negative Testing** - Test skenario invalid/error cases
🔍 **Boundary Testing** - Test nilai-nilai batas (null, empty, dll)
📋 **Exception Handling** - Memverifikasi exception yang sesuai
📝 **Clear Output** - Output yang mudah dipahami dengan ✓ dan ✗

---

## 🐛 Error Handling

Semua test file telah dikompilasi dan siap dijalankan. Jika ada error saat eksekusi, kemungkinan adalah:

1. **ClassNotFoundException** - Pastikan sudah compile dengan `javac`
2. **NullPointerException** - Beberapa repository mungkin belum fully implemented
3. **Other exceptions** - Sesuai dengan business logic validation

---

## 📝 Catatan Penting

- Semua test menggunakan `InMemory` repositories (tidak menggunakan database real)
- Data bersifat temporary (hilang setelah program selesai)
- Setiap test file berdiri sendiri dengan repositori terpisah
- Untuk test yang lebih komprehensif, bisa di-extend dengan JUnit

---

**Created:** April 1, 2026
**Status:** ✅ Compiled & Ready to Run