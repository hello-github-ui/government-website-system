<?php include APP_PATH . 'view/admin/public/header.php'; ?>

<div class="container-fluid">
    <div class="row">
        <?php include APP_PATH . 'view/admin/public/sidebar.php'; ?>
        
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">操作日志</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button type="button" class="btn btn-outline-danger" onclick="clearLogs()">
                        <i class="bi bi-trash"></i> 清空日志
                    </button>
                </div>
            </div>
            
            <!-- 筛选 -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="/admin/log" method="get" class="row g-3">
                        <div class="col-md-2">
                            <select name="type" class="form-select">
                                <option value="">所有类型</option>
                                <?php foreach ($types as $key => $name): ?>
                                <option value="<?= $key ?>" <?= ($type ?? '') === $key ? 'selected' : '' ?>><?= $name ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select name="admin_id" class="form-select">
                                <option value="0">所有管理员</option>
                                <?php foreach ($admins as $id => $name): ?>
                                <option value="<?= $id ?>" <?= ($adminId ?? 0) == $id ? 'selected' : '' ?>><?= htmlspecialchars($name) ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <input type="date" autocomplete="off" name="date_start" class="form-control" value="<?= $dateStart ?? '' ?>" placeholder="开始日期">
                        </div>
                        <div class="col-md-2">
                            <input type="date" autocomplete="off" name="date_end" class="form-control" value="<?= $dateEnd ?? '' ?>" placeholder="结束日期">
                        </div>
                        <div class="col-md-2">
                            <input type="text" autocomplete="off" name="keyword" class="form-control" value="<?= htmlspecialchars($keyword ?? '') ?>" placeholder="关键词">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-outline-primary">筛选</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- 日志列表 -->
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>管理员</th>
                                    <th>类型</th>
                                    <th>内容</th>
                                    <th>IP地址</th>
                                    <th>时间</th>
                                    <th width="100">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php foreach ($logs as $log): ?>
                                <tr>
                                    <td><?= $log['id'] ?></td>
                                    <td><?= htmlspecialchars($admins[$log['admin_id']] ?? '未知') ?></td>
                                    <td>
                                        <span class="badge bg-<?= getLogTypeColor($log['type']) ?>"><?= $types[$log['type']] ?? $log['type'] ?></span>
                                    </td>
                                    <td><?= htmlspecialchars($log['content']) ?></td>
                                    <td><?= $log['ip'] ?></td>
                                    <td><?= $log['created_at'] ?></td>
                                    <td>
                                        <a href="/admin/log/view?id=<?= $log['id'] ?>" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye"></i>
                                        </a>
                                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteLog(<?= $log['id'] ?>)">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                                <?php endforeach; ?>
                            </tbody>
                        </table>
                    </div>
                    
                    <?php if (empty($logs)): ?>
                    <div class="text-center text-muted py-5">
                        <i class="bi bi-inbox fs-1"></i>
                        <p>暂无日志数据</p>
                    </div>
                    <?php endif; ?>
                </div>
            </div>
        </main>
    </div>
</div>

<script>
function deleteLog(id) {
    if (confirm('确定要删除这条日志吗？')) {
        fetch('/admin/log/delete', {
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

function clearLogs() {
    const days = prompt('请输入要保留多少天内的日志（默认30天）：', '30');
    if (days !== null) {
        fetch('/admin/log/clear', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'days=' + days
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

<?php
function getLogTypeColor($type) {
    $colors = [
        'login' => 'success',
        'logout' => 'secondary',
        'create' => 'primary',
        'update' => 'info',
        'delete' => 'danger',
        'upload' => 'warning',
        'download' => 'dark',
        'system' => 'light'
    ];
    return $colors[$type] ?? 'secondary';
}
?>
