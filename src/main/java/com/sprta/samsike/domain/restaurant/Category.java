package com.sprta.samsike.domain.restaurant;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(catalog = "samsike", name = "p_category")
@NoArgsConstructor
public class Category extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String category;


    public Category(String category) {
        this.category = category;
    }


    public void delete(String deletedBy) {
        super.setDeletedBy(deletedBy);
    }

}

