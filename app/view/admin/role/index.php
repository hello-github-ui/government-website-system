<?php include APP_PATH . 'view/admin/public/header.php'; ?>

<div class="container-fluid">
    <div class="row">
        <?php include APP_PATH . 'view/admin/public/sidebar.php'; ?>
        
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">角色管理</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <a href="/admin/role/add" class="btn btn-primary">
                        <i class="bi bi-plus-lg"></i> 添加角色
                    </a>
                </div>
            </div>
            
            <!-- 搜索 -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="/admin/role" method="get" class="row g-3">
                        <div class="col-md-4">
                            <input type="text" name="keyword" autocomplete="off" class="form-control" placeholder="角色名称" value="<?= htmlspecialchars($keyword ?? '') ?>">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-outline-primary">搜索</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- 角色列表 -->
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>角色名称</th>
                                    <th>描述</th>
                                    <th>管理员数</th>
                                    <th>状态</th>
                                    <th width="150">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php foreach ($roles as $role): ?>
                                <tr>
                                    <td><?= $role['id'] ?></td>
                                    <td><?= htmlspecialchars($role['role_name']) ?></td>
                                    <td><?= htmlspecialchars($role['role_desc'] ?? '') ?></td>
                                    <td><?= $role['admin_count'] ?? 0 ?></td>
                                    <td>
                                        <?php if ($role['status']): ?>
                                        <span class="badge bg-success">启用</span>
                                        <?php else: ?>
                                        <span class="badge bg-secondary">禁用</span>
                                        <?php endif; ?>
                                    </td>
                                    <td>
                                        <a href="/admin/role/edit?id=<?= $role['id'] ?>" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteRole(<?= $role['id'] ?>)">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                                <?php endforeach; ?>
                            </tbody>
                        </table>
                    </div>
                    
                    <?php if (empty($roles)): ?>
                    <div class="text-center text-muted py-5">
                        <i class="bi bi-inbox fs-1"></i>
                        <p>暂无角色数据</p>
                    </div>
                    <?php endif; ?>
                </div>
            </div>
        </main>
    </div>
</div>

<script>
function deleteRole(id) {
    if (confirm('确定要删除这个角色吗？')) {
        fetch('/admin/role/delete', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'id=' + id
        }).then(r => r.json()).then(res => {
            if (res.code === 200) {
                location.reload();
            } else {
                alert(res.message);
            }
        });
    }
}
</script>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
