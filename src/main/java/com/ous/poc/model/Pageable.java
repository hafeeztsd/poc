package com.ous.poc.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Pageable {

	private Integer pageNo;
	private Integer pageSize;

	public static class PageableBuilder {

		public PageableBuilder pageNo(Integer pageNo) {
			this.pageNo = pageNo - 1;
			return this;
		}
	}

}
