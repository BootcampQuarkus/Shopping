package com.quarkus.bootcamp.nttdata.application;

import com.quarkus.bootcamp.nttdata.domain.entity.Shopping;
import com.quarkus.bootcamp.nttdata.domain.exceptions.AmountExceedsException;
import com.quarkus.bootcamp.nttdata.domain.exceptions.ProductNotFoundException;
import com.quarkus.bootcamp.nttdata.domain.services.ShoppingService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/shopping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingResource {
  @Inject
  ShoppingService service;
  @GET
  public Response getAll(@QueryParam("productId") Long productId) {
    List<Shopping> shoppingList = service.getAll();
    if (productId != null)
      shoppingList = shoppingList.stream()
            .filter(p -> (p.getProductId() == productId))
            .toList();
    return Response.ok(shoppingList).build();
  }

  @POST
  @Transactional
  public Response deposit(Shopping shopping) {
    try {
      return Response.ok(service.create(shopping)).status(201).build();
    } catch (ProductNotFoundException | AmountExceedsException e) {
      return Response.ok(e.getMessage()).status(404).build();
    }
  }
}
