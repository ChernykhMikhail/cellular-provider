package dev.chernykh.cellular.api.tariff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tariffs")
@Data
public class Tariff {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String tariffName;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tariff_id")
    private List<Option> options = new ArrayList<>();
}
