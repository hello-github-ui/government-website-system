<?php include APP_PATH . 'view/home/public/header.php'; ?>

<!-- 轮播图区域 - 现代化设计 -->
<section class="hero-section">
    <div id="heroCarousel" class="carousel slide carousel-fade" data-bs-ride="carousel" data-bs-interval="5000">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <div class="hero-slide" style="background: linear-gradient(135deg, #dc2626 0%, #b91c1c 50%, #7f1d1d 100%);">
                    <div class="hero-pattern"></div>
                    <div class="container">
                        <div class="row align-items-center" style="min-height: 520px;">
                            <div class="col-lg-7 text-white hero-content">
                                <span class="hero-badge mb-3 d-inline-block">
                                    <i class="fas fa-star me-1"></i>政务服务
                                </span>
                                <h1 class="display-3 fw-bold mb-4 hero-title"><?php echo htmlspecialchars($siteConfig['site_title'] ?? '政府官网'); ?></h1>
                                <p class="lead mb-4 hero-subtitle opacity-95"><?php echo htmlspecialchars($siteConfig['site_subtitle'] ?? '政务服务门户网站'); ?></p>
                                <div class="hero-buttons d-flex flex-wrap gap-3">
                                    <a href="/service" class="btn btn-light btn-lg rounded-pill px-5 fw-semibold">
                                        <i class="fas fa-arrow-right me-2"></i>开始办事
                                    </a>
                                    <a href="/notice" class="btn btn-outline-light btn-lg rounded-pill px-4">
                                        <i class="fas fa-bullhorn me-2"></i>查看公告
                                    </a>
                                </div>
                            </div>
                            <div class="col-lg-5 d-none d-lg-block">
                                <div class="hero-image-float">
                                    <i class="fas fa-landmark hero-icon"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="carousel-item">
                <div class="hero-slide" style="background: linear-gradient(135deg, #ea580c 0%, #dc2626 50%, #b91c1c 100%);">
                    <div class="hero-pattern"></div>
                    <div class="container">
                        <div class="row align-items-center" style="min-height: 520px;">
                            <div class="col-lg-7 text-white hero-content">
                                <span class="hero-badge mb-3 d-inline-block">
                                    <i class="fas fa-sun me-1"></i>阳光政务
                                </span>
                                <h1 class="display-3 fw-bold mb-4 hero-title">政务公开透明</h1>
                                <p class="lead mb-4 hero-subtitle opacity-95">及时发布政府信息，推进阳光政务建设，让权力在阳光下运行</p>
                                <div class="hero-buttons d-flex flex-wrap gap-3">
                                    <a href="/notice" class="btn btn-light btn-lg rounded-pill px-5 fw-semibold">
                                        <i class="fas fa-arrow-right me-2"></i>查看公告
                                    </a>
                                    <a href="/policy" class="btn btn-outline-light btn-lg rounded-pill px-4">
                                        <i class="fas fa-file-alt me-2"></i>政策法规
                                    </a>
                                </div>
                            </div>
                            <div class="col-lg-5 d-none d-lg-block">
                                <div class="hero-image-float">
                                    <i class="fas fa-bullhorn hero-icon"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="carousel-item">
                <div class="hero-slide" style="background: linear-gradient(135deg, #7c3aed 0%, #dc2626 50%, #b91c1c 100%);">
                    <div class="hero-pattern"></div>
                    <div class="container">
                        <div class="row align-items-center" style="min-height: 520px;">
                            <div class="col-lg-7 text-white hero-content">
                                <span class="hero-badge mb-3 d-inline-block">
                                    <i class="fas fa-balance-scale me-1"></i>司法公开
                                </span>
                                <h1 class="display-3 fw-bold mb-4 hero-title">裁判文书公开</h1>
                                <p class="lead mb-4 hero-subtitle opacity-95">公开透明的司法信息查询平台，让公平正义看得见</p>
                                <div class="hero-buttons d-flex flex-wrap gap-3">
                                    <a href="/judicial" class="btn btn-light btn-lg rounded-pill px-5 fw-semibold">
                                        <i class="fas fa-arrow-right me-2"></i>立即检索
                                    </a>
                                    <a href="/consult" class="btn btn-outline-light btn-lg rounded-pill px-4">
                                        <i class="fas fa-comments me-2"></i>咨询投诉
                                    </a>
                                </div>
                            </div>
                            <div class="col-lg-5 d-none d-lg-block">
                                <div class="hero-image-float">
                                    <i class="fas fa-gavel hero-icon"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#heroCarousel" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">上一张</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#heroCarousel" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">下一张</span>
        </button>
    </div>
