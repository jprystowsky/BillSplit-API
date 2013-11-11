package io.mapping.api.billsplit.resource;

import com.google.inject.Inject;
import io.mapping.api.billsplit.models.Test;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
	@Inject
	private EntityManager mEntityManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getHello() {
		return "Hello, world!";
	}

	@GET
	@Path("name/{name : [a-zA-Z]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getHello(@PathParam("name") String name) {
		return "Hello, " + name + "!";
	}

	@GET
	@Path("id/{id : \\d}")
	@Produces(MediaType.APPLICATION_JSON)
	public Test getHello(@PathParam("id") Long id) {
		Test test = mEntityManager
				.createNamedQuery("test.findById", Test.class)
				.setParameter("id", id)
				.getSingleResult();

		if (test == null) {
			throw new RuntimeException("No person for that id");
		}

		return test;
	}
}
