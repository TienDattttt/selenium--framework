# Test Strategy Document
**Du an:** ShopEasy - Ung dung mua sam online  
**Phien ban:** 1.0  
**Ngay soan:** 01/04/2026  
**Tac gia:** QA Lead  
**Tech stack:** Java Spring Boot (API) + React (Web) + React Native (Mobile)  
**Team:** 4 Dev (2 BE, 2 FE) · 1 QA · 1 Designer · 1 PM  
**Moi truong:** Dev (localhost) -> Staging (staging.shopeasy.vn) -> Production (shopeasy.vn)

Tai lieu nay dinh huong chien luoc kiem thu tong the cho du an ShopEasy trong giai doan chuan bi Sprint 5. Muc tieu cua chien luoc khong chi la tim bug, ma con tao ra mot cach tiep can co he thong de uu tien rui ro, toi uu thoi gian cua team QA, va dam bao cac luong nghiep vu lien quan den don hang va thanh toan duoc bao ve tot nhat. Trong boi canh team chi co 1 QA va pham vi san pham rong, chien luoc nay uu tien shift-left, automation-first, va su phoi hop chat che giua QA, Dev, PM va Designer.

---

## 1. Pham vi kiem thu (Scope)

| Loai | Module / Tinh nang | Ly do |
|------|-------------------|-------|
| IN SCOPE | Dang ky tai khoan | Core feature, anh huong nguoi dung moi |
| IN SCOPE | Dang nhap / Xac thuc | Bao mat, rui ro cao |
| IN SCOPE | Tim kiem san pham | Tinh nang chinh, dung hang ngay |
| IN SCOPE | Gio hang | Tien hanh mua sam, anh huong doanh thu |
| IN SCOPE | Thanh toan (bao gom tra gop VPBank) | Lien quan tien that, rui ro cao nhat |
| OUT SCOPE | Admin Dashboard | Phase 2, chua co trong Sprint nay |
| OUT SCOPE | Bao cao thong ke | Phase 2, khong anh huong end-user truc tiep |
| OUT SCOPE | App Mobile (React Native) | Can thiet bi rieng, QA chi co 1 nguoi - uu tien Web truoc |

Pham vi duoc chon dua tren nguyen tac gia tri kinh doanh va muc do rui ro. Cac tinh nang nam trong IN SCOPE deu la nhung diem cham truc tiep voi nguoi dung va tac dong den chuyen doi, doanh thu, hoac do tin cay cua san pham. Trong do, thanh toan la khu vuc nhay cam nhat vi lien quan den tien that, doi tac ngan hang, va uy tin thuong hieu. Nguoc lai, cac phan OUT SCOPE khong phai khong quan trong, ma duoc hoan lai de tranh phan tan nguon luc. Voi nguon luc QA han che, chien luoc chon do sau cho luong mua hang tren web thay vi trai rong toan bo san pham cung luc.

---

## 2. Phan loai test va ti le

| Loai test | Ti le | Cong cu | Ly do chon |
|-----------|-------|---------|-----------|
| Unit Test | 20% | JUnit 5 + Mockito | Developer tu test, phan hoi nhanh nhat, chi phi thap nhat |
| API Test | 45% | RestAssured | Thuong mai dien tu phu thuoc API rat nhieu - tat ca nghiep vu deu di qua API layer; test API nhanh hon UI 10x va it flaky hon |
| UI Test (Selenium) | 20% | Selenium 4 + POM | Kiem tra trai nghiem nguoi dung thuc te - luong mua sam dau-cuoi khong the bo qua |
| Performance Test | 10% | JMeter | Ngay sale flash sale, he thong chiu 10.000+ CCU - can do truoc khi production |
| Security Test | 5% | OWASP ZAP | Co du lieu the tin dung va thong tin ngan hang - tuan thu PCI-DSS bat buoc |

**Ly do phan bo ti le nay:**  
API Test chiem 45% vi ShopEasy la ung dung API-first - moi tinh nang (gio hang, thanh toan, tim kiem) deu goi REST API. Phat hien loi o tang API som hon va re hon gap nhieu lan so voi phat hien o UI. UI Test giu o 20% vi framework Selenium POM da xay san, tap trung vao happy path va critical user journey. Security Test chi 5% nhung khong the cat vi du lieu tai chinh nguoi dung la tai san can bao ve tuyet doi.

Ngoai ti le phan bo, team se ap dung nguyen tac test pyramid. Unit test duoc viet song song voi code va la lop phong thu dau tien. API test la lop xac nhan nghiep vu chinh va response contract. UI test chi duoc dung cho nhung luong co gia tri thuc su, tranh bien Selenium thanh noi giam tat ca bug. Performance va security test du co ti le nho hon van la lop kiem soat bat buoc truoc production, dac biet trong boi canh thanh toan VPBank can tinh on dinh va kha nang chong loi khi he thong co tai cao hoac gap dau noi khong on dinh.

---

## 3. Definition of Done (DoD) - Khi nao thi "da test xong"?

Mot tinh nang duoc coi la **DONE** khi dap ung **toan bo** tieu chi sau:

