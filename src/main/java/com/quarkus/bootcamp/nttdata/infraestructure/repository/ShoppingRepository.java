package com.quarkus.bootcamp.nttdata.infraestructure.repository;

import com.quarkus.bootcamp.nttdata.infraestructure.entity.shopping.ShoppingD;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ShoppingRepository implements IRepository<ShoppingD> {
  @Override
  public List<ShoppingD> getAll() {
    return ShoppingD.listAll(Sort.by("id"));
  }

  @Override
  public ShoppingD getById(Long id) {
    Optional<ShoppingD> shoppingD = ShoppingD.findByIdOptional(id);
    if (shoppingD.isEmpty()) {
      throw new NullPointerException("Account not found");
    }
    return shoppingD.get();
  }

  @Override
  public ShoppingD save(ShoppingD shoppingD) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu.MM.dd.HH:mm:ss");
    String date = ZonedDateTime.now(ZoneId.systemDefault()).format(formatter);
    if (shoppingD.getCreatedAt() == null) {
      shoppingD.setCreatedAt(date);
    } else {
      shoppingD.setUpdatedAt(date);
    }
    shoppingD.persist();
    return shoppingD;
  }

  @Override
  public ShoppingD softDelete(ShoppingD shoppingD) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu.MM.dd.HH:mm:ss");
    shoppingD.setDeletedAt(ZonedDateTime.now(ZoneId.systemDefault()).format(formatter));
    return this.save(shoppingD);
  }
}
