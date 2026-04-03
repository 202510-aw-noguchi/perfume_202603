# ワークショップ予約・メルマガ管理 API（Spring Boot）

`reservation-app` は、Perfume_202603 の予約・メルマガ機能を提供する Spring Boot バックエンドです。

- ワークショップ申込画面: `../workshop/registration.html`
- メルマガ登録/解除画面: `../mail.html`
- 管理画面: `src/main/resources/static/admin.html`
- ログイン画面: `src/main/resources/static/login.html`

## 技術スタック

- Java 17
- Spring Boot 3.4.4
- Spring Web / Validation / Security
- Spring Data JPA
- H2 Database（ファイルDB）

## 起動方法（ローカル）

1. `reservation-app` ディレクトリへ移動
2. アプリ起動

```bash
mvn spring-boot:run
```

Windows で Maven 未導入の場合は Maven Wrapper でも起動できます。

```bash
.\mvnw.cmd spring-boot:run
```

## 主要URL

- API ベース: `http://localhost:8080/api`
- 管理画面: `http://localhost:8080/admin.html`
- ログイン画面: `http://localhost:8080/login.html`
- H2 コンソール: `http://localhost:8080/h2-console`

## API 一覧

### 公開API

- `GET /api/reservations/availability`
  - Query: `course`, `from`（yyyy-MM-dd）, `to`（yyyy-MM-dd）
- `POST /api/reservations`
  - Body:

```json
{
  "course": "A",
  "reserveDate": "2026-04-10",
  "reserveTime": "14:00-15:00",
  "participantCount": 2,
  "contactName": "Taro Yamada",
  "contactPhone": "09012345678",
  "contactEmail": "taro@example.com"
}
```

- `POST /api/mail/subscriptions/register`
- `POST /api/mail/subscriptions/unsubscribe`
  - Body（共通）:

```json
{
  "email": "user@example.com"
}
```

- `GET /api/csrf-token`

### 管理API（認証必須）

- `GET /api/admin/me`
- `GET /api/admin/reservations`
- `GET /api/admin/reservation-summary`
- `GET /api/admin/mail-subscriptions`
- `GET /api/admin/mail-summary`

## ログイン情報

デフォルト値:

- ユーザー名: `admin`
- パスワード: `admin12345`
- 権限: `ADMIN`

admin 権限以外のログインID/パスワードは、現状このアプリには定義されていません。
（`InMemoryUserDetailsManager` に `ADMIN` ユーザー1件のみ登録）

変更する場合は `src/main/resources/application.properties` の以下を更新してください。

- `app.admin.username`
- `app.admin.password`

## データベース

H2 ファイルDB（`./data/reservationdb`）を使用します。

- JDBC URL: `jdbc:h2:file:./data/reservationdb;MODE=PostgreSQL;DATABASE_TO_UPPER=false;AUTO_SERVER=TRUE`
- H2コンソールURL: `http://localhost:8080/h2-console`

### 満席制御について

`slot_capacity` テーブルで日時ごとの上限を設定すると、予約可否に反映されます。
未設定の枠は上限 `20` として扱われます。

```sql
insert into slot_capacity (reserve_date, reserve_time, capacity)
values ('2026-04-01', '14:00-15:00', 0);
```

## Docker 実行

`Dockerfile` ではアプリを `10000` ポートで公開します。

```bash
docker build -t reservation-app .
docker run --rm -p 10000:10000 reservation-app
```
