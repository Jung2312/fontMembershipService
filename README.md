# fontMembershipService
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/ORACLE-F80000?style=for-the-badge&logo=ORACLE&logoColor=white"><img src="https://img.shields.io/badge/eclipseide-2C2255?style=for-the-badge&logo=eclipseide&logoColor=white">

## <목차>
[1. 프로젝트 설명](#1-프로젝트-설명)<br/>
[2. 데이터베이스](#2-데이터베이스)<br/>
[3. 실행 화면](#-3실행-화면)<br/>
[4. 프로시저 및 트리거](#-4프로시저-및-트리거)<br/>
[6. 팀원](#6-팀원)<br/>

------------
  
## 1. 프로젝트 설명
- 프로젝트 기간: 2023.11.03 ~ 2023.12.11
- 프로젝트 목적: JAVA를 기반으로 데이터베이스를 설계하고 구현
- 구현 환경: Windows 10, ORACLE
- 개발 언어: JAVA
- 개발 도구: eclipse ide

## 2. 데이터베이스
- ERD
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/0740089c-a2c9-41ea-a5fd-1fa94089ba7b)

- 테이블 구성
* 회원 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5c9d96c5-464f-4c1a-b028-e14d5dbedcc9)

* 폰트 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/d23fe640-b850-4ba8-86f7-484200490d23)

* 즐겨찾기 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5ff5a32f-ed02-4673-b53c-4d95fb059b01)

* 제작사 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/35034e8d-d84d-4348-bf4b-090751bbf073)

* 멤버셉 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/58f706c1-cad3-4543-afb5-07cb40b4b066)

* 리뷰 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/ae128847-570d-49e7-8955-bd0cacc20d87)

* 다운로드 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5d498479-7121-4336-bc8d-01ddd546ac93)

* 결제 테이블
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/87229495-f01c-41b8-b1ac-c706790946c2)

## 3. 실행 화면
* 로그인
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/e83b2817-7d78-429d-9fbd-b5215aee813f)

* 회원가입
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5cdb7c95-2238-4e4a-8882-96966c9d4d42)

* 메인
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5fc248d2-b5a6-41c8-9d5b-9d28a05c328d)

* 멤버십 결제
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/9a0a9467-064a-44e8-87b5-98d05e90b7a9)

* 다운로드 순위
![image](https://github.com/Jung2312/fontMembershipService/assets/97083703/5b66ca8b-01bd-4ab2-98e5-517882ad9226)

## 4. 프로시저 및 트리거
- 프로시저
*
create or replace NONEDITIONABLE procedure ft_1(--멤버십 정보 출력
ft_멤버십이름 in 멤버십.멤버십이름%type, a out sys_refcursor,b out sys_refcursor)
as
begin
    open a for--멤버십 정보
        select 멤버십.멤버십이름,멤버십.멤버십기간,멤버십.멤버십가격,제작사.제작사명
        from 멤버십 join 제작사 on 멤버십.제작사번호 = 제작사.제작사번호
        where 멤버십.멤버십이름 = ft_멤버십이름;

    open b for--멤버십제작사의 폰트 정보
        select 폰트이름
        from 폰트
        where 폰트.제작사번호=(select 제작사번호
                                from 멤버십
                                where 멤버십이름 = ft_멤버십이름);
end;

*
create or replace NONEDITIONABLE procedure ft_2(--회원이 가입한 멤버쉽 출력
ft_회원아이디 in 회원.회원아이디%type, a out sys_refcursor)
as
begin
    open a for
        select 멤버십.멤버십이름,
            case
                when(결제.결제일시+멤버십.멤버십기간*30)>sysdate--기간이 남아있을경우
                then to_char((결제.결제일시+멤버십.멤버십기간*30)-sysdate,'999')||'일 '
                else '만기됨 '
            end as b,제작사.제작사명
        from 멤버십 join 결제 on 멤버십.멤버십번호 = 결제.멤버십번호 join 제작사 on 멤버십.제작사번호 = 제작사.제작사번호
        where 결제.회원아이디=ft_회원아이디;
end;

*
create or replace NONEDITIONABLE procedure ft_3(--다운로드 순위 출력
a out sys_refcursor)
as
begin
    open a for
        select 폰트.폰트이름,count(distinct 다운로드.다운로드번호)as"다운로드횟수",avg(리뷰.별점)as"평균별점"
        --distinct를 넣어야 리뷰수가 더 많을경우 다운로드횟수가 중복되어 계산되지 않음
        from 다운로드 left join 리뷰 on 다운로드.폰트번호 = 리뷰.폰트번호
        --left join으로 다운로드횟수가 0 인 폰트 조인 x
        left join 폰트 on 다운로드.폰트번호 = 폰트.폰트번호
        group by 폰트.폰트이름,폰트.폰트번호
        order by "다운로드횟수" desc, "평균별점" desc nulls last;
        --null last를 넣지 않으면 평점이0점인 값이 맨 위로 오게됨
end;

- 트리거
*
create or replace NONEDITIONABLE trigger ft_4--회원가입시 비밀번호 4자리 이상
    before insert on 회원
    for each row
begin
    if length(:new.비밀번호) <= 3 
        then raise_application_error(-20001,'비밀번호는 4자리 이상이여야 합니다.');
    end if;
end;

*
create or replace NONEDITIONABLE trigger ft_5--결제시 10% 적립금 지급
    before insert on 결제
    for each row
declare
    a number;
begin
    a := :new.결제금액*0.1;
    update 회원 
    set 적립금 = 적립금 + a
    where 회원아이디 = :new.회원아이디;
end;
