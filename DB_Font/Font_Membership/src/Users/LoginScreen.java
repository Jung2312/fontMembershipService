package Users;

import javax.swing.*;

import DAO.DB_Conn_Query;
import Main.MainScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoginScreen extends JFrame {
	
	final Color backColor = new Color(0xC6DBCF); // 배경 색
	final JLabel backImage = new JLabel(new ImageIcon("./Images/Login_Main.png")); // 로그인 이미지
	final ImageIcon icon = new ImageIcon("./Images/Icon.png"); // 아이콘

	public LoginScreen() {
		DB_Conn_Query fdb = new DB_Conn_Query();
		// [GUI]
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 창닫는 x버튼
		
		setTitle("FONT MEMBERSHIP SERVICE"); // 타이틀
		
		setSize(960, 600); // 전체 창 크기
		setLocationRelativeTo(null); // 화면 중앙 배치
		
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(backColor);
		
		setIconImage(icon.getImage()); // 아이콘
		
		// 로그인 버튼
		JButton loginBtn = new JButton();
		loginBtn.setBounds(368,330,226,44); // 위 아래 가로 세로
		loginBtn.setBorderPainted(false); // 테두리 없애기
		loginBtn.setContentAreaFilled(false); // 투명 배경
		c.add(loginBtn);
	
		// 회원 가입 버튼
		JButton signBtn = new JButton();
		signBtn.setBounds(520,392,70,24);
		signBtn.setBorderPainted(false);
		signBtn.setContentAreaFilled(false);
		c.add(signBtn);
		
		// 아이디 입력창
		JTextField IDField = new JTextField("ID");
		IDField.setBounds(400,220,190,30);
		IDField.setBorder(null);
		IDField.setForeground(Color.gray);
		c.add(IDField);
		
		// 비밀번호 입력창
		JTextField PWField = new JTextField("PASSWORD");
		PWField.setBounds(400,268,190,30);
		PWField.setBorder(null);
		PWField.setForeground(Color.gray);
		c.add(PWField);
		
		// 배경 화면 이미지
		backImage.setSize(960, 540);
		c.add(backImage);
		
		setVisible(true); // 화면 출력
		
		// [이벤트]
		// 로그인 버튼 이벤트(회원 확인 DB 연결 필요)
		loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(fdb.login(IDField.getText(),PWField.getText()))
            		{new MainScreen(IDField.getText());setVisible(false);}
            }
        });
		
		// 회원가입 버튼 이벤트
		signBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignInScreen();
                setVisible(false); // 기존 창 안보이게 하기 
            }
        });
		
	}

	public static void main(String[] args) {
		new LoginScreen();
	}
}
