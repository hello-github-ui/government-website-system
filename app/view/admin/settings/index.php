<?php 
$title = '网站设置';
$pageTitle = '网站设置';
$currentMenu = 'settings';
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">基本设置</h3>
            </div>
            <form action="/admin/settings" method="POST">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>网站标题 <span class="text-danger">*</span></label>
                                <input type="text" name="site_title" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($config['site_title'] ?? ''); ?>" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>网站副标题</label>
                                <input type="text" name="site_subtitle" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($config['site_subtitle'] ?? ''); ?>">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>网站URL <span class="text-danger">*</span></label>
                        <input type="url" name="site_url" class="form-control" autocomplete="off" value="<?php echo htmlspecialchars($config['site_url'] ?? ''); ?>" required placeholder="https://www.example.com">
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>网站Logo</label>
                                <input type="text" name="site_logo" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($config['site_logo'] ?? ''); ?>" placeholder="/assets/images/logo.png">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>网站Favicon</label>
                                <input type="text" name="site_favicon" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($config['site_favicon'] ?? ''); ?>" placeholder="/favicon.ico">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>SEO关键词</label>
                                <input type="text" name="seo_keywords" autocomplete="off" class="form-control" value="<?php echo htmlspecialchars($config['seo_keywords'] ?? ''); ?>" placeholder="关键词1,关键词2,关键词3">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>SEO描述</label>
                                <input type="text" name="seo_description" class="form-control" value="<?php echo htmlspecialchars($config['seo_description'] ?? ''); ?>">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>ICP备案号</label>
                                <input type="text" name="icp_number" class="form-control" value="<?php echo htmlspecialchars($config['icp_number'] ?? ''); ?>" placeholder="京ICP备XXXXXXXX号">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>公安备案号</label>
                                <input type="text" name="police_number" class="form-control" value="<?php echo htmlspecialchars($config['police_number'] ?? ''); ?>" placeholder="京公网安备XXXXXXXX号">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>版权信息</label>
                        <input type="text" name="copyright" class="form-control" value="<?php echo htmlspecialchars($config['copyright'] ?? '© 2026 版权所有'); ?>">
                    </div>
                    
                    <hr>
                    <h5>联系方式</h5>
                    
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>联系地址</label>
                                <input type="text" name="contact_address" class="form-control" value="<?php echo htmlspecialchars($config['contact_address'] ?? ''); ?>" placeholder="某某市政府大楼">
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>联系电话</label>
                                <input type="text" name="contact_phone" class="form-control" value="<?php echo htmlspecialchars($config['contact_phone'] ?? ''); ?>" placeholder="12345 政务服务热线">
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>联系邮箱</label>
                                <input type="email" name="contact_email" class="form-control" value="<?php echo htmlspecialchars($config['contact_email'] ?? ''); ?>" placeholder="contact@gov.cn">
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>工作时间</label>
                                <input type="text" name="contact_work_time" class="form-control" value="<?php echo htmlspecialchars($config['contact_work_time'] ?? ''); ?>" placeholder="周一至周五 9:00-17:00">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <button type="submit" class="btn btn-primary">保存设置</button>
                </div>
            </form>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
