<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-lg-6 mx-auto">
            <h1 class="mb-4">在线咨询</h1>
            
            <div class="card">
                <div class="card-body">
                    <form action="/service/submitConsult" method="POST">
                        <div class="mb-3">
                            <label class="form-label">咨询类型</label>
                            <select name="type" class="form-select">
                                <option value="1">政策咨询</option>
                                <option value="2">办事咨询</option>
                                <option value="3">投诉建议</option>
                                <option value="4">其他</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">姓名 *</label>
                            <input type="text" name="name" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">联系电话 *</label>
                            <input type="tel" name="phone" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">电子邮箱</label>
                            <input type="email" name="email" autocomplete="off" class="form-control">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">咨询标题 *</label>
                            <input type="text" name="title" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">咨询内容 *</label>
                            <textarea name="content" class="form-control" rows="5" required></textarea>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">提交咨询</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
