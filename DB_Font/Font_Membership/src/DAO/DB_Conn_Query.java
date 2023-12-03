package DAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import oracle.jdbc.OracleTypes;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DB_Conn_Query {
   String url = "jdbc:oracle:thin:@localhost:1521:XE";
   
   Connection con = null;
   CallableStatement cstmt = null;
   Statement stmt = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;
   
   String id = "fonts";      
   String password = "1234";
   
   public DB_Conn_Query() {
     try {
    	Class.forName("oracle.jdbc.driver.OracleDriver");
        System.out.println("드라이버 적재 성공");
     } catch (ClassNotFoundException e) { System.out.println("No Driver."); }  
   }
   private void DB_Connect() {
     try {
         con = DriverManager.getConnection(url, id, password);
         System.out.println("DB 연결 성공");
     } catch (SQLException e) {         System.out.println("Connection Fail");      }
   }
   
   public void DB_Close() {
       try {
           if (con != null) {
               con.close();
               System.out.println("Connection 종료 성공");
           }
           if(cstmt != null) {
        	   cstmt.close();
        	   System.out.println("CallableStatement 종료 성공");
           }
           if(stmt != null) {
        	   stmt.close();
        	   System.out.println("Statement 종료 성공");
           }
           if(rs != null) {
        	   rs.close();
        	   System.out.println("ResultSet 종료 성공");
           }
           if(pstmt != null) {
        	   pstmt.close();
        	   System.out.println("PreparedStatement 종료 성공");
           }
       } catch (SQLException e) {
           System.out.println("Close Fail");
       }
   }


   public Map<String, Object> ft_1_callable(String memname) {   // 멤버십정보 출력 두개의 값을 리턴
	   Map<String, Object> result = new HashMap<>();
	   try {DB_Connect();
	   	   cstmt = con.prepareCall("{call ft_1(?,?,?)}");
		   cstmt.setString(1, memname);//멤버십이름값 넘겨줌
		   cstmt.registerOutParameter(2, OracleTypes.CURSOR);//멤버십정보
		   cstmt.registerOutParameter(3, OracleTypes.CURSOR);//폰트정보
		   cstmt.executeQuery();
		   rs = (ResultSet)cstmt.getObject(2);
		   while( rs.next( ) ) {
			   result.put("membershipName", rs.getString(1));// 멤버십 정보를 저장
               result.put("membershipPeriod", rs.getInt(2));
               result.put("membershipPrice", rs.getInt(3));
               result.put("membershipProducer", rs.getString(4));
		   }
		   ResultSet rs1 = (ResultSet)cstmt.getObject(3);
		
           StringBuilder fontList = new StringBuilder();// 폰트 목록을 저장
		   while( rs1.next( ) ) {
			   fontList.append(rs1.getString(1)).append("\t");
		   }
		   result.put("fontList", fontList.toString());

		   rs1.close();
		   DB_Close();
	   } catch (SQLException e) { e.printStackTrace(); }
	   return result;
   }


   public String[][] ft_2_callable(String a) {   // 회원이 가입한 멤버십 출력
	   List<String[]> dataList = new ArrayList<>();
	   try {DB_Connect();
		   cstmt = con.prepareCall("{call ft_2(?,?)}");
		   cstmt.setString(1, a);//"aaa"자리에 회원아이디 변수
		   cstmt.registerOutParameter(2, OracleTypes.CURSOR);
		   cstmt.executeQuery();
		   rs = (ResultSet)cstmt.getObject(2);
		   
		   while( rs.next( ) ) {
			   String membership = rs.getString(1);//멤버십이름
               String period = rs.getString(2);//멤버십기간
               String producer = rs.getString(3);//제작사명
               dataList.add(new String[]{membership, period, producer});
		   }
		   DB_Close();
	   } catch (SQLException e) { e.printStackTrace(); }
	   String[][] joinData = new String[dataList.size()][];
	   dataList.toArray(joinData);
	   return joinData;
   }

   public Object[][] ft_3_callable() {   //폰트랭킹 프로시저 가져오는 콜러블
	   List<Object[]> dataList = new ArrayList<>();
	   try {DB_Connect();
		   cstmt = con.prepareCall("{call ft_3(?)}");
		   cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		   cstmt.executeQuery();
		   rs = (ResultSet)cstmt.getObject(1);

		   int q=1;//랭킹
		   while( rs.next( ) ) {
			   Object a=rs.getObject(1);//폰트이름
			   Object b=rs.getObject(2);//다운로드수
			   BigDecimal c = null;
	            if (rs.getBigDecimal(3) != null) {
	                c = rs.getBigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP); // 평균평점 소수점 2번째 자리까지만
	            }
			   dataList.add(new Object[]{q, a, b, c});
			   q++;
		   }
		   DB_Close();
	   } catch (SQLException e) { e.printStackTrace(); }
	   Object[][] data = new Object[dataList.size()][];
	   dataList.toArray(data);
	   return data;
   }   
   
   
   public List<String[]> membershipAllList() { // 멤버쉽 전체 출력
	    List<String[]> dataList = new ArrayList<>();

	    try {
	        DB_Connect();
	        String query = "SELECT 멤버십.*, 제작사.제작사명 FROM 멤버십 JOIN 제작사 ON 멤버십.제작사번호 = 제작사.제작사번호";
	        
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(query);
	        
	        while (rs.next()) {
	            String membershipName = rs.getString("멤버십이름");
	            String membershipPeriod = rs.getString("제작사명");
	            String membershipDate = dateFormat(rs.getInt("멤버십기간"));
	            String membershipPrice = rs.getString("멤버십가격");

	            dataList.add(new String[]{membershipName, membershipPeriod, membershipDate, membershipPrice});
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return dataList;
	}

   public String dateFormat(int mon) { // 멤버십 전체 출력 기간 포맷 형태 수정
       // 현재 날짜 가져오기
       LocalDate currentDate = LocalDate.now();
       LocalDate futureDate = currentDate.plusMonths(mon);

       // 날짜 출력 형식 정의
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd"); 
       String memDate = currentDate.format(formatter) + "~" + futureDate.format(formatter) + "(" + mon +"개월)";
       
       return memDate;
   }

   public List<String[]> searchMembershipsByName(String searchName) { // 검색한 단어가 포함되는 멤버쉽 출력
       List<String[]> dataList = new ArrayList<>();

       try {
		   DB_Connect();
		   String query = "SELECT 멤버십.*, 제작사.제작사명 FROM 멤버십 JOIN 제작사 ON 멤버십.제작사번호 = 제작사.제작사번호 WHERE 멤버십이름 LIKE ?";
		   pstmt = con.prepareStatement(query);
		   pstmt.setString(1, "%" + searchName + "%");

		   rs = pstmt.executeQuery();

		   while (rs.next()) {
			   String membershipName = rs.getString("멤버십이름");
			   String membershipPeriod = rs.getString("제작사명");
			   String membershipDate = dateFormat(rs.getInt("멤버십기간"));
			   String membershipPrice = rs.getString("멤버십가격");
			
			    dataList.add(new String[]{membershipName, membershipPeriod, membershipDate, membershipPrice});
			}
		   DB_Close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return dataList;
   }
   
   public boolean login(String Id, String Password) {
       try {DB_Connect();
           String query = "SELECT * FROM 회원 WHERE 회원아이디 = ? AND 비밀번호 = ?";
           pstmt = con.prepareStatement(query);
           pstmt.setString(1, Id);
           pstmt.setString(2, Password);

           rs = pstmt.executeQuery();

           if (rs.next()) {
               JOptionPane.showMessageDialog(null, "로그인 성공.");
               DB_Close();
               return true;
           } else {
               JOptionPane.showMessageDialog(null, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.");
               DB_Close();
               return false;
           }
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }
   
   public void SignIn(String Id, String Password, String Name, String Phone) {
       try {DB_Connect();
           String query = "INSERT INTO 회원 (회원아이디, 비밀번호, 회원이름, 전화번호) VALUES (?, ?, ?, ?)";
           pstmt = con.prepareStatement(query);

           pstmt.setString(1, Id);
           pstmt.setString(2, Password);
           pstmt.setString(3, Name);
           pstmt.setString(4, Phone);

           pstmt.executeUpdate();

           JOptionPane.showMessageDialog(null, "회원가입 완료.");

           DB_Close();
       } catch (SQLException e) {
           e.printStackTrace();
           JOptionPane.showMessageDialog(null, "회원가입 실패.");
       }
   }
   
   public int isMembershipPaid(String membershipName, String fontID) throws SQLException { // 멤버십 결제 내역 유무 확인
       
       try{
    	   DB_Connect();
    	   
    	   String query = "SELECT COUNT(*) FROM 결제 JOIN 멤버십 ON 결제.멤버십번호 = 멤버십.멤버십번호 WHERE 결제.회원아이디 = ? AND 멤버십.멤버십이름 = ?";

    	   pstmt = con.prepareStatement(query);
    	   pstmt.setString(1, fontID);
    	   pstmt.setString(2, membershipName);

    	   rs = pstmt.executeQuery();
    	   
           if (rs.next()) {
               int count = rs.getInt(1);
               return count; // count가 0보다 크면 멤버십이 결제된 것
           }
           
           DB_Close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       DB_Close();
       return 0; // 오류 발생 시 기본값은 0으로 설정
   }
   
   public void paymentMembership(String membershipName, String fontID) throws SQLException { // 멤버십 결제
       
       try{
    	   DB_Connect();
    	   
    	   String query = "SELECT 멤버십가격, 멤버십번호 FROM 멤버십 WHERE 멤버십이름 = ?";
    	   int price = 0;
    	   int membershipNum = 0;
    			   
		   pstmt = con.prepareStatement(query);
           pstmt.setString(1, membershipName);
           
           rs = pstmt.executeQuery();
		   
           if (rs.next()) {
        	   price = rs.getInt(1);
        	   membershipNum = rs.getInt(2);
           }

		   query = "INSERT INTO 결제 (결제번호, 결제금액, 회원아이디, 멤버십번호) VALUES (payment_seq.NEXTVAL, ?, ?, ?)";
		   pstmt = con.prepareStatement(query);

           pstmt.setInt(1, price);
           pstmt.setString(2, fontID);
           pstmt.setInt(3, membershipNum);
           pstmt.executeUpdate();
           
           DB_Close();
           
           } catch (SQLException e) {
               e.printStackTrace();
           }
   }
   
   
   
  
public static void main(String arg[]) throws SQLException {
       DB_Conn_Query dbconquery = new DB_Conn_Query();
    }
}
