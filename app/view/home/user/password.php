<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-md-3">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">个人中心</h5>
                </div>
                <div class="list-group list-group-flush">
                    <a href="/user/profile" class="list-group-item list-group-item-action">基本信息</a>
                    <a href="/user/password" class="list-group-item list-group-item-action active">修改密码</a>
                    <a href="/logout" class="list-group-item list-group-item-action text-danger">退出登录</a>
                </div>
            </div>
        </div>
        
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">修改密码</h5>
                </div>
                <div class="card-body">
                    <form action="/user/password" method="POST">
                        <div class="mb-3">
                            <label class="form-label">原密码</label>
                            <input type="password" name="old_password" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">新密码</label>
                            <input type="password" name="new_password" autocomplete="off" class="form-control" required minlength="6">
                            <div class="form-text">密码长度至少6位</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">确认新密码</label>
                            <input type="password" autocomplete="off" name="confirm_password" class="form-control" required>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">保存修改</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
