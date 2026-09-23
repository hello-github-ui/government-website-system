<?php 
$title = '编辑用户';
$pageTitle = '编辑用户';
$currentMenu = 'user';
$breadcrumb = [
    ['name' => '前台用户', 'url' => '/admin/user'],
    ['name' => '编辑用户']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">编辑用户</h3>
    </div>
    <form action="/admin/user/edit?id=<?php echo $info['id']; ?>" method="POST">
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>用户名</label>
                        <input type="text" class="form-control" autocomplete="off" value="<?php echo htmlspecialchars($info['username']); ?>" disabled>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>密码 <small class="text-muted">（留空则不修改）</small></label>
                        <input type="password" name="password" autocomplete="off" class="form-control" placeholder="请输入新密码">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>姓名</label>
                        <input type="text" name="name" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($info['name'] ?? ''); ?>" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>邮箱</label>
                        <input type="email" name="email" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($info['email'] ?? ''); ?>" placeholder="请输入邮箱">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>手机</label>
                        <input type="text" name="phone" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($info['phone'] ?? ''); ?>" placeholder="请输入手机号">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>状态</label>
                        <select name="status" class="form-control">
                            <option value="1" <?php echo $info['status'] == 1 ? 'selected' : ''; ?>>正常</option>
                            <option value="0" <?php echo $info['status'] == 0 ? 'selected' : ''; ?>>禁用</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-footer">
            <button type="submit" class="btn btn-primary">保存</button>
            <a href="/admin/user" class="btn btn-default">返回</a>
        </div>
    </form>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