**Tieu chi ky thuat:**
- [ ] Smoke test: 100% PASS tren moi truong staging
- [ ] Regression test: >= 95% PASS (toi da 5% skip co ly do duoc ghi nhan)
- [ ] Code coverage >= 80% do bang JaCoCo (ap dung cho backend)
- [ ] Khong co bug **P1 - Blocker** nao o trang thai Open
- [ ] Khong co bug **P2 - Critical** nao chua co ke hoach xu ly (phai co assignee + deadline)
- [ ] Allure Report da duoc team review va QA Lead ky duyet

**Tieu chi quy trinh:**
- [ ] Pull Request da co it nhat 1 reviewer approve
- [ ] Pipeline CI xanh tren nhanh target
- [ ] Test evidence (screenshot/log) da duoc dinh kem vao ticket Jira
- [ ] Release note da duoc PM xac nhan

DoD o day duoc dinh nghia de team tranh tinh trang "dev xong roi moi test" hoac "pass local la du". Mot tinh nang chua the goi la xong neu van con bug P1, neu pipeline do, hoac neu khong co bang chung kiem thu. Allure report va evidence trong Jira la mot phan cua chat luong, khong phai phu luc. Quy trinh nay giup moi thanh vien co chung dinh nghia ve chat luong dau ra va giam tranh cai khi den sat ngay release.

---

## 4. Quan ly rui ro

| Rui ro | Xac suat | Tac dong | Ke hoach giam thieu |
|--------|----------|----------|-------------------|
| Sandbox VPBank khong on dinh | Cao | Khong test duoc thanh toan - block toan bo Sprint 5 | Lien he VPBank truoc Sprint 2 tuan; xay mock API du phong (WireMock) de unblock test offline |
| Staging data bi xoa dot xuat | Trung binh | Phai tao lai test data - mat 4-8 gio | Script tu dong seed test data truoc moi test run; backup snapshot database staging hang dem |
| API 3rd party (VPBank, email) bi down | Trung binh | Test E2E bi block, khong the xac nhan luong end-to-end | Mock service (WireMock/MockServer) cho test offline; monitor uptime sandbox bang UptimeRobot |
| CI server het disk space | Thap | Pipeline khong chay duoc - toan bo team bi block | Tu dong xoa artifact cu hang tuan bang cron; gioi han `retention-days: 7` cho allure-results |
| Developer push code chua test len main | Trung binh | Regression test fail, mat thoi gian debug | Branch protection rule: require 1 review + CI green truoc khi merge; smoke test chay tu dong tren moi PR |
| QA bottleneck (chi co 1 nguoi) | Cao | Sprint bi delay neu QA benh hoac overload | Automation first - uu tien viet script thay vi test tay; developer tu chay smoke test truoc khi ban giao |

Quan ly rui ro khong dung lai o viec liet ke. Moi rui ro phai co owner, ke hoach fallback va thoi diem xu ly truoc khi no tro thanh su co. Voi ShopEasy, rui ro lon nhat khong nam o ky thuat UI ma o phu thuoc external system. Vi vay, chien luoc uu tien mock, monitor, seed data, va branch protection de giam toi da rui ro block ca sprint. Khi rui ro da duoc xu ly chu dong, QA co the tap trung vao xac nhan chat luong thay vi chay theo su co.

---

## 5. Lich trinh kiem thu

| Loai test | Khi nao chay | Thoi gian | Trigger | Moi truong |
|-----------|-------------|-----------|---------|-----------|
| Smoke Test | Sau moi commit vao main/develop | ~5 phut | Tu dong - GitHub Actions (push) | Staging |
| Regression Test | Hang dem 2:00 AM | ~45 phut | Cron schedule (`0 2 * * 1-5`) | Staging |
| Performance Test | Chu nhat hang tuan | ~2 gio | Thu cong hoac cron (`0 22 * * 0`) | Staging clone |
| Security Scan (OWASP ZAP) | Truoc moi release | ~3 gio | Thu cong - QA Lead trigger | Staging |
| UAT | Cuoi moi Sprint (ngay 9-10) | 2-3 ngay | Thu cong, co PO va dai dien user tham gia | Staging |
| Hotfix Smoke | Sau moi hotfix deploy | ~5 phut | Tu dong - GitHub Actions | Production (readonly) |

**Nguyen tac:** Test som nhat co the (shift-left). Moi loi phat hien o unit/API test deu re hon 10-100x so voi phat hien o UAT hoac production.

Lich trinh nay can bang giua toc do phan hoi va chi phi thuc thi. Smoke test phai nhanh de bao ve branch chinh. Regression chay ban dem de giam tac dong den nhan luc ban ngay. UAT va security scan du ton thoi gian hon nhung dong vai tro quyet dinh truoc release. Khi gan release Sprint 5, team se uu tien luong thanh toan VPBank va cac test case lien quan den trang thai don hang, email xac nhan, va tinh toan tra gop, vi do la khu vuc co nguy co gay ton that tai chinh va anh huong den long tin nguoi dung cao nhat.