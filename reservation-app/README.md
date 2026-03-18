# 受付予約システム（Java バックエンド）

## 構成
- フロント: `workshop/registration.html`
- バックエンド: `reservation-app` (Spring Boot + H2)

## 起動
1. `reservation-app` を IDE で開く
2. `com.example.reservation.ReservationApplication` を実行
3. API: `http://localhost:8080/api`

## 仕様
- 手順2（日付）: 全時間帯が満席の日はボタンを無効化（受付対象外）
- 手順3（時間）: 満席の時間帯はラジオボタンを無効化（受付対象外）
- `予約内容を確定する` で予約登録し、予約番号を画面表示
- 管理画面: `http://localhost:8080/admin.html`（ログイン必須）

## 管理画面ログイン
- ユーザー名: `admin`
- パスワード: `admin12345`
- 設定変更: `src/main/resources/application.properties` の `app.admin.*`

## 満席データの設定例（H2）
H2 コンソール: `http://localhost:8080/h2-console`

```sql
-- 例: 2026-04-01 14:00 を満席扱いにする（定員0）
insert into slot_capacity (reserve_date, reserve_time, capacity)
values ('2026-04-01', '14:00-15:00', 0);
```

`slot_capacity` 未設定の枠は定員 20 として扱います。
