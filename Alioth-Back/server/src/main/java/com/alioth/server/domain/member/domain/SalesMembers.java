package com.alioth.server.domain.member.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.member.dto.req.SMAdminUpdateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdateReqDto;
import com.alioth.server.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesMembers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private Long salesMemberCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 19)
    private String phone;

    @Column(nullable = false, length = 18)
    private String name;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false)
    private String birthDay;

    @Column(nullable = false, length = 200)
    private String zoneCode;

    @Column(nullable = false)
    private String roadAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column
    private String profileImage;

    @Column
    private String officeAddress;

    @Column
    private String extensionNumber;

    @Builder.Default
    @Column(nullable = false)
    private String quit = "N";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesMemberType rank;

    @Builder.Default
    @Column(nullable = false)
    private String performanceReview = "C";

    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;


    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void updateAdmin(SMAdminUpdateReqDto dto, Team team){
        this.rank = dto.rank();
        this.team = team;
        this.performanceReview = dto.performanceReview();
    }

    public void updateMyInfo(SalesMemberUpdateReqDto dto){
        this.email = dto.email();
        this.phone = dto.phone();
        this.zoneCode = dto.zoneCode();
        this.roadAddress = dto.roadAddress();
        this.detailAddress = dto.detailAddress();
        this.birthDay = dto.birthDay();
        this.name = dto.name();
        this.profileImage = dto.profileImage();
        this.officeAddress = dto.officeAddress();
        this.extensionNumber = dto.extensionNumber();
    }

    public void exitTeam(){
        this.team = null;
    }
    public void updateTeam(Team team){
        this.team= team;
    }

    public void deleteMember(){
        this.quit = "Y";}
}
