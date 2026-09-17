package com.gov.gows.service;

import com.gov.gows.common.PageResult;
import com.gov.gows.entity.Notice;
import com.gov.gows.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告服务。
 */
@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public List<Notice> latest(int limit) {
        return noticeMapper.selectLatest(limit);
    }

    public PageResult<Notice> page(int page, int size) {
        int offset = (page - 1) * size;
        List<Notice> list = noticeMapper.selectPage(offset, size);
        return new PageResult<>(list, noticeMapper.count(), page, size);
    }

    /** 前台已发布公告分页。 */
    public PageResult<Notice> publishedPage(int page, int size) {
        int offset = (page - 1) * size;
        List<Notice> list = noticeMapper.selectPublishedPage(offset, size);
        return new PageResult<>(list, noticeMapper.countPublished(), page, size);
    }

    public Notice findById(Long id) {
        return noticeMapper.findById(id);
    }

    public void increaseViews(Long id) {
        noticeMapper.increaseViews(id);
    }

    public void create(Notice notice) {
        noticeMapper.insert(notice);
    }

    public void update(Notice notice) {
        noticeMapper.update(notice);
    }

    public void delete(Long id) {
        noticeMapper.deleteById(id);
    }

    public PageResult<Notice> search(String kw, int page, int size) {
        int offset = (page - 1) * size;
        List<Notice> list = noticeMapper.search(kw, offset, size);
        return new PageResult<>(list, noticeMapper.searchCount(kw), page, size);
    }
}
