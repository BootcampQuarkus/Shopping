package com.quarkus.bootcamp.nttdata.domain.mapper;

import com.quarkus.bootcamp.nttdata.domain.entity.Shopping;
import com.quarkus.bootcamp.nttdata.domain.interfaces.IMapper;
import com.quarkus.bootcamp.nttdata.infraestructure.entity.shopping.ShoppingD;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShoppingMapper implements IMapper<Shopping, ShoppingD> {
  @Override
  public Shopping toDto(ShoppingD shoppingD) {
    Shopping shopping = new Shopping();
    shopping.setId(shoppingD.id);
    shopping.setAmount(shoppingD.getAmount());
    shopping.setDescription(shoppingD.getDescription());
    shopping.setProductId(shoppingD.getProductId());
    shopping.setDate(shoppingD.getCreatedAt());
    return shopping;
  }

  @Override
  public ShoppingD toEntity(Shopping payment) {
    ShoppingD shoppingD = new ShoppingD() ;
    shoppingD.setAmount(payment.getAmount());
    shoppingD.setDescription(payment.getDescription());
    shoppingD.setProductId(payment.getProductId());
    return shoppingD;
  }
}
