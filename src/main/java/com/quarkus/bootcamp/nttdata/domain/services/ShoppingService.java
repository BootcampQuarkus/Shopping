package com.quarkus.bootcamp.nttdata.domain.services;

import com.quarkus.bootcamp.nttdata.domain.entity.Shopping;
import com.quarkus.bootcamp.nttdata.domain.exceptions.AmountExceedsException;
import com.quarkus.bootcamp.nttdata.domain.exceptions.ProductNotFoundException;
import com.quarkus.bootcamp.nttdata.domain.mapper.ShoppingMapper;
import com.quarkus.bootcamp.nttdata.infraestructure.entity.product.LineOfCreditD;
import com.quarkus.bootcamp.nttdata.infraestructure.entity.shopping.ShoppingD;
import com.quarkus.bootcamp.nttdata.infraestructure.repository.ShoppingRepository;
import com.quarkus.bootcamp.nttdata.infraestructure.resources.ILineOfCreditApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class ShoppingService {
  @Inject
  ShoppingRepository repository;
  @Inject
  ShoppingMapper mapper;
  @RestClient
  ILineOfCreditApi lineOfCreditApi;

  public List<Shopping> getAll() {
    return repository.getAll()
          .stream()
          .filter(p -> (p.getDeletedAt() == null))
          .map(mapper::toDto)
          .toList();
  }

  public Shopping create(Shopping shopping) throws ProductNotFoundException, AmountExceedsException {
    ShoppingD shoppingD = mapper.toEntity(shopping);
    Long productId = shopping.getProductId();
    Double amount = shopping.getAmount();
    LineOfCreditD lineOfCreditD = this.validateProduct(productId);
    if (amount > lineOfCreditD.getAvailable()) {
      throw new AmountExceedsException("Amount Exceeds available");
    }
    shoppingD = repository.save(shoppingD);
    lineOfCreditD.setAvailable(lineOfCreditD.getAvailable() - amount);
    lineOfCreditD.setCosts(lineOfCreditD.getCosts() + amount);
    lineOfCreditApi.update(productId, lineOfCreditD);
    return mapper.toDto(shoppingD);
  }

  public LineOfCreditD validateProduct(Long productId) throws ProductNotFoundException {
    LineOfCreditD lineOfCreditD = lineOfCreditApi.getById(productId);
    if (lineOfCreditD.getId() != null) return lineOfCreditD;
    throw new ProductNotFoundException("Product not found.");
  }
}
