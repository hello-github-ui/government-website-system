-- ===================================================================
-- 种子数据：角色、网站设置、前端文字、示例内容
-- 说明：管理员账号由 DatabaseInitializer 在运行时创建（bcrypt 密码）
-- ===================================================================

-- 默认角色
INSERT INTO `gov_admin_role` (`id`, `role_name`, `role_desc`, `permissions`, `status`) VALUES
(1, '超级管理员', '拥有所有权限', '["*"]', 1),
(2, '内容管理员', '管理网站内容', '["content.*","media.*"]', 1),
(3, '普通管理员', '基础管理权限', '["content.view","user.view"]', 1);

-- 默认网站设置
INSERT INTO `gov_settings` (`setting_key`, `setting_value`, `setting_group`, `setting_desc`) VALUES
('site_title', '政府官网系统', 'general', '网站标题'),
('site_subtitle', '政务服务门户网站', 'general', '网站副标题'),
('site_url', '', 'general', '网站URL'),
('site_logo', '', 'general', '网站Logo'),
('site_favicon', '', 'general', '网站图标'),
('icp_number', '', 'footer', 'ICP备案号'),
('police_number', '', 'footer', '公安备案号'),
('copyright', '© 2026 版权所有', 'footer', '版权信息'),
('seo_title', '', 'seo', 'SEO标题'),
('seo_keywords', '', 'seo', 'SEO关键词'),
('seo_description', '', 'seo', 'SEO描述'),
('lazyload_enabled', '1', 'feature', '是否启用懒加载'),
('login_fail_limit', '5', 'security', '登录失败限制次数'),
('login_lock_time', '30', 'security', '登录锁定时间(分钟)'),
('contact_phone', '12345 政务服务热线', 'contact', '联系电话'),
('contact_email', 'contact@gov.cn', 'contact', '联系邮箱'),
('contact_address', '某某市政府大楼', 'contact', '联系地址'),
('contact_work_time', '周一至周五 9:00-17:00', 'contact', '工作时间');

-- 默认前端文字配置
INSERT INTO `gov_language` (`lang_key`, `lang_value`, `lang_group`, `module`) VALUES
('common.home', '首页', 'common', 'common'),
('common.news', '新闻动态', 'common', 'common'),
('common.service', '政务服务', 'common', 'common'),
('common.contact', '联系我们', 'common', 'common'),
('common.search', '搜索', 'common', 'common'),
('common.login', '登录', 'common', 'common'),
('common.logout', '退出', 'common', 'common'),
('common.register', '注册', 'common', 'common'),
('common.submit', '提交', 'common', 'common'),
('common.more', '查看更多', 'common', 'common'),
('common.no_data', '暂无数据', 'common', 'common'),
('common.error', '出错了', 'common', 'common'),
('nav.gov_public', '政务公开', 'nav', 'common'),
('nav.public_service', '公众服务', 'nav', 'common'),
('nav.interaction', '互动交流', 'nav', 'common'),
('nav.judicial', '裁判文书', 'nav', 'common'),
('user.username', '用户名', 'user', 'common'),
('user.password', '密码', 'user', 'common'),
('judicial.search_placeholder', '请输入案件名称、案号或关键词', 'judicial', 'judicial'),
('judicial.search_btn', '检索', 'judicial', 'judicial'),
('judicial.case_no', '案号', 'judicial', 'judicial'),
('judicial.case_name', '案件名称', 'judicial', 'judicial');

-- 示例公告
INSERT INTO `gov_notice` (`title`, `content`, `summary`, `author`, `source`, `is_top`, `is_important`, `status`, `publish_time`) VALUES
('关于2026年国庆节放假安排的通知', '根据国务院办公厅通知精神，现将2026年国庆节放假安排通知如下。', '国庆节放假安排通知', '办公室', '政府办公室', 1, 1, 1, '2026-09-01 09:00:00'),
('政务服务大厅周末延时服务公告', '为方便群众办事，政务服务大厅自本月起实行周末延时服务。', '周末延时服务公告', '政务服务中心', '政务服务中心', 0, 0, 1, '2026-08-20 10:30:00'),
('关于开展政策法规宣传周活动的通知', '定于本月中旬开展政策法规宣传周活动，欢迎广大市民参与。', '政策法规宣传周', '司法局', '司法局', 0, 0, 1, '2026-08-15 14:00:00');

-- 示例政策
INSERT INTO `gov_policy` (`title`, `content`, `summary`, `doc_no`, `publish_org`, `publish_date`, `status`) VALUES
('优化营商环境实施办法', '为进一步优化营商环境，制定本实施办法。', '优化营商环境实施办法', '政发〔2026〕1号', '市人民政府', '2026-01-10', 1),
('公共服务事项便民化指引', '梳理公共服务事项，推出便民化办理指引。', '公共服务便民化指引', '政办发〔2026〕5号', '市政府办公厅', '2026-03-22', 1),
('政务信息公开工作规范', '规范政务信息公开的内容、方式与时限。', '政务信息公开规范', '政发〔2026〕8号', '市人民政府', '2026-05-18', 1);

-- 示例裁判文书（已公开并审核通过）
INSERT INTO `gov_judicial` (`case_no`, `case_name`, `court`, `case_type`, `judge_date`, `content`, `is_public`, `check_status`, `status`) VALUES
('(2026)沪01民初123号', '某民间借贷纠纷案', '上海市第一中级人民法院', '民事案件', '2026-06-15', '原告与被告因民间借贷产生纠纷，经审理判决如下……', 1, 1, 1),
('(2026)京02刑终45号', '某危险驾驶案', '北京市第二中级人民法院', '刑事案件', '2026-07-08', '被告人因危险驾驶一案，二审裁定如下……', 1, 1, 1);
