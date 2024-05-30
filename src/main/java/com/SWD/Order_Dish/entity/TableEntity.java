package com.SWD.Order_Dish.entity;

import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TABLES")
@ToString
public class TableEntity {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
    @Column(name = "TABLE_ID", length = 36)
    private String tableId;

    @Column(name = "NAME", nullable = false)
    private String name;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AREA_ID")
    private AreaEntity areaEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "tableEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orderEntities = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TableEntity that = (TableEntity) o;
        return getTableId() != null && Objects.equals(getTableId(), that.getTableId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