</section>

<!-- 快捷入口 - 现代化卡片设计 -->
<section class="py-5 bg-light quick-access-section">
    <div class="container">
        <div class="text-center mb-5">
            <span class="section-badge">便捷服务</span>
            <h2 class="section-title-center">快速通道</h2>
            <p class="section-subtitle text-muted">为您提供一站式政务服务入口</p>
        </div>
        <div class="row g-4">
            <div class="col-6 col-lg-3">
                <a href="/notice" class="text-decoration-none quick-card-link">
                    <div class="card h-100 text-center p-4 border-0 shadow-sm quick-card">
                        <div class="card-body">
                            <div class="mb-4">
                                <div class="quick-icon-wrapper bg-gradient-red">
                                    <i class="fas fa-bullhorn"></i>
                                </div>
                            </div>
                            <h5 class="card-title text-dark fw-semibold">政务公告</h5>
                            <p class="card-text text-muted small mb-0">最新政府公告通知</p>
                            <div class="quick-arrow">
                                <i class="fas fa-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-lg-3">
                <a href="/policy" class="text-decoration-none quick-card-link">
                    <div class="card h-100 text-center p-4 border-0 shadow-sm quick-card">
                        <div class="card-body">
                            <div class="mb-4">
                                <div class="quick-icon-wrapper bg-gradient-orange">
                                    <i class="fas fa-file-alt"></i>
                                </div>
                            </div>
                            <h5 class="card-title text-dark fw-semibold">政策法规</h5>
                            <p class="card-text text-muted small mb-0">政策法规文件查询</p>
                            <div class="quick-arrow">
                                <i class="fas fa-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-lg-3">
                <a href="/service" class="text-decoration-none quick-card-link">
                    <div class="card h-100 text-center p-4 border-0 shadow-sm quick-card">
                        <div class="card-body">
                            <div class="mb-4">
                                <div class="quick-icon-wrapper bg-gradient-blue">
                                    <i class="fas fa-tasks"></i>
                                </div>
                            </div>
                            <h5 class="card-title text-dark fw-semibold">在线办事</h5>
                            <p class="card-text text-muted small mb-0">便民服务在线办理</p>
                            <div class="quick-arrow">
                                <i class="fas fa-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-lg-3">
                <a href="/judicial" class="text-decoration-none quick-card-link">
                    <div class="card h-100 text-center p-4 border-0 shadow-sm quick-card">
                        <div class="card-body">
                            <div class="mb-4">
                                <div class="quick-icon-wrapper bg-gradient-purple">
                                    <i class="fas fa-gavel"></i>
                                </div>
                            </div>
                            <h5 class="card-title text-dark fw-semibold">裁判文书</h5>
                            <p class="card-text text-muted small mb-0">司法文书公开检索</p>
                            <div class="quick-arrow">
                                <i class="fas fa-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
</section>

