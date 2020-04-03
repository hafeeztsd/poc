package com.ous.poc.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ous.poc.entity.Task;
import com.ous.poc.service.impl.TaskServiceImpl;

/**
 * Task repository to interact with the database. PagingAndSortingRepository
 * provide methods related pagination and sorting. Check {@link TaskServiceImpl}
 * for more details.
 * 
 * @author abdulhafeez
 *
 */
public interface TaskRepository extends PagingAndSortingRepository<Task, String> {

}
