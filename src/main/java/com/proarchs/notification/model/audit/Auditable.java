package com.proarchs.notification.model.audit;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

	@Column(name = "CREATED_BY", nullable = false)
	@CreatedBy
	protected U createdBy;

	@Column(name = "CREATED_TS", nullable = false)
	@CreatedDate
	@Temporal(TIMESTAMP)
	protected Date createdDate;

	@Column(name = "LAST_UPDATED_BY", nullable = false)
	@LastModifiedBy
	protected U lastModifiedBy;

	@Column(name = "LAST_UPDATED_TS", nullable = false)
	@LastModifiedDate
	@Temporal(TIMESTAMP)
	protected Date lastModifiedDate;
}