<?php 
$title = '添加管理员';
$pageTitle = '添加管理员';
$currentMenu = 'admin';
$breadcrumb = [
    ['name' => '管理员', 'url' => '/admin/admin'],
    ['name' => '添加管理员']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">添加管理员</h3>
    </div>
    <form action="/admin/admin/add" method="POST">
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>用户名 <span class="text-danger">*</span></label>
                        <input type="text" name="username" class="form-control" autocomplete="off" required placeholder="请输入用户名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>密码 <span class="text-danger">*</span></label>
                        <input type="password" name="password" class="form-control" autocomplete="off" required placeholder="请输入密码">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>姓名</label>
                        <input type="text" name="name" class="form-control" autocomplete="off" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>邮箱</label>
                        <input type="email" name="email" class="form-control" autocomplete="off" placeholder="请输入邮箱">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>超级管理员</label>
                        <select name="is_super" class="form-control">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>状态</label>
                        <select name="status" class="form-control">
                            <option value="1">正常</option>
                            <option value="0">禁用</option>
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
