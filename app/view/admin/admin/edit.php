<?php 
$title = '编辑管理员';
$pageTitle = '编辑管理员';
$currentMenu = 'admin';
$breadcrumb = [
    ['name' => '管理员', 'url' => '/admin/admin'],
    ['name' => '编辑管理员']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">编辑管理员</h3>
    </div>
    <form action="/admin/admin/edit?id=<?php echo $info['id']; ?>" method="POST">
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
                        <input type="password" name="password" class="form-control" placeholder="请输入新密码">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>姓名</label>
                        <input type="text" name="name" class="form-control" value="<?php echo htmlspecialchars($info['name'] ?? ''); ?>" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>邮箱</label>
                        <input type="email" name="email" class="form-control" value="<?php echo htmlspecialchars($info['email'] ?? ''); ?>" placeholder="请输入邮箱">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>超级管理员</label>
                        <select name="is_super" class="form-control">
                            <option value="0" <?php echo $info['is_super'] == 0 ? 'selected' : ''; ?>>否</option>
                            <option value="1" <?php echo $info['is_super'] == 1 ? 'selected' : ''; ?>>是</option>
                        </select>
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
            <a href="/admin/admin" class="btn btn-default">返回</a>
        </div>
    </form>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
