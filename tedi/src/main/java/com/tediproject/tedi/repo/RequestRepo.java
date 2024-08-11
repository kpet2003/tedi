package com.tediproject.tedi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tediproject.tedi.model.Request;

public interface  RequestRepo  extends JpaRepository<Request, Long>{
    
    @Query("select r from Request r where r.receiver = ?1 order by r.date_sent asc")
    List <Request> findReceiver(long receiver_id);
}
