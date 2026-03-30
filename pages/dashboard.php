<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/config.php';
require_auth();

$user = current_user($db);
$pageTitle = 'Dashboard';
$activePage = 'dashboard';
require_once __DIR__ . '/../includes/header.php';
?>
<div class="row g-3">
    <div class="col-lg-8">
        <div class="card shadow-sm">
            <div class="card-body">
                <h1 class="h4">Welcome, <?= e($user['name'] ?? 'User'); ?></h1>
                <p class="mb-0 text-muted">You are authenticated with secure session and CSRF-protected forms.</p>
            </div>
        </div>
    </div>
    <div class="col-lg-4">
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="h6 text-uppercase text-muted">Account</h2>
                <ul class="list-unstyled mb-0 small">
                    <li><strong>Email:</strong> <?= e($user['email'] ?? ''); ?></li>
                    <li><strong>Role:</strong> <?= e($user['role'] ?? 'user'); ?></li>
                    <li><strong>Joined:</strong> <?= e($user['created_at'] ?? ''); ?></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<?php require_once __DIR__ . '/../includes/footer.php'; ?>
