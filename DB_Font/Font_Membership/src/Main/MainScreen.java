package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.DB_Conn_Query;
import Download.DLRankScreen;
import Membership.MembershipScreen;
import Users.LoginScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {
	
	final Color backColor = new Color(0xC6DBCF); // 배경 색
	final JLabel backImage = new JLabel(new ImageIcon("./Images/Main.png")); // 메인 이미지
	final ImageIcon icon = new ImageIcon("./Images/Icon.png"); // 아이콘
	
	public MainScreen(String FontID) {		
		DB_Conn_Query fdb = new DB_Conn_Query();//db참조***************************************
		// [GUI]
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 창닫는 x버튼
		
		setTitle("FONT MEMBERSHIP SERVICE"); // 타이틀
		
		setSize(960, 600); // 전체 창 크기
		setLocationRelativeTo(null); // 화면 중앙 배치
		
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(backColor);
		
		setIconImage(icon.getImage()); // 아이콘
		
		// 다운로드 순위 버튼
		JButton DLRankBtn = new JButton();
		DLRankBtn.setBounds(70,32,106,30); // 위 아래 가로 세로
		DLRankBtn.setBorderPainted(false); // 테두리 없애기
		DLRankBtn.setContentAreaFilled(false); // 투명 배경
		c.add(DLRankBtn);
		
		// 멤버쉽 검색창
		JTextField searchField = new JTextField();
		searchField.setBounds(116,120,338,26);
		searchField.setBorder(null);
		searchField.setForeground(Color.gray);
		c.add(searchField);
		
		// 멤버쉽 검색 버튼
		JButton searchBtn = new JButton();
		searchBtn.setBounds(470,120,74,26);
		searchBtn.setBorderPainted(false); // 테두리 없애기
		searchBtn.setContentAreaFilled(false); // 투명 배경
		c.add(searchBtn);
		
		// 테이블
		List<String[]> data = fdb.membershipAllList();
		String header[] = { "멤버쉽명", "제작사명", "기간", "금액" };
		
	    DefaultTableModel memberShip = new DefaultTableModel(data.toArray(new String[0][0]), header) {
	        public boolean isCellEditable(int i, int c) {
	            return false; // 셀 내용 수정 불가
	        }
	    };
		
	    JTable table = new JTable(memberShip);
		table.getTableHeader().setReorderingAllowed(false); // 이동불가
		table.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		table.getTableHeader().setBackground(Color.white); // 행 배경 색 변경
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 행 글씨체, 굵기, 크기 변경
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 행 글씨체, 굵기, 크기 변경
		table.setBackground(Color.white);
		table.setSelectionBackground(backColor);
		table.setBorder(null);
		table.setGridColor(Color.white);
		table.getColumn("멤버쉽명").setPreferredWidth(110);
		table.getColumn("제작사명").setPreferredWidth(70);
		table.getColumn("기간").setPreferredWidth(100);
		table.getColumn("금액").setPreferredWidth(4);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // JTable에서 중복 셀 선택 막기
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(100, 180, 460, 260);
		c.add(scrollPane);
		
		// 가입 멤버쉽 정보
		JLabel infoTxt = new JLabel("가입 멤버쉽");
		infoTxt.setBounds(650, 70, 436, 100);
		infoTxt.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		c.add(infoTxt);
		
		
		// 가입 멤버쉽 리스트 테이블
		String joinData[][] = fdb.ft_2_callable(FontID); // aaa대신 로그인한 회원아이디 넣어야함*************************
		String joinHeader[] = { "가입 멤버쉽"};
		
		DefaultTableModel joinList = new DefaultTableModel(joinData, joinHeader)
		{
			 public boolean isCellEditable(int i, int c) {
	                return false; // 셀 내용 수정 불가
			 }
		};
		
		JTable jointable = new JTable(joinList); 

		jointable.getTableHeader().setReorderingAllowed(false); // 이동불가
		jointable.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		jointable.getTableHeader().setBackground(Color.white); // 행 배경 색 변경
		jointable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 12)); // 행 글씨체, 굵기, 크기 변경
		jointable.setFont(new Font("맑은 고딕", Font.PLAIN, 10)); // 행 글씨체, 굵기, 크기 변경
		jointable.setBackground(Color.white);
		jointable.setSelectionBackground(backColor);
		jointable.setBorder(null);
		jointable.setGridColor(Color.white);
		jointable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // JTable에서 중복 셀 선택 막기
		
		JScrollPane scrollPane2 = new JScrollPane(jointable);
		scrollPane2.setBounds(650, 150, 216, 60);
		c.add(scrollPane2);
		
		// 가입 멤버쉽 상세 정보
		JLabel joinName = new JLabel("멤버쉽 : ");
		joinName.setBounds(650, 210, 216, 60);
		joinName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		c.add(joinName);
		
		JLabel joinPeriod = new JLabel("기간 : ");
		joinPeriod.setBounds(650, 230, 216, 60);
		joinPeriod.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		c.add(joinPeriod);
		
		JLabel joinProducer = new JLabel("제작사 : ");
		joinProducer.setBounds(650, 250, 216, 60);
		joinProducer.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		c.add(joinProducer);	
		
		jointable.addMouseListener(new MouseAdapter() {//가입멤버십 리스트 클릭시*******
			@Override
			public void mouseClicked(MouseEvent e) {
		        int selectedRow = jointable.getSelectedRow();//클릭한 리스트 정보 표시
		        if (selectedRow >= 0 && selectedRow < joinData.length) {
		            joinName.setText("멤버쉽 : " + joinData[selectedRow][0]);
		            joinPeriod.setText("기간 : " + joinData[selectedRow][1]);
		            joinProducer.setText("제작사 : " + joinData[selectedRow][2]);
		        }
			}
		});
		
		// 멤버쉽 안내 버튼
		JButton MembershipBtn = new JButton();
		MembershipBtn.setBounds(630, 340, 250, 60);
		MembershipBtn.setBorderPainted(false); // 테두리 없애기
		MembershipBtn.setContentAreaFilled(false); // 투명 배경
		c.add(MembershipBtn);
		
		
		// 배경 화면 이미지
		backImage.setSize(960, 540);
		c.add(backImage);
		
		setVisible(true); // 화면 출력
		
		// [이벤트]
		// 다운로드 순위 버튼 이벤트
		DLRankBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DLRankScreen(FontID);
                setVisible(false); // 기존 창 안보이게 하기 
            }
        });
		
		// 검색 버튼 이벤트(DB 연결 필요)
		searchBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {

		        List<String[]> searchResults = fdb.searchMembershipsByName(searchField.getText());

		        DefaultTableModel model = (DefaultTableModel) table.getModel();
		        model.setRowCount(0);

		        for (String[] data : searchResults) {
		            model.addRow(data);
		        }
		    }
		});
		
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedRow = table.getSelectedRow();
		        int selectedColumn = table.getSelectedColumn();

		        if (selectedRow != -1 && selectedColumn == table.getColumn("멤버쉽명").getModelIndex()) {
		            String selectedMembershipName = (String) table.getValueAt(selectedRow, selectedColumn);

		            MembershipBtn.setEnabled(true);
		            MembershipBtn.addActionListener(new ActionListener() {
		                @Override
		                public void actionPerformed(ActionEvent e) {
		                    new MembershipScreen(selectedMembershipName,FontID); // 멤버쉽 명 전송
		                    System.out.println(selectedMembershipName);
		                    setVisible(false);
		                }
		            });
		        } else {
		            MembershipBtn.setEnabled(false);
		        }
		    }
		});
	
	}

	public static void main(String[] args) {
	}

}
