================================================================================
                 SISTEM SUBSCRIPTION DIGITAL - README
================================================================================

PROJECT OVERVIEW
================================================================================
Sistem Subscription Digital adalah aplikasi backend Java yang mengelola sistem 
subscription (langganan) digital dengan fitur pembayaran otomatis, pembuatan 
invoice, dan manajemen plan/paket berlangganan.

Aplikasi ini mengimplementasikan layanan berbasis arsitektur berlapis (layered 
architecture) dengan separation of concerns antara model, repository, dan service.

================================================================================
PROJECT PURPOSE
================================================================================
Aplikasi ini bertujuan untuk:
• Mengelola user/pelanggan yang berlangganan
• Mengelola berbagai paket subscription (plan)
• Membuat dan melacak invoice untuk setiap subscription
• Memproses pembayaran secara otomatis
• Menangani status subscription (active, suspended, cancelled, expired)
• Mendukung upgrade/downgrade plan
• Mengelola auto-renewal subscription

================================================================================
SERVICES DESCRIPTION
================================================================================

1. USER SERVICE (UserService.java)
   ├─ createUser()           : Membuat user/pelanggan baru
   ├─ getUserById()          : Mencari user berdasarkan ID
   ├─ getUserByEmail()       : Mencari user berdasarkan email
   └─ getAllUsers()          : Mendapatkan semua user terdaftar

   Validasi:
   • Nama tidak boleh kosong/null
   • Email harus format valid dan unik (tidak duplikat)

---

2. PLAN SERVICE (PlanService.java)
   ├─ createPlan()          : Membuat paket subscription baru
   ├─ updatePlan()          : Mengubah informasi plan (nama, dll)
   ├─ changePlanPrice()     : Mengubah harga plan (efektif di masa depan)
   ├─ getPlanById()         : Mencari plan berdasarkan ID
   └─ getPlanByCode()       : Mencari plan berdasarkan kode unik

   Validasi:
   • Kode plan harus unik (tidak duplikat)
   • Harga harus lebih dari 0
   • Tidak boleh mengubah kode plan
   • Perubahan harga hanya berlaku tanggal masa depan
   • Periode (MONTHLY/YEARLY) harus valid

---

3. SUBSCRIPTION SERVICE (SubscriptionService.java)
   ├─ createSubscription()   : Membuat subscription baru untuk user
   ├─ cancelSubscription()   : Membatalkan subscription
   ├─ upgradeSubscription()  : Upgrade ke plan yang lebih mahal
   ├─ downgradeSubscription(): Downgrade ke plan yang lebih murah
   └─ suspendSubscription()  : Menangguhkan subscription (gagal bayar)

   Validasi:
   • User hanya boleh punya 1 subscription ACTIVE per plan
   • Status subscription: ACTIVE, SUSPENDED, EXPIRED, CANCELLED
   • Pembayaran otomatis saat subscription dibuat
   • Invoice pertama dibuat saat subscription aktif

---

4. INVOICE SERVICE (InvoiceService.java)
   ├─ createInitialInvoice()  : Membuat invoice pertama saat subscribe
   ├─ createRenewalInvoice()  : Membuat invoice renewal saat period habis
   └─ markInvoicePaid()       : Menandai invoice sebagai sudah dibayar

   Validasi:
   • Hanya 1 unpaid invoice per subscription
   • Billing date harus sesuai dengan cycle (monthly/yearly)
   • Invoice expired otomatis jika lewat due date
   • Status: PENDING, PAID, FAILED, EXPIRED

---

5. PAYMENT SERVICE (PaymentService.java)
   ├─ attemptPayment()         : Mencoba melakukan pembayaran
   ├─ processPayment()         : Memproses pembayaran (set status SUCCESS)
   ├─ processPaymentForInvoice(): Melakukan pembayaran untuk invoice tertentu
   └─ handleGatewayCallback()  : Handle callback dari payment gateway

   Validasi:
   • Invoice harus PENDING untuk bisa dibayar
   • Tidak boleh ada duplicate payment untuk invoice sama
   • Payment method: CARD, VA (Virtual Account), EWALLET, MANUAL
   • Status payment: PENDING, SUCCESS, FAILED
   • Jika payment gagal, subscription di-suspend

================================================================================
TECHNOLOGY STACK
================================================================================
• Language    : Java
• Architecture: Layered Architecture (Model-Repository-Service)
• Data Storage: In-Memory (tidak pakai database real)
• Testing     : Manual testing dengan main classes

================================================================================
TEST COVERAGE
================================================================================
Total Test Scenarios: 68
├─ Positive Tests (Valid Cases)     : 20 test cases ✓
└─ Negative Tests (Invalid Cases)   : 48 test cases ✗

Per Service:
• UserService          : 12 scenarios
• PlanService          : 17 scenarios
• PaymentService       : 12 scenarios
• InvoiceService       : 13 scenarios
• SubscriptionService  : 14 scenarios

Lihat TESTING_GUIDE.md untuk detail lengkap setiap test case.

================================================================================
KEY FEATURES
================================================================================
✓ User Management          - Registrasi & manajemen user
✓ Plan Management          - Buat & kelola paket subscription
✓ Subscription Lifecycle   - Create, Active, Suspend, Cancel, Expire
✓ Automatic Billing        - Invoice otomatis saat period habis
✓ Payment Processing       - Proses pembayaran dengan validation
✓ Auto-Renewal             - Subscription bisa auto-renew setiap cycle
✓ Plan Upgrade/Downgrade   - User bisa ganti plan
✓ Exception Handling       - Comprehensive validation & error handling

================================================================================
BUSINESS LOGIC HIGHLIGHTS
================================================================================
1. Subscription dibuat dengan status ACTIVE dan akan generate invoice pertama
2. Invoice otomatis dibuat untuk setiap cycle (monthly/yearly)
3. Jika payment gagal, subscription otomatis SUSPENDED
4. Jika payment sukses, subscription tetap ACTIVE & end date di-extend
5. User hanya bisa punya 1 ACTIVE subscription per plan
6. Plan price bisa berubah tapi hanya efektif di masa depan
7. Subscription bisa di-upgrade/downgrade dengan proration harga

================================================================================
NOTES
================================================================================
• Aplikasi menggunakan In-Memory storage (data hilang saat program stop)
• Tidak ada persistence ke database/file
• Cocok untuk testing, prototyping, dan development
• Untuk production perlu ditambah: Database, Payment Gateway Integration, Logging
• Exception handling sudah comprehensive (null checks, validation, dll)

================================================================================
AUTHOR & DATE
================================================================================
Created  : April 1, 2026
Modified : April 2, 2026
Status   : ✓ Compiled & Ready to Run

================================================================================
                              END OF README
================================================================================
