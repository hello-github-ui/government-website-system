<?php 
$title = '编辑政策';
$pageTitle = '编辑政策';
$currentMenu = 'policy';
$breadcrumb = [
    ['name' => '政策法规', 'url' => '/admin/policy'],
    ['name' => '编辑政策']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">编辑政策</h3>
    </div>
    <form action="/admin/policy/edit?id=<?php echo $info['id']; ?>" method="POST">
        <div class="card-body">
            <div class="form-group">
                <label>标题 <span class="text-danger">*</span></label>
                <input type="text" name="title" autocomplete="off" class="form-control" required value="<?php echo htmlspecialchars($info['title']); ?>" placeholder="请输入政策标题">
            </div>
            
            <div class="form-group">
                <label>发布部门</label>
                <input type="text" name="publish_org" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($info['publish_org'] ?? ''); ?>" placeholder="请输入发布部门">
            </div>
            
            <div class="form-group">
                <label>内容 <span class="text-danger">*</span></label>
                <textarea name="content" class="form-control" rows="10" required placeholder="请输入政策内容"><?php echo htmlspecialchars($info['content'] ?? ''); ?></textarea>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>发布日期</label>
                        <input type="date" name="publish_date" autocomplete="off" class="form-control" value="<?php echo $info['publish_date'] ?? date('Y-m-d'); ?>">
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
            <a href="/admin/policy" class="btn btn-default">返回</a>
        </div>
    </form>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
