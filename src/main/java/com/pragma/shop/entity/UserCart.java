package com.pragma.shop.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_cart", indexes = {
        @Index(name = "user_product_idx", columnList = "app_user_id, product_id, active"),
        @Index(name = "user_idx", columnList = "app_user_id, active")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserCart implements Serializable {

    private static final long serialVersionUID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    @ToString.Exclude
    private AppUser appUser;

    @Column(name = "app_user_id", insertable = false, updatable = false)
    private Long appUserId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @Column(name = "order_count")
    private Integer orderCount;

    @Column(name = "active")
    private Boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserCart userCart = (UserCart) o;
        return id != null && Objects.equals(id, userCart.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
