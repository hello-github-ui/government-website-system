package com.example.gov.service;

import com.example.gov.common.PageResult;
import com.example.gov.entity.Consult;
import com.example.gov.mapper.ConsultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咨询投诉服务。
 */
@Service
public class ConsultService {

    private final ConsultMapper consultMapper;

    public ConsultService(ConsultMapper consultMapper) {
        this.consultMapper = consultMapper;
    }

    public PageResult<Consult> adminPage(Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Consult> list = consultMapper.selectPageByStatus(status, offset, size);
        return new PageResult<>(list, consultMapper.countByStatus(status), page, size);
    }

    public long pendingCount() {
        return consultMapper.countPending();
    }

    public Consult findById(Long id) {
        return consultMapper.findById(id);
    }

    public void create(Consult consult) {
        consultMapper.insert(consult);
    }

    public void markRead(Long id) {
        consultMapper.markRead(id);
    }

    public void reply(Long id, String reply, Long adminId) {
        consultMapper.reply(id, reply, adminId);
    }

    public void delete(Long id) {
        consultMapper.deleteById(id);
    }

    public PageResult<Consult> publicReplied(int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(consultMapper.selectPublicReplied(offset, size), consultMapper.countPublicReplied(), page, size);
    }

    public PageResult<Consult> myList(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(consultMapper.selectByUser(userId, offset, size), consultMapper.countByUser(userId), page, size);
    }
}
