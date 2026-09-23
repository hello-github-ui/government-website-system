<?php 
$title = '媒体库';
$pageTitle = '媒体库管理';
$currentMenu = 'media';
include APP_PATH . 'view/admin/public/header.php'; 
?>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">媒体文件</h3>
        <div class="card-tools">
            <div class="btn-group mr-2">
                <a href="/admin/media" class="btn btn-default btn-sm <?php echo $type === '' ? 'active' : ''; ?>">全部</a>
                <a href="/admin/media?type=image" class="btn btn-default btn-sm <?php echo $type === 'image' ? 'active' : ''; ?>">图片</a>
                <a href="/admin/media?type=video" class="btn btn-default btn-sm <?php echo $type === 'video' ? 'active' : ''; ?>">视频</a>
                <a href="/admin/media?type=document" class="btn btn-default btn-sm <?php echo $type === 'document' ? 'active' : ''; ?>">文档</a>
            </div>
            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#uploadModal">
                <i class="fas fa-upload"></i> 上传文件
            </button>
        </div>
    </div>
    <div class="card-body">
        <?php if (!empty($list)): ?>
        <div class="row">
            <?php foreach ($list as $item): ?>
            <div class="col-md-3 col-sm-4 col-6 mb-3">
                <div class="card h-100">
                    <?php if ($item['type'] == 'image'): ?>
                    <img src="<?php echo $item['url']; ?>" class="card-img-top" style="height: 150px; object-fit: cover;">
                    <?php else: ?>
                    <div class="card-img-top bg-light d-flex align-items-center justify-content-center" style="height: 150px;">
                        <i class="fas fa-file fa-3x text-muted"></i>
                    </div>
                    <?php endif; ?>
                    <div class="card-body p-2">
                        <p class="card-text small text-truncate mb-1" title="<?php echo $item['name']; ?>">
                            <?php echo $item['name']; ?>
                        </p>
                        <p class="card-text small text-muted mb-0">
                            <?php echo $item['size']; ?> | <?php echo $item['time']; ?>
                        </p>
                    </div>
                    <div class="card-footer p-2">
                        <button type="button" class="btn btn-info btn-xs" onclick="copyUrl('<?php echo $item['url']; ?>')">
                            <i class="fas fa-link"></i> 复制链接
                        </button>
                        <button type="button" class="btn btn-danger btn-xs float-right" onclick="deleteFile('<?php echo $item['name']; ?>')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
            <?php endforeach; ?>
        </div>
        <?php else: ?>
        <div class="text-center py-5">
            <i class="fas fa-folder-open fa-3x text-muted mb-3"></i>
            <p>暂无媒体文件</p>
        </div>
        <?php endif; ?>
    </div>
    <?php if ($totalPage > 1): ?>
    <div class="card-footer clearfix">
        <ul class="pagination pagination-sm m-0 float-right">
            <?php if ($page > 1): ?>
            <li class="page-item"><a class="page-link" href="?page=<?php echo $page-1; ?><?php echo $type !== '' ? '&type='.$type : ''; ?>">上一页</a></li>
            <?php endif; ?>
            <?php for ($i = 1; $i <= $totalPage; $i++): ?>
            <li class="page-item <?php echo $i == $page ? 'active' : ''; ?>">
                <a class="page-link" href="?page=<?php echo $i; ?><?php echo $type !== '' ? '&type='.$type : ''; ?>"><?php echo $i; ?></a>
            </li>
            <?php endfor; ?>
            <?php if ($page < $totalPage): ?>
            <li class="page-item"><a class="page-link" href="?page=<?php echo $page+1; ?><?php echo $type !== '' ? '&type='.$type : ''; ?>">下一页</a></li>
            <?php endif; ?>
        </ul>
    </div>
    <?php endif; ?>
</div>

<!-- 上传模态框 -->
<div class="modal fade" id="uploadModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">上传文件</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="uploadForm" enctype="multipart/form-data">
                    <div class="form-group">
                        <label>选择文件</label>
                        <input type="file" name="file" autocomplete="off" class="form-control" required>
                        <small class="form-text text-muted">支持图片、文档等格式，最大10MB</small>
                    </div>
                </form>
                <div id="uploadProgress" class="progress mt-3" style="display: none;">
                    <div class="progress-bar" role="progressbar" style="width: 0%"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
            </div>
        </div>
    </div>
</div>

<script>
function copyUrl(url) {
    navigator.clipboard.writeText(url).then(() => {
        alert('链接已复制到剪贴板');
    });
}

function deleteFile(filename) {
    if (confirm('确定要删除这个文件吗？')) {
        fetch('/admin/media/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'filename=' + encodeURIComponent(filename)
        })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                location.reload();
            } else {
                alert(data.message);
            }
        });
    }
}

function uploadFile() {
    const form = document.getElementById('uploadForm');
    const formData = new FormData(form);
    
    fetch('/admin/media/upload', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            location.reload();
        } else {
            alert(data.message);
        }
    });
}
</script>

<?php include APP_PATH . 'view/admin/public/footer.php'; ?>
