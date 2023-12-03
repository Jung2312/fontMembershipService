package Membership;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import DAO.DB_Conn_Query;
import Main.MainScreen;
import Users.LoginScreen;


@SuppressWarnings("serial")
public class MembershipScreen extends JFrame{
	
	final Color backColor = new Color(0xC6DBCF); // 배경 색
	final JLabel backImage = new JLabel(new ImageIcon("./Images/MembershipInfo.png")); // 로그인 이미지
	final ImageIcon icon = new ImageIcon("./Images/Icon.png"); // 아이콘
	
	public MembershipScreen(String merbershipName, String FontID) {
		DB_Conn_Query fdb = new DB_Conn_Query();
		Map<String, Object> membershipInfo = fdb.ft_1_callable(merbershipName);//1대신 멤버십값 받아서 넣어야함*****************
		// [GUI]
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 창닫는 x버튼
		
		setTitle("FONT MEMBERSHIP SERVICE"); // 타이틀
		
		setSize(540, 600); // 전체 창 크기
		setLocationRelativeTo(null); // 화면 중앙 배치
		
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(backColor);
		
		setIconImage(icon.getImage()); // 아이콘
		
		// 메인 화면 바로 가기 버튼
		JButton homeBtn = new JButton(new ImageIcon("./Images/HomeBtn.png"));
		homeBtn.setBounds(440,70,30,30); // 위 아래 가로 세로
		homeBtn.setBorderPainted(false); // 테두리 없애기
		homeBtn.setContentAreaFilled(false); // 투명 배경
		c.add(homeBtn);
		
		// 멤버쉽 상세 정보
		JLabel membershipName = new JLabel((String) membershipInfo.get("membershipName"));
		membershipName.setBounds(100, 70, 240, 60);
		membershipName.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		c.add(membershipName);
		
		JLabel membershipPeriod = new JLabel("기간 : "+membershipInfo.get("membershipPeriod")+"개월");
		membershipPeriod.setBounds(100, 120, 216, 60);
		membershipPeriod.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		c.add(membershipPeriod);
		
		JLabel membershipProducer = new JLabel("제작사 : "+membershipInfo.get("membershipProducer"));
		membershipProducer.setBounds(100, 160, 216, 60);
		membershipProducer.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		c.add(membershipProducer);	
		
		JLabel membershipPrice = new JLabel("금액 : "+membershipInfo.get("membershipPrice")+"원");
		membershipPrice.setBounds(100, 200, 216, 60);
		membershipPrice.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		c.add(membershipPrice);	
		
		String fList = (String) membershipInfo.get("fontList");// Map에서 폰트 목록을 가져옴
		String[] fontArray = fList.split("\t");// 폰트 목록을 배열로 변환 (여기서는 간단하게 줄바꿈으로 나누어 배열에 저장)
		
		// 사용 가능 폰트 테이블
		String fontData[][] = new String[fontArray.length][1];
		for (int i = 0; i < fontArray.length; i++) {
		    fontData[i][0] = fontArray[i];
		} // 2차원 배열에 폰트 목록을 넣기
		String fontHeader[] = { "사용 가능 폰트"};
		
		DefaultTableModel fontList = new DefaultTableModel(fontData, fontHeader)
		{
			 public boolean isCellEditable(int i, int c) {
	                return false; // 셀 내용 수정 불가
			 }
		};
		
		JTable fonttable = new JTable(fontList);
		fonttable.getTableHeader().setReorderingAllowed(false); // 이동불가
		fonttable.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		fonttable.getTableHeader().setBackground(Color.white); // 행 배경 색 변경
		fonttable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 12)); // 행 글씨체, 굵기, 크기 변경
		fonttable.setFont(new Font("맑은 고딕", Font.PLAIN, 10)); // 행 글씨체, 굵기, 크기 변경
		fonttable.setBackground(Color.white);
		fonttable.setSelectionBackground(backColor);
		fonttable.setBorder(null);
		fonttable.setGridColor(Color.white);
		fonttable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // JTable에서 중복 셀 선택 막기
		
		JScrollPane scrollPane = new JScrollPane(fonttable);
		scrollPane.setBounds(100, 260, 330, 120);
		c.add(scrollPane);
		
		// 결제 버튼
		JButton paymentBtn = new JButton();
		paymentBtn.setBounds(138, 430, 260, 60); // 위 아래 가로 세로
		paymentBtn.setBorderPainted(false); // 테두리 없애기
		paymentBtn.setContentAreaFilled(false); // 투명 배경
		c.add(paymentBtn);
		
		// 배경 화면 이미지
		backImage.setSize(536, 580);
		c.add(backImage);
		
		setVisible(true); // 화면 출력
		
		// [이벤트]
		// 메인화면 바로가기 버튼 이벤트
		homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainScreen(FontID);
                setVisible(false); // 기존 창 안보이게 하기 
            }
        });
		// 결제 버튼 이벤트
		paymentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					if(fdb.isMembershipPaid(merbershipName, FontID) == 0) {
						int answer = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제 안내", JOptionPane.YES_NO_OPTION );
		        		if(answer==JOptionPane.YES_OPTION){  //사용자가 yes를 눌렀을 경우
		        			fdb.paymentMembership(merbershipName, FontID);
		        			JOptionPane.showMessageDialog(null, "결제 완료되었습니다.");
		        		}
		        		else {
		        			JOptionPane.showMessageDialog(null, "결제가 취소되었습니다.");
		        		}
					}
					else {
						JOptionPane.showMessageDialog(null, "이미 결제된 멤버십입니다.");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
        		
            }
        });
		
	}

	public static void main(String[] args) {
	}

}
