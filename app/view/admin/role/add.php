<?php include APP_PATH . 'view/admin/public/header.php'; ?>

<div class="container-fluid">
    <div class="row">
        <?php include APP_PATH . 'view/admin/public/sidebar.php'; ?>
        
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">添加角色</h1>
            </div>
            
            <div class="card">
                <div class="card-body">
                    <form action="/admin/role/add" method="post">
                        <div class="mb-3">
                            <label class="form-label">角色名称 <span class="text-danger">*</span></label>
                            <input type="text" name="role_name" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">描述</label>
                            <textarea name="role_desc" class="form-control" rows="2"></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">权限设置</label>
                            <div class="card">
                                <div class="card-body">
                                    <?php foreach ($permissions as $groupKey => $group): ?>
                                    <div class="mb-3">
                                        <h6 class="fw-bold"><?= $group['name'] ?></h6>
                                        <div class="row">
                                            <?php foreach ($group['permissions'] as $permKey => $permName): ?>
                                            <div class="col-md-4">
                                                <div class="form-check">
                                                    <input class="form-check-input" autocomplete="off" type="checkbox" name="permissions[]" value="<?= $permKey ?>" id="perm_<?= $permKey ?>">
                                                    <label class="form-check-label" for="perm_<?= $permKey ?>"><?= $permName ?></label>
                                                </div>
                                            </div>
                                            <?php endforeach; ?>
                                        </div>
                                    </div>
                                    <?php if (next($permissions) !== false): ?>
                                    <hr>
                                    <?php endif; ?>
                                    <?php endforeach; ?>
                                </div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">状态</label>
                            <select name="status" class="form-select">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>
                        
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="/admin/role" class="btn btn-secondary">返回</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
