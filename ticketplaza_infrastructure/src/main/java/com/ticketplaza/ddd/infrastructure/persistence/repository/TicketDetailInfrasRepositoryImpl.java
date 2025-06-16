package com.ticketplaza.ddd.infrastructure.persistence.repository;

import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import com.ticketplaza.ddd.domain.repository.TicketDetailRepository;
import com.ticketplaza.ddd.infrastructure.persistence.mapper.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TicketDetailInfrasRepositoryImpl implements TicketDetailRepository {
    @Autowired
    private TicketDetailJPAMapper ticketDetailJPAMapper;

    @Override
    public Optional<TicketDetail> findById(Long id) {
        log.info("Implement Infrastructure: {}", id);
        return ticketDetailJPAMapper.findById(id);
    }
}
