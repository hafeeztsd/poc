create table task
(
  id         				varchar(36) 			primary key,
  title 					varchar(500)            not null,
  description   			varchar(1000)           not null,
  priority					int 					not null,
  delay_in_seconds			int						default 0,
  status					varchar(255) 			not null,
  created_at 				timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_at 				timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  resolved_at 				timestamp 				null,
  due_at 					timestamp 				null
  
);
