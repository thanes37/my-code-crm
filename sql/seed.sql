-- Seed data for development/testing.
-- Default admin password: ChangeMe123!

INSERT INTO users (name, email, password_hash, role, status)
VALUES
    ('System Admin', 'admin@example.com', '$2y$10$37wXYYt8pd8acTDhR7HdbeDQmtgS9DNUERDASJmQw95E1bYQW88vW', 'admin', 'active')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    role = VALUES(role),
    status = VALUES(status);

INSERT INTO tags (name, color_hex)
VALUES
    ('VIP', '#D63384'),
    ('At Risk', '#DC3545'),
    ('Upsell', '#0D6EFD')
ON DUPLICATE KEY UPDATE color_hex = VALUES(color_hex);
