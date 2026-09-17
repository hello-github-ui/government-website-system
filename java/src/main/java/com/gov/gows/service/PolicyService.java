package com.gov.gows.service;

import com.gov.gows.common.PageResult;
import com.gov.gows.entity.Policy;
import com.gov.gows.mapper.PolicyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 政策法规服务。
 */
@Service
public class PolicyService {

    private final PolicyMapper policyMapper;

    public PolicyService(PolicyMapper policyMapper) {
        this.policyMapper = policyMapper;
    }

    public List<Policy> latest(int limit) {
        return policyMapper.selectLatest(limit);
    }

    public PageResult<Policy> page(int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(policyMapper.selectPage(offset, size), policyMapper.count(), page, size);
    }

    public PageResult<Policy> publishedPage(int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(policyMapper.selectPublishedPage(offset, size), policyMapper.countPublished(), page, size);
    }

    public Policy findById(Long id) {
        return policyMapper.findById(id);
    }

    public void create(Policy policy) {
        policyMapper.insert(policy);
    }

    public void update(Policy policy) {
        policyMapper.update(policy);
    }

    public void delete(Long id) {
        policyMapper.deleteById(id);
    }

    public PageResult<Policy> search(String kw, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(policyMapper.search(kw, offset, size), policyMapper.searchCount(kw), page, size);
    }
}
