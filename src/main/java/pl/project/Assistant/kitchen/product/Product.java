package pl.project.Assistant.kitchen.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.auth.User;

@Entity
@Table(name = "kit_product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;





}
