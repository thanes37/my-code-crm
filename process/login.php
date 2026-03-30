<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    redirect('pages/login.php');
}

if (!csrf_validate($_POST['csrf_token'] ?? null)) {
    set_flash('danger', 'Invalid request token. Please try again.');
    redirect('pages/login.php');
}

$email = post_email('email');
$password = (string)($_POST['password'] ?? '');

if (!filter_var($email, FILTER_VALIDATE_EMAIL) || $password === '') {
    set_flash('danger', 'Invalid email or password.');
    redirect('pages/login.php');
}

$stmt = $db->prepare('SELECT id, password_hash FROM users WHERE email = ? LIMIT 1');
$stmt->bind_param('s', $email);
$stmt->execute();
$user = $stmt->get_result()->fetch_assoc();

if (!$user || !password_verify($password, $user['password_hash'])) {
    set_flash('danger', 'Invalid email or password.');
    redirect('pages/login.php');
}

$_SESSION['user_id'] = (int)$user['id'];
session_regenerate_id(true);

set_flash('success', 'You are now signed in.');
redirect('pages/dashboard.php');
