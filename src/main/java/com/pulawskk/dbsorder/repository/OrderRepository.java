package com.pulawskk.dbsorder.repository;

import com.pulawskk.dbsorder.domain.Order;
import com.pulawskk.dbsorder.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);

//    @Enumerated(EnumType.STRING)
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
