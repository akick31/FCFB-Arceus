package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.RequestMessageLog
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestMessageLogRepository : CrudRepository<RequestMessageLog?, String?>
