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

package io.mapping.api.billsplit.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.mapping.api.billsplit.entities.BillSetEntity;
import io.mapping.api.billsplit.entities.BillSetUserEntity;
import io.mapping.api.billsplit.entities.UserEntity;
import io.mapping.api.billsplit.sessions.SessionAttributes;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Path("/billset")
@Singleton
public class BillSetResource {
	@Inject
	private HttpServletRequest mRequest;
	@Inject
	private HttpServletResponse mResponse;
	@Inject
	private EntityManager mEntityManager;
	@Inject
	private SessionAttributes mSessionAttributes;

	@GET
	@Path("/user/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<BillSetEntity> getId(@PathParam("userId") final String userId) {
		if (userId == null || userId.equals("")) {
			return null;
		}

		UserEntity currentUser = (UserEntity) mRequest.getSession().getAttribute(mSessionAttributes.getAttribute(SessionAttributes.Attribute.USER));
		if (currentUser == null) {
			return null;
		}

		// Iterate over the bill sets that the current user is a part of
		Iterator<BillSetUserEntity> billSetUserEntityIterator = mEntityManager
				.createNamedQuery("billSetUserEntity.findByUserId", BillSetUserEntity.class)
				.setParameter("id", currentUser.getId())
				.getResultList()
				.iterator();

		List<BillSetEntity> billSetEntities = new LinkedList<>();
		while (billSetUserEntityIterator.hasNext()) {
			try {
				billSetEntities.add(mEntityManager
						.createNamedQuery("billSetEntity.findById", BillSetEntity.class)
						.setParameter("id", userId)
						.getSingleResult());
			} catch (NoResultException ex) {
				/**
				 * Swallow the exception because it is dumb, although this should never be reached
				 * given the iterator
				 */
			}
		}

		return billSetEntities;
	}
}
