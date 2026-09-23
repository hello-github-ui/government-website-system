<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="<?php echo htmlspecialchars($siteConfig['seo_description'] ?? ''); ?>">
    <meta name="keywords" content="<?php echo htmlspecialchars($siteConfig['seo_keywords'] ?? ''); ?>">
    <meta name="author" content="<?php echo htmlspecialchars($siteConfig['site_title'] ?? '政府官网'); ?>">
    <meta name="theme-color" content="#dc2626">
    <title><?php echo htmlspecialchars($title ?? ($siteConfig['site_title'] ?? '政府官网')); ?></title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link href="/assets/css/style.css" rel="stylesheet">
    
    <?php if (!empty($siteConfig['site_favicon'])): ?>
    <link rel="icon" href="<?php echo htmlspecialchars($siteConfig['site_favicon']); ?>">
    <?php endif; ?>
</head>
<body>
    <!-- 顶部信息栏 -->
    <div class="top-bar d-none d-md-block">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center py-2">
                <div class="top-bar-left">
                    <span class="me-3"><i class="fas fa-phone-alt me-1 text-primary"></i><?php echo htmlspecialchars($siteConfig['contact_phone'] ?? '12345 政务服务热线'); ?></span>
                    <span><i class="fas fa-envelope me-1 text-primary"></i><?php echo htmlspecialchars($siteConfig['contact_email'] ?? 'contact@gov.cn'); ?></span>
                </div>
                <div class="top-bar-right">
                    <span class="me-3"><?php echo date('Y年m月d日'); ?> <?php echo ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'][date('w')]; ?></span>
                    <a href="/admin" class="text-muted text-decoration-none admin-link">
                        <i class="fas fa-cog me-1"></i>管理后台
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 主导航 -->
    <nav class="navbar navbar-expand-lg navbar-dark main-navbar sticky-top">
        <div class="container">
            <a class="navbar-brand" href="/">
                <div class="brand-icon">
                    <?php if (!empty($siteConfig['site_logo'])): ?>
                    <img src="<?php echo htmlspecialchars($siteConfig['site_logo']); ?>" alt="logo">
                    <?php else: ?>
                    <i class="fas fa-landmark"></i>
                    <?php endif; ?>
                </div>
                <div class="brand-text">
                    <span class="brand-title"><?php echo htmlspecialchars($siteConfig['site_title'] ?? '政府官网'); ?></span>
                    <span class="brand-subtitle d-none d-sm-block"><?php echo htmlspecialchars($siteConfig['site_subtitle'] ?? '政务服务门户网站'); ?></span>
                </div>
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link <?php echo ($_SERVER['REQUEST_URI'] == '/' || $_SERVER['REQUEST_URI'] == '') ? 'active' : ''; ?>" href="/">
                            <i class="fas fa-home me-1"></i><span><?php echo $lang['common.home'] ?? '首页'; ?></span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <?php echo strpos($_SERVER['REQUEST_URI'], '/notice') === 0 ? 'active' : ''; ?>" href="/notice">
                            <i class="fas fa-bullhorn me-1"></i><span><?php echo $lang['nav.gov_public'] ?? '政务公开'; ?></span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <?php echo strpos($_SERVER['REQUEST_URI'], '/service') === 0 ? 'active' : ''; ?>" href="/service">
                            <i class="fas fa-tasks me-1"></i><span><?php echo $lang['nav.public_service'] ?? '公众服务'; ?></span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <?php echo strpos($_SERVER['REQUEST_URI'], '/judicial') === 0 ? 'active' : ''; ?>" href="/judicial">
                            <i class="fas fa-gavel me-1"></i><span><?php echo $lang['nav.judicial'] ?? '裁判文书'; ?></span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <?php echo strpos($_SERVER['REQUEST_URI'], '/consult') === 0 ? 'active' : ''; ?>" href="/consult">
                            <i class="fas fa-comments me-1"></i><span><?php echo $lang['nav.consult'] ?? '咨询投诉'; ?></span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <?php echo strpos($_SERVER['REQUEST_URI'], '/contact') === 0 ? 'active' : ''; ?>" href="/contact">
                            <i class="fas fa-envelope me-1"></i><span><?php echo $lang['common.contact'] ?? '联系我们'; ?></span>
                        </a>
                    </li>
                </ul>
                
                <div class="navbar-actions d-flex align-items-center gap-2 ms-lg-3">
                    <button class="btn btn-search d-none d-lg-flex" type="button" data-bs-toggle="modal" data-bs-target="#searchModal">
                        <i class="fas fa-search"></i>
                    </button>
                    
                    <?php if (isset($_SESSION['user_id'])): ?>
                    <div class="dropdown user-dropdown">
                        <button class="btn btn-user dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                            <div class="user-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <span class="d-none d-md-inline"><?php echo htmlspecialchars($_SESSION['user_name'] ?? '用户'); ?></span>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow-lg">
                            <li class="dropdown-header">
                                <i class="fas fa-user-circle me-2"></i><?php echo htmlspecialchars($_SESSION['user_name'] ?? '用户'); ?>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/user/profile"><i class="fas fa-id-card me-2 text-primary"></i>个人中心</a></li>
                            <li><a class="dropdown-item" href="/consult/my"><i class="fas fa-comments me-2 text-primary"></i>我的咨询</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="/logout"><i class="fas fa-sign-out-alt me-2"></i><?php echo $lang['common.logout'] ?? '退出登录'; ?></a></li>
                        </ul>
                    </div>
                    <?php else: ?>
                    <a href="/login" class="btn btn-login">
                        <i class="fas fa-sign-in-alt me-1"></i><span class="d-none d-md-inline"><?php echo $lang['common.login'] ?? '登录'; ?></span>
                    </a>
                    <?php endif; ?>
                </div>
            </div>
        </div>
    </nav>
    
    <!-- 搜索模态框 -->
    <div class="modal fade" id="searchModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title"><i class="fas fa-search me-2 text-primary"></i>全站搜索</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body pt-3">
                    <form action="/search" method="GET">
                        <div class="input-group input-group-lg">
                            <input type="text" name="q" autocomplete="off" class="form-control" placeholder="请输入关键词搜索..." autofocus>
                            <button class="btn btn-primary" type="submit">
                                <i class="fas fa-search me-1"></i>搜索
                            </button>
                        </div>
                    </form>
                    <div class="mt-3">
                        <small class="text-muted">热门搜索：</small>
                        <a href="/search?q=政策" class="badge bg-light text-dark text-decoration-none me-1">政策</a>
                        <a href="/search?q=公告" class="badge bg-light text-dark text-decoration-none me-1">公告</a>
                        <a href="/search?q=办事指南" class="badge bg-light text-dark text-decoration-none">办事指南</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
