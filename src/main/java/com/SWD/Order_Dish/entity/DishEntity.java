    package com.SWD.Order_Dish.entity;

    import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.GenericGenerator;
    import org.hibernate.annotations.UpdateTimestamp;
    import org.hibernate.proxy.HibernateProxy;

    import java.util.Date;
    import java.util.Objects;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "DISH")
    @ToString
    public class DishEntity {
        @Id
        @GeneratedValue(generator = "custom-id")
        @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
        @Column(name = "DISH_ID", length = 36)
        private String dishId;

        @Column(name = "IMAGE_URL", nullable = false)
        private String imageURL;

        @Column(name = "NAME", nullable = false)
        private String name;

        @Column(name = "DISH_PRICE", nullable = false)
        private double dishPrice;

        @Column(name = "IS_AVAILABLE", nullable = false)
        private Boolean isAvailable;

        @Column(name = "CREATED_DATE", nullable = false)
        @CreationTimestamp
        private Date createdDate;

        @Column(name = "CREATED_by", nullable = false)
        private String createdBy;

        @Column(name = "UPDATED_DATE", nullable = false)
        @UpdateTimestamp
        private Date updatedDate;

        @Column(name = "UPDATED_BY", nullable = false)
        private String updatedBy;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
        @JoinColumn(name = "DISH_CATEGORY_ID", referencedColumnName = "DISH_CATEGORY_ID", nullable = false)
        private DishCategoryEntity dishCategoryEntity;

        @ToString.Exclude
        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "ORDER_DETAIL_ID")
        private OrderDetailEntity orderDetailEntity;

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
            Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
            if (thisEffectiveClass != oEffectiveClass) return false;
            DishEntity that = (DishEntity) o;
            return getDishId() != null && Objects.equals(getDishId(), that.getDishId());
        }

        @Override
        public final int hashCode() {
            return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
        }
    }
