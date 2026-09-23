<?php 
$title = '添加公告';
$pageTitle = '添加公告';
$currentMenu = 'notice';
$breadcrumb = [
    ['name' => '公告管理', 'url' => '/admin/notice'],
    ['name' => '添加公告']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">添加公告</h3>
    </div>
    <form action="/admin/notice/add" method="POST">
        <div class="card-body">
            <div class="form-group">
                <label>标题 <span class="text-danger">*</span></label>
                <input type="text" name="title" autocomplete="off" class="form-control" required placeholder="请输入公告标题">
            </div>
            
            <div class="form-group">
                <label>摘要</label>
                <textarea name="summary" class="form-control" rows="3" placeholder="请输入公告摘要（可选）"></textarea>
            </div>
            
            <div class="form-group">
                <label>内容 <span class="text-danger">*</span></label>
                <textarea name="content" class="form-control" rows="10" required placeholder="请输入公告内容"></textarea>
            </div>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label>置顶</label>
                        <select name="is_top" class="form-control">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <label>重要</label>
                        <select name="is_important" class="form-control">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-4">
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
            <a href="/admin/notice" class="btn btn-default">返回</a>
        </div>
    </form>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
