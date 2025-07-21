package com.ticketplaza.ddd.domain.service.impl;

import com.ticketplaza.ddd.domain.repository.HiDomainRepository;
import com.ticketplaza.ddd.domain.service.HiDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HiDomainServiceImpl implements HiDomainService {
    private final HiDomainRepository hiDomainRepository;

    @Override
    public String sayHi(String Who) {
        return hiDomainRepository.sayHi(Who);
    }
}
