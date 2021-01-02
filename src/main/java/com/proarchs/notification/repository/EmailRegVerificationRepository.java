package com.proarchs.notification.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proarchs.notification.model.RegVerificationInfo;



@Repository
public interface EmailRegVerificationRepository extends CrudRepository<RegVerificationInfo, Integer>{
	
}
