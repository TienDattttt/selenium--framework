# Test Plan - Sprint 5
**Tinh nang:** Thanh toan tra gop qua VPBank (3/6/12 thang, phi 0% voi don >= 3 trieu dong)  
**Sprint:** Sprint 5 (2 tuan)  
**QA Lead:** [Ten]  
**Ngay cap nhat:** 01/04/2026

---

## 1. Muc tieu Sprint 5

Ra mat tinh nang thanh toan tra gop VPBank cho nguoi dung ShopEasy:
- Ho tro 3 ky han: 3 thang, 6 thang, 12 thang
- Dieu kien: Don hang >= 3.000.000 VND, phi 0%
- Tich hop voi VPBank Payment Gateway (sandbox -> production)

---

## 2. Phan tich rui ro nghiep vu - 5 kich ban co the gay mat tien nguoi dung

| # | Kich ban rui ro | Hau qua | Muc do |
|---|----------------|---------|--------|
| R1 | Don < 3 trieu nhung he thong van cho phep chon tra gop | Nguoi dung bi thu phi ngoai y muon, khieu nai | P1 - Blocker |
| R2 | Tinh so tien tra moi thang sai (lam tron sai, chia khong deu) | Nguoi dung bi tru tien khong dung hop dong | P1 - Blocker |
| R3 | Thanh toan thanh cong nhung don hang khong duoc xac nhan (race condition / timeout) | Mat tien, khong co hang - rui ro phap ly | P1 - Blocker |
| R4 | He thong loi giua chung, tien da bi tru nhung chua confirm (partial commit) | Tien mat, trang thai don hang khong xac dinh | P1 - Blocker |
| R5 | Session het han giua qua trinh thanh toan | Nguoi dung bi logout, mat thong tin don, tien co the da duoc ghi no | P2 - Critical |

---

## 3. 15 Test Case Sprint 5

| ID | Tieu de | Loai | Uu tien | Dieu kien tien quyet | Buoc thuc hien tom tat | Ket qua mong doi |
|-------|---------|------|---------|---------------------|----------------------|----------------|
| TC-001 | Thanh toan tra gop 3 thang, don >= 3tr | API | P1 | Don hang 3.500.000d, tai khoan VPBank hop le | POST /api/payment/installment {term:3, amount:3500000} | HTTP 201, status=APPROVED, monthly=1.166.667d |
| TC-002 | Bi tu choi khi don < 3 trieu | API | P1 | Don hang 2.999.999d | POST /api/payment/installment {term:3, amount:2999999} | HTTP 400, error=ORDER_TOO_SMALL |
| TC-003 | UI hien thi dung 3 tuy chon 3/6/12 thang | UI | P1 | Don hang >= 3tr, trang thanh toan | Mo trang checkout, kiem tra section tra gop | 3 radio button hien thi dung nhan: "3 thang", "6 thang", "12 thang" |
| TC-004 | Tinh so tien tra/thang chinh xac | Unit | P1 | InstallmentCalculator class | Goi calculate(3500000, 3) | Tra ve 1.166.667d (lam tron len, thang cuoi bu phan le) |
| TC-005 | Hien thi tong tien tra gop ro rang tren UI | UI | P2 | Don >= 3tr, chon ky han 6 thang | Quan sat UI sau khi chon "6 thang" | Hien thi: tien goc, so tien/thang, tong = goc (phi 0%) |
| TC-006 | Thanh toan 6 thang thanh cong | API | P1 | Don 6.000.000d, tai khoan VPBank hop le | POST /api/payment/installment {term:6, amount:6000000} | HTTP 201, status=APPROVED, monthly=1.000.000d |
| TC-007 | Thanh toan 12 thang thanh cong | API | P1 | Don 12.000.000d, tai khoan VPBank hop le | POST /api/payment/installment {term:12, amount:12000000} | HTTP 201, status=APPROVED, monthly=1.000.000d |
| TC-008 | Tra gop voi tai khoan VPBank hop le - luong E2E | E2E | P1 | Tai khoan VPBank test, don 5.000.000d | Dang nhap -> them gio -> checkout -> chon tra gop -> xac nhan | Don hang APPROVED, email xac nhan gui, lich su cap nhat |
| TC-009 | Tra gop voi the VPBank het han | API | P1 | The test expired | POST /api/payment/installment voi card expired | HTTP 400, error=CARD_EXPIRED, tien khong bi tru |
| TC-010 | Tra gop khi VPBank sandbox tra loi 503 | API | P2 | Mock VPBank tra 503 | POST /api/payment/installment (VPBank mock down) | HTTP 503, thong bao loi than thien, khong hien technical detail |
| TC-011 | Nhan 'Huy' giua chung khong tru tien | E2E | P1 | Dang o buoc xac nhan thanh toan | Click nut Huy tai trang xac nhan VPBank | Redirect ve gio hang, tien khong bi tru, don status=CANCELLED |
| TC-012 | Session het han giua thanh toan | E2E | P2 | Session timeout = 30 phut | De idle 31 phut -> thu confirm thanh toan | Redirect ve login, don hang duoc luu tam (draft), tien khong bi tru |
| TC-013 | Khong hien thi tuy chon tra gop voi don < 3tr | UI | P2 | Don hang 2.500.000d | Mo trang checkout | Section "Tra gop VPBank" bi an hoan toan, khong co radio button |
| TC-014 | Email xac nhan gui sau khi tra gop thanh cong | API | P2 | Don APPROVED, email user hop le | Kiem tra mailbox sau khi TC-008 thanh cong | Email den trong vong 5 phut, noi dung du: ky han, so tien/thang, ngay dau |
| TC-015 | Lich su don hang hien thi thong tin tra gop | UI | P3 | Don da APPROVED tu TC-008 | Vao "Don hang cua toi" -> xem chi tiet | Hien thi: ky han da chon, so tien/thang, so ky con lai |

---

## 4. Blockers va ke hoach xu ly

| # | Blocker | Xac suat | Ke hoach xu ly | Owner | Deadline |
|---|---------|----------|----------------|-------|----------|
| B1 | Sandbox VPBank chua ready | Cao | Yeu cau VPBank cung cap sandbox truoc Sprint 2 tuan; dung WireMock lam mock API du phong cho TC-001 den TC-010 | QA Lead + PM | Ngay 1 Sprint |
| B2 | Test data tai khoan ngan hang VPBank chua co | Cao | PM lam viec voi VPBank de co it nhat 3 tai khoan test (hop le / het han / khong du han muc) | PM + BA | Ngay 2 Sprint |
| B3 | API thanh toan chua co tai lieu Swagger/Postman | Trung binh | Backend viet API contract (request/response schema) truoc khi code xong - khong cho dev hoan thanh moi viet test | Backend Lead | Ngay 3 Sprint |
| B4 | Staging chua deploy code Sprint 5 | Trung binh | Tuan 1 test voi mock (WireMock); Tuan 2 test voi staging that sau khi BE deploy | DevOps + QA | Ngay 7 Sprint |

---

## 5. Ke hoach 2 tuan Sprint 5

| Ngay | Hoat dong |
|------|----------|
| Ngay 1-2 | Thiet lap mock VPBank (WireMock), viet TC-001 den TC-010 (API test) |
| Ngay 3-4 | Chay API test voi mock, ghi nhan bug som |
| Ngay 5-6 | Viet UI test TC-003, TC-005, TC-013, TC-015 |
| Ngay 7 | Staging deploy -> chuyen tu mock sang staging that |
| Ngay 8-9 | Chay full regression + E2E TC-008, TC-011, TC-012 |
| Ngay 10 | UAT voi PO, fix P1 bug, sign-off release |