<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/">首页</a></li>
                    <li class="breadcrumb-item"><a href="/consult">咨询投诉</a></li>
                    <li class="breadcrumb-item active">提交咨询</li>
                </ol>
            </nav>
            
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="bi bi-chat-dots"></i> 提交咨询/投诉</h5>
                </div>
                <div class="card-body">
                    <form action="/consult/save" method="post" id="consultForm">
                        <div class="mb-3">
                            <label for="type" class="form-label">类型 <span class="text-danger">*</span></label>
                            <select class="form-select" id="type" name="type" required>
                                <option value="1">政策咨询</option>
                                <option value="2">投诉建议</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="title" class="form-label">标题 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" autocomplete="off" id="title" name="title"
                                   placeholder="请输入咨询标题" maxlength="200" required>
                            <div class="form-text">标题长度不超过200字</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="content" class="form-label">内容 <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="content" name="content" rows="8" 
                                      placeholder="请详细描述您的问题或建议..." required></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">公开设置</label>
                            <div class="form-check">
                                <input class="form-check-input" autocomplete="off" type="radio" name="is_public" id="public1" value="1" checked>
                                <label class="form-check-label" for="public1">
                                    公开（其他用户可以看到您的咨询和回复）
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" autocomplete="off" type="radio" name="is_public" id="public0" value="0">
                                <label class="form-check-label" for="public0">
                                    私密（仅您自己可见）
                                </label>
                            </div>
                        </div>
                        
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="/consult" class="btn btn-outline-secondary">取消</a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-send"></i> 提交咨询
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="alert alert-info mt-4">
                <h6><i class="bi bi-info-circle"></i> 温馨提示</h6>
                <ul class="mb-0">
                    <li>请如实填写咨询内容，我们会尽快处理并回复</li>
                    <li>涉及个人隐私的信息建议选择"私密"选项</li>
                    <li>请勿发布违法违规内容</li>
                    <li>您可以在"我的咨询"中查看处理进度</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script>
document.getElementById('consultForm').addEventListener('submit', function(e) {
    const title = document.getElementById('title').value.trim();
    const content = document.getElementById('content').value.trim();
    
    if (!title) {
        e.preventDefault();
        alert('请输入标题');
        return;
    }
    
    if (!content) {
        e.preventDefault();
        alert('请输入内容');
        return;
    }
    
    if (content.length < 10) {
        e.preventDefault();
        alert('内容不能少于10个字');
        return;
    }
});
</script>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