<!-- 最新公告和政策 - 现代化布局 -->
<section class="py-5 content-section">
    <div class="container">
        <div class="row g-4">
            <!-- 最新公告 -->
            <div class="col-lg-8">
                <div class="section-header d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <span class="section-badge-sm">政务动态</span>
                        <h2 class="section-title-inline mb-0">
                            <i class="fas fa-bullhorn text-gradient me-2"></i>最新公告
                        </h2>
                    </div>
                    <a href="/notice" class="btn btn-outline-primary btn-sm rounded-pill">
                        查看更多<i class="fas fa-arrow-right ms-1"></i>
                    </a>
                </div>
                
                <div class="card border-0 shadow-sm content-card">
                    <div class="list-group list-group-flush notice-list">
                        <?php if (!empty($notices)): ?>
                            <?php foreach ($notices as $index => $notice): ?>
                            <a href="/notice/detail/<?php echo $notice['id']; ?>" class="list-group-item list-group-item-action py-3 notice-item" style="animation-delay: <?php echo $index * 0.1; ?>s">
                                <div class="d-flex w-100 justify-content-between align-items-start">
                                    <div class="flex-grow-1 me-3">
                                        <div class="notice-tags mb-2">
                                            <?php if ($notice['is_top']): ?>
                                            <span class="badge bg-danger me-1"><i class="fas fa-thumbtack me-1"></i>置顶</span>
                                            <?php endif; ?>
                                            <?php if ($notice['is_important']): ?>
                                            <span class="badge bg-warning text-dark me-1"><i class="fas fa-exclamation me-1"></i>重要</span>
                                            <?php endif; ?>
                                        </div>
                                        <h6 class="notice-title mb-2"><?php echo htmlspecialchars($notice['title']); ?></h6>
                                        <p class="notice-summary mb-0 text-muted small"><?php echo htmlspecialchars($notice['summary'] ?? ''); ?></p>
                                    </div>
                                    <div class="notice-date text-center ms-2">
                                        <span class="date-day"><?php echo date('d', strtotime($notice['publish_time'])); ?></span>
                                        <span class="date-month"><?php echo date('m月', strtotime($notice['publish_time'])); ?></span>
                                    </div>
                                </div>
                            </a>
                            <?php endforeach; ?>
                        <?php else: ?>
                            <div class="list-group-item text-center py-5 text-muted empty-state">
                                <div class="empty-icon">
                                    <i class="fas fa-inbox"></i>
                                </div>
                                <p class="mb-0"><?php echo $lang['common.no_data'] ?? '暂无公告'; ?></p>
                            </div>
                        <?php endif; ?>
                    </div>
                </div>
            </div>
            
            <!-- 最新政策 -->
            <div class="col-lg-4">
                <div class="section-header mb-4">
                    <span class="section-badge-sm">政策文件</span>
                    <h2 class="section-title-inline mb-0">
                        <i class="fas fa-file-alt text-gradient me-2"></i>最新政策
                    </h2>
                </div>
                <div class="card border-0 shadow-sm h-100 policy-card-modern">
                    <div class="card-body p-0">
                        <div class="list-group list-group-flush policy-list">
                            <?php if (!empty($policies)): ?>
                                <?php foreach ($policies as $index => $policy): ?>
                                <a href="/policy/detail/<?php echo $policy['id']; ?>" class="list-group-item list-group-item-action py-3 policy-item" style="animation-delay: <?php echo $index * 0.1; ?>s">
                                    <div class="d-flex align-items-start">
                                        <div class="policy-icon me-3">
                                            <i class="fas fa-file-contract"></i>
                                        </div>
                                        <div class="flex-grow-1 min-width-0">
                                            <h6 class="policy-title text-truncate mb-1"><?php echo htmlspecialchars($policy['title']); ?></h6>
                                            <div class="policy-meta d-flex align-items-center text-muted small">
                                                <span class="me-2"><i class="fas fa-building me-1"></i><?php echo htmlspecialchars($policy['publish_org'] ?? '政府部门'); ?></span>
                                                <span><?php echo date('Y-m-d', strtotime($policy['publish_date'])); ?></span>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                                <?php endforeach; ?>
                            <?php else: ?>
                                <div class="list-group-item text-center py-5 text-muted empty-state">
                                    <div class="empty-icon">
                                        <i class="fas fa-folder-open"></i>
                                    </div>
                                    <p class="mb-0"><?php echo $lang['common.no_data'] ?? '暂无政策'; ?></p>
                                </div>
                            <?php endif; ?>
                        </div>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center py-3">
                        <a href="/policy" class="btn btn-soft-primary btn-sm rounded-pill w-100">
                            查看全部政策<i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- 裁判文书检索 - 现代化搜索区域 -->
