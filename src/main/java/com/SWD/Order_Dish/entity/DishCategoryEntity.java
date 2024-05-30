package com.SWD.Order_Dish.entity;

import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DISH_CATEGORY")
@ToString
public class DishCategoryEntity {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
    @Column(name = "DISH_CATEGORY_ID", length = 36)
    private String dishCateGoryId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

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

    @OneToMany(mappedBy = "dishCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DishEntity> dishEntities = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DishCategoryEntity that = (DishCategoryEntity) o;
        return getDishCateGoryId() != null && Objects.equals(getDishCateGoryId(), that.getDishCateGoryId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
