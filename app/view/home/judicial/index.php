<?php include APP_PATH . 'view/home/public/header.php'; ?>

<div class="container py-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/">首页</a></li>
                    <li class="breadcrumb-item active" aria-current="page">裁判文书</li>
                </ol>
            </nav>
            <h1 class="page-title mb-4">
                <i class="fas fa-gavel me-2"></i>裁判文书检索
            </h1>
            
            <!-- 搜索框 -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="/judicial" method="GET" class="d-flex">
                        <input type="text" name="keyword" autocomplete="off" class="form-control form-control-lg me-2"
                               placeholder="请输入案件名称、案号或关键词" 
                               value="<?php echo htmlspecialchars($keyword); ?>">
                        <button class="btn btn-primary btn-lg" type="submit">
                            <i class="fas fa-search"></i> 检索
                        </button>
                    </form>
                </div>
            </div>
            
            <?php if ($isSearch && !empty($keyword)): ?>
                <div class="alert alert-info">
                    "<?php echo htmlspecialchars($keyword); ?>" 的搜索结果，共 <?php echo $total; ?> 条
                </div>
            <?php endif; ?>
            
            <?php if (!empty($judicials)): ?>
                <div class="list-group">
                    <?php foreach ($judicials as $item): ?>
                        <a href="/judicial/detail/<?php echo $item['id']; ?>" class="list-group-item list-group-item-action">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1"><?php echo htmlspecialchars($item['case_name']); ?></h5>
                                <small class="text-muted"><?php echo date('Y-m-d', strtotime($item['judge_date'])); ?></small>
                            </div>
                            <p class="mb-1 text-muted">
                                <small>
                                    <i class="fas fa-hashtag me-1"></i><?php echo htmlspecialchars($item['case_no']); ?>
                                    <span class="mx-2">|</span>
                                    <i class="fas fa-building me-1"></i><?php echo htmlspecialchars($item['court']); ?>
                                </small>
                            </p>
                        </a>
                    <?php endforeach; ?>
                </div>
                
                <!-- 分页 -->
                <?php if ($totalPages > 1): ?>
                    <nav class="mt-4">
                        <ul class="pagination justify-content-center">
                            <?php if ($page > 1): ?>
                                <li class="page-item">
                                    <a class="page-link" href="/judicial?keyword=<?php echo urlencode($keyword); ?>&page=<?php echo $page - 1; ?>">上一页</a>
                                </li>
                            <?php endif; ?>
                            
                            <?php for ($i = 1; $i <= $totalPages; $i++): ?>
                                <li class="page-item <?php echo $i == $page ? 'active' : ''; ?>">
                                    <a class="page-link" href="/judicial?keyword=<?php echo urlencode($keyword); ?>&page=<?php echo $i; ?>"><?php echo $i; ?></a>
                                </li>
                            <?php endfor; ?>
                            
                            <?php if ($page < $totalPages): ?>
                                <li class="page-item">
                                    <a class="page-link" href="/judicial?keyword=<?php echo urlencode($keyword); ?>&page=<?php echo $page + 1; ?>">下一页</a>
                                </li>
                            <?php endif; ?>
                        </ul>
                    </nav>
                <?php endif; ?>
            <?php else: ?>
                <div class="alert alert-info">
                    <?php echo $isSearch ? '未找到相关文书' : '暂无裁判文书'; ?>
                </div>
            <?php endif; ?>
        </div>
    </div>
</div>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
