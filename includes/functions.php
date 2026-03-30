<?php

declare(strict_types=1);

/**
 * Creates and returns a MySQLi connection.
 */
function db_connect(): mysqli
{
    static $connection = null;

    if ($connection instanceof mysqli) {
        return $connection;
    }

    $connection = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME, DB_PORT);
    $connection->set_charset('utf8mb4');

    return $connection;
}

/**
 * Escapes output for HTML context.
 */
function e(string $value): string
{
    return htmlspecialchars($value, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
}

/**
 * Generates and stores CSRF token.
 */
function csrf_token(): string
{
    if (empty($_SESSION['csrf_token'])) {
        $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
    }

    return $_SESSION['csrf_token'];
}

/**
 * Verifies submitted CSRF token.
 */
function csrf_validate(?string $token): bool
{
    return is_string($token)
        && !empty($_SESSION['csrf_token'])
        && hash_equals($_SESSION['csrf_token'], $token);
}

/**
 * Redirect helper.
 */
function redirect(string $path): never
{
    header('Location: ' . APP_URL . '/' . ltrim($path, '/'));
    exit;
}

/**
 * Flash messaging for form feedback.
 */
function set_flash(string $type, string $message): void
{
    $_SESSION['flash'] = ['type' => $type, 'message' => $message];
}

function get_flash(): ?array
{
    if (empty($_SESSION['flash'])) {
        return null;
    }

    $flash = $_SESSION['flash'];
    unset($_SESSION['flash']);

    return $flash;
}

/**
 * Auth guard utilities.
 */
function is_logged_in(): bool
{
    return !empty($_SESSION['user_id']);
}

function require_auth(): void
{
    if (!is_logged_in()) {
        set_flash('warning', 'Please sign in to continue.');
        redirect('pages/login.php');
    }
}

function current_user(mysqli $db): ?array
{
    if (!is_logged_in()) {
        return null;
    }

    $stmt = $db->prepare('SELECT id, name, email, role, created_at FROM users WHERE id = ? LIMIT 1');
    $stmt->bind_param('i', $_SESSION['user_id']);
    $stmt->execute();
    $result = $stmt->get_result();

    return $result->fetch_assoc() ?: null;
}

/**
 * Basic input sanitizers.
 */
function post_string(string $key): string
{
    $value = trim((string)($_POST[$key] ?? ''));
    return preg_replace('/\s+/', ' ', $value) ?? '';
}



/**
 * Generic prepared SELECT returning all rows.
 */
function db_select_all(mysqli $db, string $sql, string $types = '', array $params = []): array
{
    $stmt = $db->prepare($sql);
    if ($types !== '' && $params !== []) {
        $stmt->bind_param($types, ...$params);
    }
    $stmt->execute();

    return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
}

/**
 * Generic prepared execute for INSERT/UPDATE/DELETE.
 */
function db_execute(mysqli $db, string $sql, string $types = '', array $params = []): int
{
    $stmt = $db->prepare($sql);
    if ($types !== '' && $params !== []) {
        $stmt->bind_param($types, ...$params);
    }
    $stmt->execute();

    return $stmt->affected_rows;
}

/**
 * Transaction wrapper for grouped write operations.
 */
function db_transaction(mysqli $db, callable $callback): mixed
{
    $db->begin_transaction();

    try {
        $result = $callback($db);
        $db->commit();
        return $result;
    } catch (Throwable $e) {
        $db->rollback();
        throw $e;
    }
}
function post_email(string $key): string
{
    return strtolower(trim((string)($_POST[$key] ?? '')));
}
