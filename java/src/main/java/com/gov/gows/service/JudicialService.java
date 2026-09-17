package com.gov.gows.service;

import com.gov.gows.common.PageResult;
import com.gov.gows.entity.Judicial;
import com.gov.gows.mapper.JudicialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 裁判文书服务。
 */
@Service
public class JudicialService {

    private final JudicialMapper judicialMapper;

    public JudicialService(JudicialMapper judicialMapper) {
        this.judicialMapper = judicialMapper;
    }

    public PageResult<Judicial> page(int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(judicialMapper.selectPage(offset, size), judicialMapper.count(), page, size);
    }

    public PageResult<Judicial> publicPage(int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(judicialMapper.selectPublicPage(offset, size), judicialMapper.countPublic(), page, size);
    }

    public Judicial findById(Long id) {
        return judicialMapper.findById(id);
    }

    public void increaseViews(Long id) {
        judicialMapper.increaseViews(id);
    }

    public void create(Judicial judicial) {
        judicialMapper.insert(judicial);
    }

    public void update(Judicial judicial) {
        judicialMapper.update(judicial);
    }

    public void delete(Long id) {
        judicialMapper.deleteById(id);
    }

    public PageResult<Judicial> search(String kw, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(judicialMapper.search(kw, offset, size), judicialMapper.searchCount(kw), page, size);
    }
}
