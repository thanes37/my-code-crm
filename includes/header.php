<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/config.php';

$pageTitle = $pageTitle ?? APP_NAME;
$activePage = $activePage ?? '';
$flash = get_flash();
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="csrf-token" content="<?= e(csrf_token()); ?>">
    <title><?= e($pageTitle); ?> - <?= e(APP_NAME); ?></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="<?= e(APP_URL); ?>/assets/css/app.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="<?= e(APP_URL); ?>/index.php"><?= e(APP_NAME); ?></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link <?= $activePage === 'home' ? 'active' : ''; ?>" href="<?= e(APP_URL); ?>/index.php">Home</a>
                </li>
                <?php if (is_logged_in()): ?>
                    <li class="nav-item">
                        <a class="nav-link <?= $activePage === 'dashboard' ? 'active' : ''; ?>" href="<?= e(APP_URL); ?>/pages/dashboard.php">Dashboard</a>
                    </li>
                <?php endif; ?>
            </ul>
            <ul class="navbar-nav ms-auto">
                <?php if (is_logged_in()): ?>
                    <li class="nav-item me-2 d-flex align-items-center text-white-50 small">Signed in</li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light btn-sm" href="<?= e(APP_URL); ?>/process/logout.php">Logout</a>
                    </li>
                <?php else: ?>
                    <li class="nav-item me-2">
                        <a class="btn btn-outline-light btn-sm" href="<?= e(APP_URL); ?>/pages/login.php">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-primary btn-sm" href="<?= e(APP_URL); ?>/pages/register.php">Register</a>
                    </li>
                <?php endif; ?>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
    <?php if ($flash): ?>
        <div class="alert alert-<?= e($flash['type']); ?> alert-dismissible fade show" role="alert">
            <?= e($flash['message']); ?>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <?php endif; ?>
