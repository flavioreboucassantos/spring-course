package com.springcourse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageRequestModel {
	private int page = 0;
	private int size = 10;
	private String sort = "";
	private String search = "";

	public PageRequestModel(Map<String, String> params) {
		if (params.containsKey("page") && !params.get("page").trim().equals(""))
			page = Integer.parseInt(params.get("page"));

		if (params.containsKey("size") && !params.get("size").trim().equals(""))
			size = Integer.parseInt(params.get("size"));

		if (params.containsKey("sort"))
			sort = params.get("sort");
		
		if (params.containsKey("search"))
			search = params.get("search");
	}

	public PageRequest toStringPageRequest() {
		List<Order> orders = new ArrayList<>();

		String[] properties = sort.split(",");
		for (String property : properties) {
			if (property.trim().length() > 0) {
				String column = property.trim();
				if (column.startsWith("-")) {
					column = column.replace("-", "").trim();
					orders.add(Order.desc(column));
				} else {
					orders.add(Order.asc(column));
				}
			}
		}

		return PageRequest.of(page, size, Sort.by(orders));
	}
}
