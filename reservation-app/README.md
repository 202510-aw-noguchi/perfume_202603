# 予約・メルマガ管理 API（Spring Boot）

## 概要
- バックエンド: `reservation-app`（Spring Boot + H2）
- 連携フロント:
1. ワークショップ予約: `../workshop/registration.html`
2. メルマガ登録/解除: `../mail.html`

## 起動方法
1. `reservation-app` ディレクトリへ移動
2. アプリ起動
```bash
mvn spring-boot:run
```
3. 稼働確認
- API ベース: `http://localhost:8080/api`
- 管理画面: `http://localhost:8080/admin.html`
- ログイン画面: `http://localhost:8080/login.html`
- H2 コンソール: `http://localhost:8080/h2-console`

## 主な API
### 予約
- `GET /api/reservations/availability`
- `POST /api/reservations`

### メルマガ
- `POST /api/mail/subscriptions/register`
- `POST /api/mail/subscriptions/unsubscribe`

### 管理（要ログイン）
- `GET /api/admin/me`
- `GET /api/admin/reservations`
- `GET /api/admin/reservation-summary`
- `GET /api/admin/mail-subscriptions`
- `GET /api/admin/mail-summary`

## 管理画面ログイン
- ユーザー名: `admin`
- パスワード: `admin12345`
- 変更箇所: `src/main/resources/application.properties` の `app.admin.username` / `app.admin.password`

## 空き枠・定員の考え方
- `slot_capacity` テーブルに設定した枠はその定員を採用
- 未設定の枠は定員 `20` として扱う

満席設定例:
```sql
insert into slot_capacity (reserve_date, reserve_time, capacity)
values ('2026-04-01', '14:00-15:00', 0);
```
