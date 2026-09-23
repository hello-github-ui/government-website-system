<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-md-6 mx-auto">
            <div class="card">
                <div class="card-header bg-success text-white">
                    <h4 class="mb-0">用户注册</h4>
                </div>
                <div class="card-body p-4">
                    <form action="/login/doRegister" method="POST">
                        <div class="mb-3">
                            <label class="form-label">用户名 <span class="text-danger">*</span></label>
                            <input type="text" name="username" autocomplete="off" class="form-control" required placeholder="请输入用户名">
                            <div class="form-text">用户名长度为3-20个字符</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">密码 <span class="text-danger">*</span></label>
                            <input type="password" name="password" autocomplete="off" class="form-control" required placeholder="请输入密码">
                            <div class="form-text">密码长度至少6位</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">确认密码 <span class="text-danger">*</span></label>
                            <input type="password" autocomplete="off" name="confirm_password" class="form-control" required placeholder="请再次输入密码">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">真实姓名</label>
                            <input type="text" name="real_name" autocomplete="off" class="form-control" placeholder="请输入真实姓名">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">手机号</label>
                            <input type="tel" name="phone" autocomplete="off" class="form-control" placeholder="请输入手机号">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">邮箱</label>
                            <input type="email" name="email" autocomplete="off" class="form-control" placeholder="请输入邮箱地址">
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" autocomplete="off" id="agree" name="agree" required>
                            <label class="form-check-label" for="agree">
                                我已阅读并同意 <a href="#" target="_blank">用户协议</a> 和 <a href="#" target="_blank">隐私政策</a>
                            </label>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-success">注册</button>
                        </div>
                    </form>
                    
                    <hr class="my-4">
                    
                    <div class="text-center">
                        <p class="mb-0">已有账号？ <a href="/login">立即登录</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
