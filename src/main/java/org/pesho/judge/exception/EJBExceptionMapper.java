package org.pesho.judge.exception;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<javax.ejb.EJBException> {

	public Response toResponse(EJBException exception) {
		if (exception instanceof EJBAccessException) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

}