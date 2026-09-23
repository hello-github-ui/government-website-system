<?php 
$title = '查看咨询';
$pageTitle = '查看咨询';
$currentMenu = 'consult';
$breadcrumb = [
    ['name' => '咨询投诉', 'url' => '/admin/consult'],
    ['name' => '查看咨询']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">咨询详情</h3>
            </div>
            <div class="card-body">
                <div class="form-group">
                    <label>咨询人</label>
                    <p class="form-control-static"><?php echo htmlspecialchars($info['contact_name'] ?? '-'); ?></p>
                </div>
                <div class="form-group">
                    <label>联系电话</label>
                    <p class="form-control-static"><?php echo htmlspecialchars($info['contact_phone'] ?? '-'); ?></p>
                </div>
                <div class="form-group">
                    <label>电子邮箱</label>
                    <p class="form-control-static"><?php echo htmlspecialchars($info['contact_email'] ?? '-'); ?></p>
                </div>
                <div class="form-group">
                    <label>咨询内容</label>
                    <div class="p-3 bg-light rounded">
                        <?php echo nl2br(htmlspecialchars($info['content'])); ?>
                    </div>
                </div>
                <div class="form-group">
                    <label>提交时间</label>
                    <p class="form-control-static"><?php echo $info['create_time']; ?></p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">回复咨询</h3>
            </div>
            <div class="card-body">
                <?php if (!empty($info['reply_content'])): ?>
                <div class="form-group">
                    <label>历史回复</label>
                    <div class="p-3 bg-success text-white rounded mb-3">
                        <?php echo nl2br(htmlspecialchars($info['reply_content'])); ?>
                        <hr class="my-2">
                        <small>回复时间：<?php echo $info['reply_time']; ?></small>
                    </div>
                </div>
                <?php endif; ?>
                
                <form action="/admin/consult/reply" method="POST">
                    <input type="hidden" name="id" autocomplete="off" value="<?php echo $info['id']; ?>">
                    <div class="form-group">
                        <label>回复内容</label>
                        <textarea name="reply" class="form-control" rows="6" placeholder="请输入回复内容" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">提交回复</button>
                </form>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-body text-center">
                <a href="/admin/consult" class="btn btn-default">返回列表</a>
            </div>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
