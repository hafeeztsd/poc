package com.ous.poc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

/**
 * Task entity mapped to the task table.
 * 
 * @author abdulhafeez
 *
 */
@Getter
@Setter
@Audited
@Entity
@Table(name = "task", catalog = "task_management")
public class Task extends AuditEntity {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Column(nullable = false)
	private String title;

	@Column
	private String description;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private int priority;

	@Column(name = "delay_in_seconds")
	private int delayInSeconds;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "resolved_at", nullable = true, length = 19)
	private Date resolvedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_at", nullable = false, length = 19)
	private Date dueAt;

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", delayInSeconds=" + delayInSeconds + "]";
	}

}
