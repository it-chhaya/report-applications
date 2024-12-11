package co.istad.reporting.domain.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products", schema = "app_user")
public class Product {

    @Id
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "STOCK_QUANTITY")
    private Integer stockQuantity;

}
