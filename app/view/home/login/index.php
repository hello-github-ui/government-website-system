<?php include APP_PATH . 'view/home/public/header.php'; ?>

<!-- 登录页面 -->
<section class="auth-section py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="auth-card">
                    <div class="auth-header text-center">
                        <div class="auth-icon">
                            <i class="fas fa-user-circle"></i>
                        </div>
                        <h3 class="auth-title">欢迎回来</h3>
                        <p class="auth-subtitle">登录您的账户以继续</p>
                    </div>
                    
                    <div class="auth-body">
                        <form action="/login/doLogin" method="POST" class="auth-form">
                            <div class="form-floating mb-3">
                                <input type="text" name="username" class="form-control" id="username" placeholder="用户名" autocomplete="off" required>
                                <label for="username"><i class="fas fa-user me-2 text-muted"></i>用户名</label>
                            </div>
                            
                            <div class="form-floating mb-4">
                                <input type="password" name="password" class="form-control" id="password" autocomplete="off" placeholder="密码" required>
                                <label for="password"><i class="fas fa-lock me-2 text-muted"></i>密码</label>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-auth btn-lg">
                                    <i class="fas fa-sign-in-alt me-2"></i>立即登录
                                </button>
                            </div>
                        </form>
                        
                        <div class="auth-divider">
                            <span>还没有账号？</span>
                        </div>
                        
                        <div class="text-center">
                            <a href="/login/register" class="btn btn-outline-auth">
                                <i class="fas fa-user-plus me-2"></i>创建新账户
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
