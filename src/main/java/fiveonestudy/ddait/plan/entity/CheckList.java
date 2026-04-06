package fiveonestudy.ddait.plan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "check_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 5000)
    private String checkContent;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private LocalDate date;

    public void updateCompleted(boolean completed) {
        this.completed = completed;
    }
}