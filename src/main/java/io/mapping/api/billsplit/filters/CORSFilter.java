/*
 * Copyright 2013 Jacob Miles Prystowsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mapping.api.billsplit.filters;

import com.google.inject.Singleton;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter used to add Cross-Origin Resource Sharing support by adding output headers.
 */
@Singleton
public class CORSFilter implements Filter, ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		return addCorsSupport(request, response);
	}

	/**
	 * Add CORS to this {@link ContainerResponse}, optionally including ACRH headers from the {@link ContainerRequest}
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	private ContainerResponse addCorsSupport(ContainerRequest request, ContainerResponse response) {
		// Add CORS headers outbound
		response.getHttpHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
		response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.getHttpHeaders().add("Access-Control-Allow-Credentials", true);

		String reqHead = request.getHeaderValue("Access-Control-Request-Headers");
		if (reqHead != null) {
			response.getHttpHeaders().add("Access-Control-Allow-Headers", reqHead);
		}
		return response;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
