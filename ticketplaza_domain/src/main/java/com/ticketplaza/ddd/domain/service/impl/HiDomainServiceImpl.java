package com.ticketplaza.ddd.domain.service.impl;

import com.ticketplaza.ddd.domain.repository.HiDomainRepository;
import com.ticketplaza.ddd.domain.service.HiDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HiDomainServiceImpl implements HiDomainService {

    @Autowired
    private HiDomainRepository hiDomainRepository;

    @Override
    public String sayHi(String Who) {
        return hiDomainRepository.sayHi(Who);
    }
}
