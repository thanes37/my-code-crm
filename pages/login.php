<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/config.php';
if (is_logged_in()) {
    redirect('pages/dashboard.php');
}

$pageTitle = 'Login';
require_once __DIR__ . '/../includes/header.php';
?>
<div class="row justify-content-center">
    <div class="col-md-6 col-lg-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h2 class="h4 mb-3">Sign in</h2>
                <form method="post" action="<?= e(APP_URL); ?>/process/login.php" novalidate>
                    <input type="hidden" name="csrf_token" value="<?= e(csrf_token()); ?>">

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" maxlength="190" required>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>
            </div>
        </div>
    </div>
</div>
<?php require_once __DIR__ . '/../includes/footer.php'; ?>
