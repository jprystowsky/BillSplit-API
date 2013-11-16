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
import io.mapping.api.billsplit.entities.UserEntity;
import io.mapping.api.billsplit.sessions.SessionUserProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/billset")
@Singleton
public class BillSetResource {
	@Context
	private HttpServletRequest mRequest;
	@Inject
	private EntityManagerFactory mEntityManagerFactory;
	@Inject
	private SessionUserProvider mSessionUserProvider;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BillSetEntity> get() {
		UserEntity currentUser = mSessionUserProvider.getUser(mRequest.getSession());
		if (currentUser == null) {
			return null;
		}

		EntityManager em = mEntityManagerFactory.createEntityManager();

		Collection<BillSetEntity> billSetEntities = em.createNamedQuery("billSetEntity.findByUserId", BillSetEntity.class)
				.setParameter("userId", currentUser.getId())
				.getResultList();

		// Detach so we can safely return them
		Iterator<BillSetEntity> billSetEntityIterator = billSetEntities.iterator();
		while (billSetEntityIterator.hasNext()) {
			em.detach(billSetEntityIterator.next());
		}

		em.close();

		return billSetEntities;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BillSetEntity create(BillSetEntity templateEntity) {
		if (templateEntity == null || templateEntity.getName().equals("")) {
			return null;
		}

		UserEntity currentUser = mSessionUserProvider.getUser(mRequest.getSession());
		if (currentUser == null) {
			return null;
		}

		EntityManager em = mEntityManagerFactory.createEntityManager();

		// Insert the current user into the list of users
		Set<UserEntity> userEntities = new HashSet<>(em.createNamedQuery("userEntity.findById", UserEntity.class)
				.setParameter("id", currentUser.getId())
				.getResultList());
		templateEntity.setUsers(userEntities);

		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			em.persist(templateEntity);

			tx.commit();

			// Detach for return
			em.detach(templateEntity);
		} catch (RuntimeException ex) {
			tx.rollback();
		} finally {
			em.close();
		}

		return templateEntity;
	}
}
