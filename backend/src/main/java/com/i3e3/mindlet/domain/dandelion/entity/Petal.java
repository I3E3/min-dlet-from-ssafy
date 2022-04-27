package com.i3e3.mindlet.domain.dandelion.entity;

import com.i3e3.mindlet.domain.admin.entity.Report;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_petal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "message", "imagePath", "nation", "city", "nationalFlagImagePath", "isDeleted"})
public class Petal extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petal_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(length = 100)
    private String message;

    private String imagePath;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String nationalFlagImagePath;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dandelion_seq", nullable = false)
    private Dandelion dandelion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "petal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Petal(String message, String imagePath, String nation, String city, String nationalFlagImagePath, Dandelion dandelion, Member member) {
        this.message = message;
        this.imagePath = imagePath;
        this.nation = nation;
        this.city = city;
        this.nationalFlagImagePath = nationalFlagImagePath;
        this.dandelion = dandelion;
        this.member = member;

        dandelion.getPetals().add(this);
        member.getPetals().add(this);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
