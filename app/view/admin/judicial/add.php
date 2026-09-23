<?php 
$title = '添加裁判文书';
$pageTitle = '添加裁判文书';
$currentMenu = 'judicial';
$breadcrumb = [
    ['name' => '裁判文书', 'url' => '/admin/judicial'],
    ['name' => '添加文书']
];
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">添加裁判文书</h3>
    </div>
    <form action="/admin/judicial/add" method="POST">
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>案号 <span class="text-danger">*</span></label>
                        <input type="text" name="case_no" autocomplete="off" class="form-control" required placeholder="请输入案号">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>案件名称 <span class="text-danger">*</span></label>
                        <input type="text" name="case_name" autocomplete="off" class="form-control" required placeholder="请输入案件名称">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>法院 <span class="text-danger">*</span></label>
                        <input type="text" name="court" class="form-control" autocomplete="off" required placeholder="请输入法院名称">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>案件类型</label>
                        <select name="case_type" class="form-control">
                            <option value="民事案件">民事案件</option>
                            <option value="刑事案件">刑事案件</option>
                            <option value="行政案件">行政案件</option>
                            <option value="执行案件">执行案件</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <label>文书内容 <span class="text-danger">*</span></label>
                <textarea name="content" class="form-control" rows="10" required placeholder="请输入文书内容"></textarea>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>判决日期</label>
                        <input type="date" name="judge_date" autocomplete="off" class="form-control" value="<?php echo date('Y-m-d'); ?>">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>状态</label>
                        <select name="status" class="form-control">
                            <option value="1">公开</option>
                            <option value="0">未公开</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-footer">
            <button type="submit" class="btn btn-primary">保存</button>
            <a href="/admin/judicial" class="btn btn-default">返回</a>
        </div>
    </form>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
