<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <h1 class="mb-4">全站搜索</h1>
            
            <!-- 搜索框 -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="/search" method="GET" class="d-flex">
                        <input type="text" name="q" autocomplete="off" class="form-control form-control-lg me-2"
                               placeholder="请输入关键词搜索..." 
                               value="<?php echo htmlspecialchars($keyword); ?>">
                        <button class="btn btn-primary btn-lg" type="submit">
                            <i class="fas fa-search"></i> 搜索
                        </button>
                    </form>
                </div>
            </div>
            
            <?php if (!empty($keyword)): ?>
                <div class="alert alert-info">
                    "<?php echo htmlspecialchars($keyword); ?>" 的搜索结果，共 <?php echo $total; ?> 条
                </div>
                
                <?php if ($type === 'all'): ?>
                    <!-- 综合搜索结果 -->
                    <?php if (!empty($results['data']['notices'])): ?>
                        <h5 class="mt-4 mb-3"><i class="fas fa-bullhorn text-primary me-2"></i>政务公告</h5>
                        <div class="list-group mb-4">
                            <?php foreach ($results['data']['notices'] as $item): ?>
                                <a href="/notice/detail/<?php echo $item['id']; ?>" class="list-group-item list-group-item-action">
                                    <h6 class="mb-1"><?php echo htmlspecialchars($item['title']); ?></h6>
                                    <small class="text-muted"><?php echo date('Y-m-d', strtotime($item['publish_time'])); ?></small>
                                </a>
                            <?php endforeach; ?>
                        </div>
                    <?php endif; ?>
                    
                    <?php if (!empty($results['data']['policies'])): ?>
                        <h5 class="mt-4 mb-3"><i class="fas fa-file-alt text-success me-2"></i>政策法规</h5>
                        <div class="list-group mb-4">
                            <?php foreach ($results['data']['policies'] as $item): ?>
                                <a href="/policy/detail/<?php echo $item['id']; ?>" class="list-group-item list-group-item-action">
                                    <h6 class="mb-1"><?php echo htmlspecialchars($item['title']); ?></h6>
                                    <small class="text-muted"><?php echo date('Y-m-d', strtotime($item['publish_date'])); ?></small>
                                </a>
                            <?php endforeach; ?>
                        </div>
                    <?php endif; ?>
                    
                    <?php if (!empty($results['data']['judicials'])): ?>
                        <h5 class="mt-4 mb-3"><i class="fas fa-gavel text-warning me-2"></i>裁判文书</h5>
                        <div class="list-group mb-4">
                            <?php foreach ($results['data']['judicials'] as $item): ?>
                                <a href="/judicial/detail/<?php echo $item['id']; ?>" class="list-group-item list-group-item-action">
                                    <h6 class="mb-1"><?php echo htmlspecialchars($item['case_name']); ?></h6>
                                    <small class="text-muted"><?php echo htmlspecialchars($item['case_no']); ?></small>
                                </a>
                            <?php endforeach; ?>
                        </div>
                    <?php endif; ?>
                    
                    <?php if (empty($results['data']['notices']) && empty($results['data']['policies']) && empty($results['data']['judicials'])): ?>
                        <div class="alert alert-warning">未找到相关内容</div>
                    <?php endif; ?>
                <?php endif; ?>
            <?php else: ?>
                <div class="text-center py-5 text-muted">
                    <i class="fas fa-search fa-3x mb-3"></i>
                    <p>请输入关键词进行搜索</p>
                </div>
            <?php endif; ?>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
