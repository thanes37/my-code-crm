<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    redirect('pages/register.php');
}

if (!csrf_validate($_POST['csrf_token'] ?? null)) {
    set_flash('danger', 'Invalid request token. Please try again.');
    redirect('pages/register.php');
}

$name = post_string('name');
$email = post_email('email');
$password = (string)($_POST['password'] ?? '');

if ($name === '' || strlen($name) < 2 || !filter_var($email, FILTER_VALIDATE_EMAIL) || strlen($password) < 8) {
    set_flash('danger', 'Please provide valid registration details.');
    redirect('pages/register.php');
}

$stmt = $db->prepare('SELECT id FROM users WHERE email = ? LIMIT 1');
$stmt->bind_param('s', $email);
$stmt->execute();
if ($stmt->get_result()->fetch_assoc()) {
    set_flash('warning', 'An account with this email already exists.');
    redirect('pages/register.php');
}

$passwordHash = password_hash($password, PASSWORD_DEFAULT);
$role = 'user';

$insert = $db->prepare('INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)');
$insert->bind_param('ssss', $name, $email, $passwordHash, $role);
$insert->execute();

$_SESSION['user_id'] = (int)$insert->insert_id;
session_regenerate_id(true);

set_flash('success', 'Registration successful. Welcome!');
redirect('pages/dashboard.php');
