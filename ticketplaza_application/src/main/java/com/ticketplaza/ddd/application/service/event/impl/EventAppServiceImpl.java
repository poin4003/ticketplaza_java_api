package com.ticketplaza.ddd.application.service.event.impl;

import com.ticketplaza.ddd.application.service.event.EventAppService;
import com.ticketplaza.ddd.domain.service.impl.HiDomainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventAppServiceImpl implements EventAppService {

    @Autowired
    private HiDomainServiceImpl hiDomainServiceImpl;

    @Override
    public String sayHi(String who) {
        return hiDomainServiceImpl.sayHi(who);
    }
}
