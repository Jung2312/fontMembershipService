package Download;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.DB_Conn_Query;
import Main.MainScreen;

public class DLRankScreen extends JFrame{
	
	final Color backColor = new Color(0xC6DBCF); // 배경 색
	final JLabel backImage = new JLabel(new ImageIcon("./Images/DLRank.png")); // 다운로드 순위 이미지
	final ImageIcon icon = new ImageIcon("./Images/Icon.png"); // 아이콘
	
	public DLRankScreen(String FontID){
		DB_Conn_Query fdb = new DB_Conn_Query();//db참조
		// [GUI]
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 창닫는 x버튼
		
		setTitle("FONT MEMBERSHIP SERVICE"); // 타이틀
		
		setSize(960, 600); // 전체 창 크기
		setLocationRelativeTo(null); // 화면 중앙 배치
		
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(backColor);
		
		setIconImage(icon.getImage()); // 아이콘	
		
		// 메인 화면 바로 가기 버튼
		JButton homeBtn = new JButton(new ImageIcon("./Images/HomeBtn.png"));
		homeBtn.setBounds(900,20,30,30); // 위 아래 가로 세로
		homeBtn.setBorderPainted(false); // 테두리 없애기
		homeBtn.setContentAreaFilled(false); // 투명 배경
		c.add(homeBtn);
		
		// 테이블
		Object data[][] = fdb.ft_3_callable(); // db에서 데이터 가져옴
		String header[] = { "순위", "폰트명", "다운로드 수", "평균평점" };
		
		DefaultTableModel merberShip = new DefaultTableModel(data, header)
				
		{
			 public boolean isCellEditable(int i, int c) {
	                return false; // 셀 내용 수정 불가
			 }
		};
		
		JTable table = new JTable(merberShip);
		table.getTableHeader().setReorderingAllowed(false); // 이동불가
		table.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		table.getTableHeader().setBackground(Color.white); // 행 배경 색 변경
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 행 글씨체, 굵기, 크기 변경
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 행 글씨체, 굵기, 크기 변경
		table.setBackground(Color.white);
		table.setSelectionBackground(backColor);
		table.setBorder(null);
		table.setGridColor(Color.white);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(200, 180, 560, 280);
		c.add(scrollPane);
		
		// 배경 화면 이미지
		backImage.setSize(960, 540);
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
	}

	public static void main(String[] args) {
	}

}
