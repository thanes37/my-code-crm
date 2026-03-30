<?php

declare(strict_types=1);

/**
 * Global application configuration.
 *
 * This file centralizes database credentials, security defaults,
 * and mysqli bootstrap behavior.
 */

// Environment-specific values should come from server env vars in production.
const APP_NAME = 'My CRM';
const APP_URL = 'http://localhost';
const DB_HOST = '127.0.0.1';
const DB_PORT = 3306;
const DB_NAME = 'my_crm';
const DB_USER = 'root';
const DB_PASS = '';
const SESSION_NAME = 'mycrm_session';

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

if (session_status() === PHP_SESSION_NONE) {
    session_name(SESSION_NAME);
    session_set_cookie_params([
        'lifetime' => 0,
        'path' => '/',
        'domain' => '',
        'secure' => !empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off',
        'httponly' => true,
        'samesite' => 'Lax',
    ]);
    session_start();
}

require_once __DIR__ . '/../includes/functions.php';

$db = db_connect();
