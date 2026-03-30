# My CRM (Procedural PHP + MySQLi)

## Structure

- `config/config.php` - app constants, secure session bootstrap, DB initialization.
- `includes/functions.php` - reusable helpers (DB, auth, CSRF, escaping, flash, redirects).
- `includes/header.php` / `includes/footer.php` - shared layout and Bootstrap 5 wiring.
- `pages/` - public UI pages.
- `process/` - POST handlers (login/register/logout).
- `assets/` - static CSS/JS.
- `sql/schema.sql` - full backend CRM schema.
- `sql/seed.sql` - development seed data.

## Database Modules Included

- Auth: `users`, `password_resets`, optional `sessions`.
- CRM Core: `customers`, `contacts`, `contact_notes`.
- Sales Pipeline: `deals`, `deal_stage_history`.
- Work Tracking: `activities`, `activity_comments`.
- Taxonomy: `tags`, `customer_tags`.
- Auditing: `audit_logs`.

## Setup

1. Create database `my_crm`.
2. Run `sql/schema.sql`.
3. (Optional) Run `sql/seed.sql`.
4. Update DB credentials and `APP_URL` in `config/config.php`.

## Notes

The original repository did not contain `header.php`, `footer.php`, or `config.php` website files.
This implementation provides a production-ready baseline aligned with requested architecture and security requirements.
