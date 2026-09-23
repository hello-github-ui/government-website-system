<?php include APP_PATH . 'view/admin/public/header.php'; ?>

<div class="container-fluid">
    <div class="row">
        <?php include APP_PATH . 'view/admin/public/sidebar.php'; ?>
        
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">添加导航</h1>
            </div>
            
            <div class="card">
                <div class="card-body">
                    <form action="/admin/nav/add" method="post">
                        <div class="mb-3">
                            <label class="form-label">导航名称 <span class="text-danger">*</span></label>
                            <input type="text" name="nav_name" autocomplete="off" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">链接地址 <span class="text-danger">*</span></label>
                            <input type="text" name="nav_url" autocomplete="off" class="form-control" required placeholder="如：/about">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">链接类型</label>
                            <select name="nav_type" class="form-select">
                                <option value="1">内部链接</option>
                                <option value="2">外部链接</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">上级导航</label>
                            <select name="parent_id" class="form-select">
                                <option value="0">作为一级导航</option>
                                <?php foreach ($parentNavs as $nav): ?>
                                <option value="<?= $nav['id'] ?>"><?= htmlspecialchars($nav['nav_name']) ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">打开方式</label>
                            <select name="target" class="form-select">
                                <option value="_self">当前窗口</option>
                                <option value="_blank">新窗口</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">图标类名</label>
                            <input type="text" name="icon" autocomplete="off" class="form-control" placeholder="如：house">
                            <div class="form-text">Bootstrap Icons 图标名称</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">排序</label>
                            <input type="number" autocomplete="off" name="sort" class="form-control" value="0">
                            <div class="form-text">数字越小越靠前</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">是否显示</label>
                            <select name="is_show" class="form-select">
                                <option value="1">显示</option>
                                <option value="0">隐藏</option>
                            </select>
                        </div>
                        
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="/admin/nav" class="btn btn-secondary">返回</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
