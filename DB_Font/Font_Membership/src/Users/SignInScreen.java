package Users;

import javax.swing.*;

import DAO.DB_Conn_Query;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class SignInScreen extends JFrame {
	
	Color backColor = new Color(0xC6DBCF); // 배경 색
	JLabel backImage = new JLabel(new ImageIcon("./Images/Sign_In.png")); // 가입 이미지
	ImageIcon icon = new ImageIcon("./Images/Icon.png"); // 아이콘

	public SignInScreen() {
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
		
		// 회원 가입 버튼
		JButton signBtn = new JButton();
		signBtn.setBounds(368,400,226,44); // 위 아래 가로 세로
		signBtn.setBorderPainted(false);
		signBtn.setContentAreaFilled(false);
		c.add(signBtn);
		
		// 아이디 입력창
		JTextField IDField = new JTextField("ID");
		IDField.setBounds(380,212,190,30);
		IDField.setBorder(null);
		IDField.setForeground(Color.gray);
		c.add(IDField);
		
		// 비밀번호 입력창
		JTextField PWField = new JTextField("PASSWORD");
		PWField.setBounds(380,255,190,30);
		PWField.setBorder(null);
		PWField.setForeground(Color.gray);
		c.add(PWField);
		
		// 이름 입력창
		JTextField nameField = new JTextField("NAME");
		nameField.setBounds(380,294,190,30);
		nameField.setBorder(null);
		nameField.setForeground(Color.gray);
		c.add(nameField);
		
		// 전화번호 입력창
		JTextField phoneField = new JTextField("PHONE");
		phoneField.setBounds(380,335,190,30);
		phoneField.setBorder(null);
		phoneField.setForeground(Color.gray);
		c.add(phoneField);
		
		// 배경 화면 이미지
		backImage.setSize(960, 540);
		c.add(backImage);

		setVisible(true); // 화면 출력
		
		// [이벤트]
		// 회원가입 버튼 이벤트(DB 연결 필요)
		signBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String phoneNumber = phoneField.getText().trim();
            	if (isValidPhoneNumber(phoneNumber)) {
                    fdb.SignIn(IDField.getText(), PWField.getText(), nameField.getText(), phoneField.getText());
                    new LoginScreen();
                    setVisible(false); // 기존 창 안보이게 하기 
                } else {
                    JOptionPane.showMessageDialog(null, "전화번호는 000-0000-0000 형식으로 입력하세요.");
                }
            	
            }
        });

	}
	
    private static boolean isValidPhoneNumber(String phoneNumber) {
        // 정규 표현식을 사용하여 전화번호 형식 검증
        String regex = "\\d{3}-\\d{4}-\\d{4}";
        return Pattern.matches(regex, phoneNumber);
    }

	public static void main(String[] args) {
	}
}
