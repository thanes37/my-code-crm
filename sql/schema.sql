-- My CRM full backend schema (MySQL 8+, InnoDB, utf8mb4).
-- This expands the initial auth-only schema into core CRM entities.

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS activity_comments;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS deal_stage_history;
DROP TABLE IF EXISTS deals;
DROP TABLE IF EXISTS contact_notes;
DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS customer_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS password_resets;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------------
-- Users and auth support
-- ---------------------------------------------------------------------------
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'manager', 'sales', 'support', 'user') NOT NULL DEFAULT 'user',
    status ENUM('active', 'disabled') NOT NULL DEFAULT 'active',
    last_login_at DATETIME NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE password_resets (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    reset_token_hash CHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_resets_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uq_reset_token_hash (reset_token_hash),
    KEY idx_password_resets_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Optional server-side session storage table (if enabled in PHP INI).
CREATE TABLE sessions (
    id VARCHAR(128) PRIMARY KEY,
    user_id INT UNSIGNED NULL,
    ip_address VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,
    payload MEDIUMBLOB NOT NULL,
    last_activity INT UNSIGNED NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sessions_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    KEY idx_sessions_last_activity (last_activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- CRM domain entities
-- ---------------------------------------------------------------------------
CREATE TABLE customers (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    owner_user_id INT UNSIGNED NOT NULL,
    company_name VARCHAR(180) NOT NULL,
    industry VARCHAR(120) NULL,
    website VARCHAR(255) NULL,
    phone VARCHAR(40) NULL,
    email VARCHAR(190) NULL,
    billing_address VARCHAR(255) NULL,
    city VARCHAR(120) NULL,
    state VARCHAR(120) NULL,
    postal_code VARCHAR(20) NULL,
    country VARCHAR(120) NULL,
    status ENUM('lead', 'prospect', 'active', 'inactive', 'churned') NOT NULL DEFAULT 'lead',
    source ENUM('web', 'referral', 'email', 'phone', 'ads', 'event', 'other') NOT NULL DEFAULT 'other',
    annual_revenue DECIMAL(15,2) NULL,
    employee_count INT UNSIGNED NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_customers_owner
        FOREIGN KEY (owner_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    KEY idx_customers_status (status),
    KEY idx_customers_owner (owner_user_id),
    KEY idx_customers_company_name (company_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE contacts (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT UNSIGNED NOT NULL,
    first_name VARCHAR(120) NOT NULL,
    last_name VARCHAR(120) NOT NULL,
    job_title VARCHAR(120) NULL,
    email VARCHAR(190) NULL,
    phone VARCHAR(40) NULL,
    is_primary TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_contacts_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    KEY idx_contacts_customer (customer_id),
    KEY idx_contacts_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE contact_notes (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    contact_id BIGINT UNSIGNED NOT NULL,
    author_user_id INT UNSIGNED NOT NULL,
    note_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contact_notes_contact
        FOREIGN KEY (contact_id) REFERENCES contacts(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_contact_notes_author
        FOREIGN KEY (author_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    KEY idx_contact_notes_contact (contact_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE deals (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT UNSIGNED NOT NULL,
    owner_user_id INT UNSIGNED NOT NULL,
    title VARCHAR(180) NOT NULL,
    description TEXT NULL,
    amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    stage ENUM('new', 'qualified', 'proposal', 'negotiation', 'won', 'lost') NOT NULL DEFAULT 'new',
    probability TINYINT UNSIGNED NOT NULL DEFAULT 0,
    expected_close_date DATE NULL,
    closed_at DATETIME NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_deals_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_deals_owner
        FOREIGN KEY (owner_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CHECK (probability <= 100),
    KEY idx_deals_stage (stage),
    KEY idx_deals_owner (owner_user_id),
    KEY idx_deals_expected_close_date (expected_close_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE deal_stage_history (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    deal_id BIGINT UNSIGNED NOT NULL,
    from_stage ENUM('new', 'qualified', 'proposal', 'negotiation', 'won', 'lost') NULL,
    to_stage ENUM('new', 'qualified', 'proposal', 'negotiation', 'won', 'lost') NOT NULL,
    changed_by_user_id INT UNSIGNED NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_deal_stage_history_deal
        FOREIGN KEY (deal_id) REFERENCES deals(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_deal_stage_history_user
        FOREIGN KEY (changed_by_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    KEY idx_deal_stage_history_deal (deal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE activities (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT UNSIGNED NOT NULL,
    contact_id BIGINT UNSIGNED NULL,
    deal_id BIGINT UNSIGNED NULL,
    assigned_user_id INT UNSIGNED NOT NULL,
    created_by_user_id INT UNSIGNED NOT NULL,
    activity_type ENUM('call', 'email', 'meeting', 'task', 'note', 'demo') NOT NULL,
    subject VARCHAR(190) NOT NULL,
    details TEXT NULL,
    due_at DATETIME NULL,
    completed_at DATETIME NULL,
    priority ENUM('low', 'normal', 'high') NOT NULL DEFAULT 'normal',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_activities_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_activities_contact
        FOREIGN KEY (contact_id) REFERENCES contacts(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_activities_deal
        FOREIGN KEY (deal_id) REFERENCES deals(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_activities_assigned
        FOREIGN KEY (assigned_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_activities_created_by
        FOREIGN KEY (created_by_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    KEY idx_activities_due_at (due_at),
    KEY idx_activities_assigned_user (assigned_user_id),
    KEY idx_activities_customer (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE activity_comments (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT UNSIGNED NOT NULL,
    author_user_id INT UNSIGNED NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_comments_activity
        FOREIGN KEY (activity_id) REFERENCES activities(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_activity_comments_author
        FOREIGN KEY (author_user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    KEY idx_activity_comments_activity (activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tags (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    color_hex CHAR(7) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_tags_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE customer_tags (
    customer_id BIGINT UNSIGNED NOT NULL,
    tag_id INT UNSIGNED NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id, tag_id),
    CONSTRAINT fk_customer_tags_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_customer_tags_tag
        FOREIGN KEY (tag_id) REFERENCES tags(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Auditing
-- ---------------------------------------------------------------------------
CREATE TABLE audit_logs (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NULL,
    entity_type VARCHAR(80) NOT NULL,
    entity_id BIGINT UNSIGNED NULL,
    action ENUM('create', 'update', 'delete', 'login', 'logout', 'other') NOT NULL,
    changes_json JSON NULL,
    ip_address VARCHAR(45) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    KEY idx_audit_logs_entity (entity_type, entity_id),
    KEY idx_audit_logs_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
