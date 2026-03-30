<?php

declare(strict_types=1);

$pageTitle = 'Welcome';
$activePage = 'home';
require_once __DIR__ . '/includes/header.php';
?>
<div class="p-5 mb-4 bg-white rounded-3 shadow-sm">
    <div class="container-fluid py-3">
        <h1 class="display-6 fw-bold">Modernized CRM Starter</h1>
        <p class="col-md-8 fs-5 mb-4">
            This rebuild keeps a lightweight procedural PHP stack with MySQLi, strong security defaults,
            and a modular file structure suitable for shared hosting.
        </p>
        <?php if (!is_logged_in()): ?>
            <a class="btn btn-primary btn-lg" href="<?= e(APP_URL); ?>/pages/register.php">Create account</a>
        <?php else: ?>
            <a class="btn btn-primary btn-lg" href="<?= e(APP_URL); ?>/pages/dashboard.php">Open dashboard</a>
        <?php endif; ?>
    </div>
</div>
<?php require_once __DIR__ . '/includes/footer.php'; ?>
