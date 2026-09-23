<?php include APP_PATH . 'view/admin/public/header.php'; ?>

<div class="container-fluid">
    <div class="row">
        <?php include APP_PATH . 'view/admin/public/sidebar.php'; ?>
        
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">添加语言配置</h1>
            </div>
            
            <div class="card">
                <div class="card-body">
                    <form action="/admin/language/add" method="post">
                        <div class="mb-3">
                            <label class="form-label">语言键 <span class="text-danger">*</span></label>
                            <input type="text" name="lang_key" autocomplete="off" class="form-control" required placeholder="如：common.home">
                            <div class="form-text">唯一标识，建议使用模块.名称的格式</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">语言值</label>
                            <textarea name="lang_value" class="form-control" rows="3" placeholder="如：首页"></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">所属分组</label>
                            <select name="lang_group" class="form-select">
                                <?php foreach ($groups as $key => $name): ?>
                                <option value="<?= $key ?>"><?= htmlspecialchars($name) ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">所属模块</label>
                            <select name="module" class="form-select">
                                <?php foreach ($modules as $key => $name): ?>
                                <option value="<?= $key ?>"><?= $name ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <div class="form-check">
                                <input type="checkbox" autocomplete="off" name="is_default" value="1" class="form-check-input" id="isDefault">
                                <label class="form-check-label" for="isDefault">设为默认值</label>
                            </div>
                        </div>
                        
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="/admin/language" class="btn btn-secondary">返回</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