<section class="py-5 bg-gradient-search search-section">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8 text-center">
                <div class="search-header mb-4">
                    <span class="search-badge">司法公开</span>
                    <h2 class="search-title">
                        <i class="fas fa-gavel me-2"></i>裁判文书检索
                    </h2>
                    <p class="search-subtitle">公开透明的司法信息查询平台，支持按案件名称、案号、法院等多维度检索</p>
                </div>
                
                <form action="/judicial" method="GET" class="search-form-modern mb-4">
                    <div class="input-group input-group-lg shadow-lg rounded-pill overflow-hidden bg-white">
                        <span class="input-group-text border-0 bg-white ps-4">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" name="keyword" autocomplete="off" class="form-control border-0 py-3" placeholder="请输入案件名称、案号或关键词" aria-label="搜索裁判文书">
                        <button class="btn btn-gradient-primary px-5 fw-semibold" type="submit">
                            检索
                        </button>
                    </div>
                </form>
                
                <div class="search-tags d-flex flex-wrap justify-content-center gap-2">
                    <span class="search-tag"><i class="fas fa-balance-scale me-1"></i>民事案件</span>
                    <span class="search-tag"><i class="fas fa-gavel me-1"></i>刑事案件</span>
                    <span class="search-tag"><i class="fas fa-landmark me-1"></i>行政案件</span>
                    <span class="search-tag"><i class="fas fa-hammer me-1"></i>执行案件</span>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- 统计数据 - 现代化统计展示 -->
<section class="py-5 stats-section-modern">
    <div class="container">
        <div class="text-center mb-5">
            <span class="section-badge">数据概览</span>
            <h2 class="section-title-center">平台统计</h2>
            <p class="section-subtitle text-muted">为您提供全面的政务服务数据</p>
        </div>
        <div class="row g-4">
            <div class="col-6 col-md-3">
                <div class="stat-card-modern">
                    <div class="stat-icon-modern stat-icon-red">
                        <i class="fas fa-bullhorn"></i>
                    </div>
                    <div class="stat-content">
                        <h2 class="stat-number-modern" data-count="1000">1000+</h2>
                        <p class="stat-label-modern">政务公告</p>
                    </div>
                    <div class="stat-wave"></div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-card-modern">
                    <div class="stat-icon-modern stat-icon-orange">
                        <i class="fas fa-file-alt"></i>
                    </div>
                    <div class="stat-content">
                        <h2 class="stat-number-modern" data-count="500">500+</h2>
                        <p class="stat-label-modern">政策法规</p>
                    </div>
                    <div class="stat-wave"></div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-card-modern">
                    <div class="stat-icon-modern stat-icon-blue">
                        <i class="fas fa-tasks"></i>
                    </div>
                    <div class="stat-content">
                        <h2 class="stat-number-modern" data-count="50">50+</h2>
                        <p class="stat-label-modern">在线服务</p>
                    </div>
                    <div class="stat-wave"></div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-card-modern">
                    <div class="stat-icon-modern stat-icon-purple">
                        <i class="fas fa-gavel"></i>
                    </div>
                    <div class="stat-content">
                        <h2 class="stat-number-modern" data-count="10000">10000+</h2>
                        <p class="stat-label-modern">裁判文书</p>
                    </div>
                    <div class="stat-wave"></div>
                </div>
            </div>
        </div>
    </div>
</section>

<?php include APP_PATH . 'view/home/public/footer.php'; ?>
