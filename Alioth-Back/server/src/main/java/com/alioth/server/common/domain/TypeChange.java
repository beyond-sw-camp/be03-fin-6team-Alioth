package com.alioth.server.common.domain;

import com.alioth.server.domain.answer.domain.Answer;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.answer.dto.res.AnswerResDto;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.dummy.domain.ContractMembers;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.dummy.domain.InsuranceProduct;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamResDto;
import com.alioth.server.domain.team.dto.TeamReqDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeChange {

    // 사원
    public LoginResDto memberToLoginResDto(SalesMembers findMember, String accessToken, String refreshToken) {
        return LoginResDto.builder()
                .memberCode(findMember.getSalesMemberCode())
                .name(findMember.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public SalesMembers salesMemberCreateReqDtoToSalesMembers(SalesMemberCreateReqDto dto, Long salesMemberCode, String encodePassword) {
        SalesMembers member = SalesMembers.builder()
                .salesMemberCode(salesMemberCode)
                .email(dto.email())
                .phone(dto.phone())
                .name(dto.name())
                .password(encodePassword)
                .birthDay(dto.birthDay())
                .address(dto.address())
                .rank(dto.rank())
                .build();

        return member;
    }

    public SMTeamListResDto smToSmTeamListResDto(SalesMembers member){
        return SMTeamListResDto.builder()
                .rank(member.getRank())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .salesMemberCode(member.getSalesMemberCode())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }

    public SalesMemberResDto smToSmResDto(SalesMembers member){
        return SalesMemberResDto.builder()
                .rank(member.getRank())
                .salesMemberCode(member.getSalesMemberCode())
                .birthDay(member.getBirthDay())
                .performanceReview(member.getPerformanceReview())
                .teamCode(member.getTeam().getTeamCode())
                .teamName(member.getTeam().getTeamName())
                .address(member.getAddress())
                .officeAddress(member.getOfficeAddress())
                .extensionNumber(member.getExtensionNumber())
                .phone(member.getPhone())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }


    // 팀
    public TeamResDto teamToTeamReqDto(Team team, List<SMTeamListResDto> list){
        return TeamResDto.builder()
                .teamCode(team.getTeamCode())
                .teamName(team.getTeamName())
                .teamManagerCode(team.getTeamManagerCode())
                .teamMemberList(list)
                .build();
    }

    public Team teamCreateDtoToTeam(TeamReqDto dto, String teamCode){
        return Team.builder()
                .teamCode(teamCode)
                .teamName(dto.teamName())
                .teamManagerCode(dto.teamManagerCode())
                .build();
    }


    // 계약
    public Contract ContractCreateDtoToContract(ContractCreateDto dto, ContractMembers contractMembers, Custom custom, InsuranceProduct insuranceProduct, SalesMembers salesMember) {
        return Contract.builder()
                .contractCode(dto.contractCode())
                .contractDate(dto.contractDate())
                .contractExpireDate(dto.contractExpireDate())
                .contractPeriod(dto.contractPeriod())
                .contractTotalPrice(dto.contractTotalPrice())
                .contractPaymentAmount(dto.contractPaymentAmount())
                .contractPaymentFrequency(dto.contractPaymentFrequency())
                .contractPaymentMaturityInstallment(dto.contractPaymentMaturityInstallment())
                .contractCount(dto.contractCount())
                .contractPaymentMethod(dto.contractPaymentMethod())
                .contractPayer(dto.contractPayer())
                .contractConsultation(dto.contractConsultation())
                .contractStatus(dto.contractStatus())
                .contractMembers(contractMembers)
                .custom(custom)
                .insuranceProduct(insuranceProduct)
                .salesMembers(salesMember)
                .build();
    }

    public ContractResDto ContractToContractResDto(Contract contract) {
        return ContractResDto.builder()
                .contractId(contract.getContractId())
                .contractCode(contract.getContractCode())
                .contractDate(contract.getContractDate())
                .contractExpireDate(contract.getContractExpireDate())
                .contractPeriod(contract.getContractPeriod())
                .contractTotalPrice(contract.getContractTotalPrice())
                .contractPaymentAmount(contract.getContractPaymentAmount())
                .contractPaymentFrequency(contract.getContractPaymentFrequency())
                .contractPaymentMaturityInstallment(contract.getContractPaymentMaturityInstallment())
                .contractCount(contract.getContractCount())
                .contractPaymentMethod(contract.getContractPaymentMethod())
                .contractPayer(contract.getContractPayer())
                .contractConsultation(contract.getContractConsultation())
                .contractStatus(contract.getContractStatus())
                .insuranceProductName(contract.getInsuranceProduct() != null ? contract.getInsuranceProduct().getInsuranceName() : null)
                .customName(contract.getCustom() != null ? contract.getCustom().getCustomerName() : null)
                .contractMemberName(contract.getContractMembers() != null ? contract.getContractMembers().getCM_name() : null)
                .build();
    }


    // 일정
    public Schedule ScheduleCreateDtoToSchedule(ScheduleReqDto scheduleReqDto, SalesMembers salesMembers){
        return Schedule.builder()
                .scheduleStartTime(scheduleReqDto.scheduleStartTime())
                .scheduleEndTime(scheduleReqDto.scheduleEndTime())
                .scheduleNote(scheduleReqDto.scheduleNote())
                .scheduleType(scheduleReqDto.scheduleType())
                .allDay(scheduleReqDto.allDay())
                .salesMembers(salesMembers) // 사원
                .build();
    }

    public ScheduleResDto ScheduleToScheduleResDto(Schedule schedule){
        return ScheduleResDto.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleStartTime(schedule.getScheduleStartTime())
                .scheduleEndTime(schedule.getScheduleEndTime())
                .scheduleNote(schedule.getScheduleNote())
                .scheduleType(schedule.getScheduleType())
                .allDay(schedule.getAllDay())
                .del_yn(schedule.getScheduleDel_YN())
                .memberId(schedule.getSalesMembers().getId())
                .build();
    }


    // 게시판
    public BoardResDto BoardToBoardResDto(Board board){
        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardType(board.getBoardType())
                .memberId(board.getSalesMembers().getId())
                .build();
    }

    public Board BoardCreateDtoToBoard(BoardCreateDto boardCreateDto, SalesMembers salesMembers){
        return Board.builder()
                .title(boardCreateDto.title())
                .content(boardCreateDto.content())
                .boardType(boardCreateDto.boardType())
                .salesMembers(salesMembers)
                .build();
    }


    // 답변
    public AnswerResDto AnswerToAnswerResDto(Answer answer){
        return AnswerResDto.builder()
                .answer_id(answer.getAnswerId())
                .title(answer.getTitle())
                .content(answer.getContent())
                .answer_name(answer.getSalesMembers().getName())
                .build();
    }

    public Answer AnswerReqToAnswer(AnswerReqDto answerReqDto, SalesMembers salesMembers, Board board){
        return Answer.builder()
                .title(answerReqDto.title())
                .content(answerReqDto.content())
                .salesMembers(salesMembers)
                .board(board)
                .build();
    }
}
