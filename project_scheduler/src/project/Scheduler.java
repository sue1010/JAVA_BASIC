package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;

public class Scheduler extends JFrame implements ActionListener{
	private JFrame frame = new JFrame("Scheduler");
	private JPanel topPanel, dayPanel, datePanel, memoPanel, cPanel, colorPanel, subPanel, basePanel1, basePanel2, subPanel2, todolistPanel;
	private JPanel[] listPanel;
	private JComboBox<String> monthList;
	private JLabel yearLabel, monthLabel, clickDate, todolist;
	private JButton[] btn, colorBtn, checkBtn;
	private JButton emptyBtn, inBtn, delBtn, plusBtn, minusBtn, qBtn;
	private JTextArea memotxt;
	private JScrollPane memoScroll, checkScroll;
	private JTextField[] checktxt;
	private String btnNum;
	private String[][] memo = new String[12][31];
	private String[][] todo = new String[12][31];
	private String[][] todoColor = new String[12][31];
	private Color c;
	private Color[] colors;
	private Color[][] color = new Color[12][31];
	private Color[][] dateColor = new Color[12][31];
	private int checkNum = 0;
	private int[] last = new int[10];
	
	public Scheduler() {  
	   //초기화 부분
	   for(int i = 0; i < 12; i++) {
	      for(int j = 0; j < 31; j++) {
	         color[i][j] = Color.WHITE;
	         todoColor[i][j] = "N,N,N,N,N,N,N,N,N,N,";
	      }
	   }
	  
	   //UI로 알림창 수정(출처:https://stackoverflow.com/questions/65747901/how-to-change-color-of-ok-button-in-joptionpane?noredirect=1&lq=1)
	   UIManager.put("OptionPane.background", new Color(246, 246, 242));
	   UIManager.put("Panel.background", new Color(246, 246, 242));
	   UIManager.put("OptionPane.okButtonText", "확인");
	   UIManager.put("Button.background", new Color(212, 212, 212));
	   
	   //cPanel
	   cPanel = new JPanel();
	   frame.add(cPanel, BorderLayout.CENTER);
	   cPanel.setBackground(new Color(246, 246, 242));
	
	   //topPanel
	   topPanel = new JPanel();
	   topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	   topPanel.setBackground(new Color(246, 246, 242));
	   frame.add(topPanel, BorderLayout.NORTH);
	   
	   // yearLabel(연도표시)
	   yearLabel = new JLabel("2022년");
	   yearLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
	   yearLabel.setBounds(10, 10, 70, 25);
	   topPanel.add(yearLabel);
	   
	   // monthList, monthLabel(월표시)   
	   String[] month = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10" ,"11", "12"};
	   monthList= new JComboBox<String>(month);
	   monthList.setBackground(new Color(248, 248, 248));
	   monthLabel = new JLabel("월");
	   monthLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
	   topPanel.add(monthList);
	   topPanel.add(monthLabel);
	   monthList.addActionListener(this);
	   
	   // qBtn(? 버튼)
	   qBtn = new RoundedButton("?");
	   qBtn.setBackground(new Color(212, 212, 212));
	   qBtn.setPreferredSize(new Dimension(12, 12));
	   qBtn.addActionListener(this);
	   topPanel.add(qBtn);
	   
	   // 콤보박스 스크롤 수정(출처:https://stackoverflow.com/questions/37396939/jcombobox-customize-vertical-scrollbar)
	   monthList.setUI(new BasicComboBoxUI() {
	       @Override
	       protected ComboPopup createPopup() {
	           return new BasicComboPopup(comboBox) {
	               @Override
	               protected JScrollPane createScroller() {
	                   JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	                   scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
	                	   @Override
	                	   protected void configureScrollBarColors() {
	               	         thumbColor = new Color(212, 212, 212);
	               	       }
	                       @Override
	                       protected JButton createDecreaseButton(int orientation) {
	                           return createZeroButton();
	                       }
	                       @Override
	                       protected JButton createIncreaseButton(int orientation) {
	                           return createZeroButton();
	                       }
	                       @Override
	                       public Dimension getPreferredSize(JComponent c) {
	                           return new Dimension(10, super.getPreferredSize(c).height);
	                       }
	
	                       private JButton createZeroButton() {
	                    	   JButton jbutton = new JButton();
	                	       jbutton.setPreferredSize(new Dimension(0, 0));
	                	       jbutton.setMinimumSize(new Dimension(0, 0));
	                	       jbutton.setMaximumSize(new Dimension(0, 0));
	                	       return jbutton;
	                       }
	                   });
	                   return scroller;
	               }
	           };
	       }
	   });
	   
	   // dayPanel(요일 표시)
	   dayPanel = new JPanel();
	   dayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 5));
	   dayPanel.setBackground(new Color(246, 246, 242));
	  
	   String[] day = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
	   for(int i = 0; i < 7; i++) {
	       JLabel dayLabel = new JLabel("" + day[i]);
	       dayLabel.setForeground(new Color(54, 54, 54));
	       if(i == 0) dayLabel.setForeground(new Color(182, 56, 56));
	       if(i == 6) dayLabel.setForeground(new Color(54, 97, 151));
	       dayPanel.add(dayLabel);
	   }
	   cPanel.add(dayPanel, BorderLayout.NORTH);
	
	   // datePanel(날짜 표시)
	   datePanel = new JPanel();
	   datePanel.setLayout(new GridLayout(0, 7));
	   datePanel.setBackground(new Color(246, 246, 242));
	   showDate(0);
	 
	   // frame(프레임)
	   frame.setResizable(false);   // 프레임 사이즈 고정
	   frame.setVisible(true);
	   frame.setSize(850, 670);   // 프레임의 크기
	   frame.setLocationRelativeTo(null);   // 가운데에서 프레임 출력
	  
	   // basePanel1(memoPanel에 초기 날짜 표시)
	   basePanel1 = new JPanel();
	   basePanel1.setLayout(new BorderLayout());
	   basePanel1.setBackground(new Color(246, 246, 242));
	   clickDate = new JLabel("2022/"+0+"/"+0);
	   basePanel1.add(clickDate, BorderLayout.NORTH);
	  
	    // memoPanel, memotxt(메모장 표시)
	   memoPanel = new JPanel();
	   memoPanel.setLayout(new GridLayout(2,0));
	   memoPanel.setBackground(new Color(246, 246, 242));
	   memoPanel.setBorder(null);
	   
	   memotxt = new JTextArea();
	   memotxt.setBackground(new Color(248, 248, 248));
	   memotxt.setLineWrap(true); //행 넘길 때 행의 마지막 단어가 두행에 걸쳐 나뉘지 않도록 하기
	   memotxt.setWrapStyleWord(true);
	   memoScroll = new JScrollPane(memotxt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	   memoScroll.setPreferredSize(new Dimension(200,100));
	  
	   // 메모장 스크롤 수정(출처:https://stackoverflow.com/questions/7633354/how-to-hide-the-arrow-buttons-in-a-jscrollbar/7661467)
	   memoScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {   
	      @Override
	    protected void configureScrollBarColors() {
	         thumbColor = new Color(212, 212, 212);
	    }
	    @Override
	      protected JButton createDecreaseButton(int orientation) {
	          return createZeroButton();
	      }
	      @Override    
	      protected JButton createIncreaseButton(int orientation) {
	          return createZeroButton();
	      }
	
	      private JButton createZeroButton() {
	          JButton jbutton = new JButton();
	          jbutton.setPreferredSize(new Dimension(0, 0));
	          jbutton.setMinimumSize(new Dimension(0, 0));
	          jbutton.setMaximumSize(new Dimension(0, 0));
	          return jbutton;
	      }
	   });
	  
	   basePanel1.add(memoScroll, BorderLayout.CENTER);
	   frame.add(memoPanel, BorderLayout.EAST);
	  
	   // subPanel(메모 버튼 표시)
	   subPanel = new JPanel();
	   subPanel.setLayout(new BorderLayout());
	   subPanel.setBackground(new Color(246, 246, 242));
	  
	   // inBtn(입력 버튼)
	   inBtn = new RoundedButton("입력");
	   inBtn.setBackground(new Color(212, 212, 212));
	   inBtn.addActionListener(this);
	   subPanel.add(inBtn, BorderLayout.WEST);
	  
	   // delBtn(삭제 버튼)
	   delBtn = new RoundedButton("삭제");
	   delBtn.setBackground(new Color(212, 212, 212));
	   delBtn.addActionListener(this);
	   subPanel.add(delBtn, BorderLayout.EAST);     
	  
	   basePanel1.add(subPanel,BorderLayout.SOUTH);
	  
	   // colorPanel(일정 색 표시)
	   colors = new Color[] {new Color(177, 205, 194), new Color(175, 176, 207), new Color(209, 173, 188)};
	   colorPanel = new JPanel();
	   colorPanel.setBackground(new Color(246, 246, 242));
	   colorBtn = new JButton[3];
	   for(int i = 0; i < 3; i++) {
	      //colorBtn[i] = new JButton();
	      colorBtn[i] = new RoundedButton();
	      colorBtn[i].setPreferredSize(new Dimension(30, 30));
	      colorBtn[i].setBackground(colors[i]);
	      colorBtn[i].addActionListener(this);
	      colorPanel.add(colorBtn[i]);
	   }     
	   subPanel.add(colorPanel, BorderLayout.NORTH);
	  
	   // basePanel2(todolist 표시)
	   basePanel2 = new JPanel();    
	   basePanel2.setLayout(new BorderLayout());
	   basePanel2.setBackground(new Color(246, 246, 242));
	   todolist = new JLabel("To do list");
	   basePanel2.add(todolist, BorderLayout.NORTH);
	  
	   // subPanel(투두리스트 버튼 표시)
	   subPanel2 = new JPanel();
	   subPanel2.setBackground(new Color(246, 246, 242));
	   basePanel2.add(subPanel2, BorderLayout.SOUTH);
	  
	   // plusBtn(+ 버튼)
	   plusBtn = new RoundedButton("+");
	   plusBtn.setBackground(new Color(212, 212, 212));;
	   subPanel2.add(plusBtn, BorderLayout.WEST);
	   plusBtn.addActionListener(this);
	  
	   // minusBtn(- 버튼)
	   minusBtn = new RoundedButton("-");
	   minusBtn.setBackground(new Color(212, 212, 212));
	   subPanel2.add(minusBtn, BorderLayout.EAST);
	   minusBtn.addActionListener(this);
	  
	   // todolistPanel(투두리스트, 스크롤 표시)
	   todolistPanel = new JPanel();
	   todolistPanel.setLayout(new GridLayout(15, 1));
	   todolistPanel.setBackground(new Color(248, 248, 248));
	   checkScroll = new JScrollPane(todolistPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	   checkScroll.setPreferredSize(new Dimension(200, 100));
	  
	   // 투두리스트 스크롤 수정(출처:https://stackoverflow.com/questions/7633354/how-to-hide-the-arrow-buttons-in-a-jscrollbar/7661467)
	   checkScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {   
	       @Override
	     protected void configureScrollBarColors() {
	          thumbColor = new Color(212, 212, 212);
	     }
	     @Override
	       protected JButton createDecreaseButton(int orientation) {
	           return createZeroButton();
	       }
	       @Override    
	       protected JButton createIncreaseButton(int orientation) {
	           return createZeroButton();
	       }
	 
	       private JButton createZeroButton() {
	           JButton jbutton = new JButton();
	           jbutton.setPreferredSize(new Dimension(0, 0));
	           jbutton.setMinimumSize(new Dimension(0, 0));
	           jbutton.setMaximumSize(new Dimension(0, 0));
	           return jbutton;
	       }
	   });
	  
	   basePanel2.add(checkScroll, BorderLayout.CENTER);
	   memoPanel.add(basePanel1);
	   memoPanel.add(basePanel2);
	  
	   // listPanel(투두리스트 리스트표시)
	   listPanel = new JPanel[10];
	   subPanel2.setBackground(new Color(246, 246, 242));
	   checkBtn = new JButton[10];
	   checktxt = new JTextField[10];
	  
	   for(int i = 0; i < 10; i++) {
	      listPanel[i] = new JPanel();
	      listPanel[i].setBackground(new Color(248, 248, 248));
	      // checkBtn(투두리스트 버튼 표시)
	      checkBtn[i] = new RoundedButton();
	      checkBtn[i].setBackground(new Color(227, 227, 227));
	      checkBtn[i].setPreferredSize(new Dimension(12, 12));
	      checkBtn[i].addActionListener(this);
	      listPanel[i].add(checkBtn[i]);
	      //checktxt(투두리스트 텍스트 표시)
	      checktxt[i] = new JTextField();
	      checktxt[i].setBackground(new Color(227, 227, 227));
	      checktxt[i].setPreferredSize(new Dimension(150, 20));
	      checktxt[i].setBorder(null);
	      checktxt[i].addActionListener(this);
	      listPanel[i].add(checktxt[i]);
	     
	      todolistPanel.add(listPanel[i]);
	      listPanel[i].setVisible(false);
	   }
	  
	   pack();
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// showDate(날짜 표시 함수)
	public void showDate(int idx) {
	  Calendar cal = Calendar.getInstance();
	  int month = idx + 1;
	  cal.set(2022, month - 1, 1);
	  int week = cal.get(Calendar.DAY_OF_WEEK);
	  int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	  
	  // emptyBtn(날짜 시작전까지 공백 버튼)
	  for(int j = 1; j < week; j++) {
	       emptyBtn = new JButton(" ");
	       emptyBtn.setPreferredSize(new Dimension(85, 85));
	       emptyBtn = new RoundedButton2();
	       emptyBtn.setHorizontalAlignment(SwingConstants.RIGHT);
	       emptyBtn.setVerticalAlignment(SwingConstants.TOP);
	       emptyBtn.setBackground(Color.WHITE);
	       datePanel.add(emptyBtn);
	   }
	  
	   // btn(날짜 버튼)
	   btn = new JButton[lastDate];
	   for (int i = 0; i < lastDate; i++) {
	       btn[i] = new RoundedButton2("" + (i+1));
	       btn[i].addActionListener(this);
	       btn[i].setPreferredSize(new Dimension(85, 85));
	       btn[i].setHorizontalAlignment(SwingConstants.RIGHT);
	       btn[i].setVerticalAlignment(SwingConstants.TOP);
	       btn[i].setBackground(Color.WHITE);
	       datePanel.add(btn[i]);
	   }
	   cPanel.add(datePanel, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// ? 버튼 눌렀을때
		if(e.getSource() == qBtn) {
			JOptionPane.showMessageDialog(null, "날짜를 선택하고 메모를 작성한 후, 색을 지정하고 입력을 누르세요.\n주의! To do list 사용 시, 내용을 작성한 후 반드시 엔터를 누르세요.", "사용 방법" ,JOptionPane.PLAIN_MESSAGE);
		    return;
		}
		
		// 월 바꿨을때
		else if(e.getSource() == monthList) {
		     datePanel.setVisible(false);
		     datePanel.removeAll();
		     int smonth = monthList.getSelectedIndex(); 
		     showDate(smonth);
		     Calendar cal = Calendar.getInstance();
		     cal.set(2022, smonth, 1); 
		     int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		     // color, dateColor(해당 날짜에 맞는 저장한 날짜 배경색, 글씨색 복구)
		     for(int i = 0; i < lastDate; i++) {
		        btn[i].setBackground(color[smonth][i]);
		        btn[i].setForeground(dateColor[smonth][i]);
		     }
		     datePanel.setVisible(true);
		 }
		 
		 // 입력 버튼 눌렀을때
		 else if(e.getSource() == inBtn) {
		    if(btnNum == null) {
		      JOptionPane.showMessageDialog(null, "날짜를 누르세요.", "메세지" ,JOptionPane.PLAIN_MESSAGE);
		       return;
		    }
		    if(memotxt.getText().isEmpty()) {
		       JOptionPane.showMessageDialog(null, "텍스트를 입력하세요.", "메세지" ,JOptionPane.PLAIN_MESSAGE);
		       return;
		    }
		    if(c == null) {
		       JOptionPane.showMessageDialog(null, "색을 정하세요.", "메세지" ,JOptionPane.PLAIN_MESSAGE); 
		       return;
		    }
		    int row = monthList.getSelectedIndex(); //1월은 0
		    int col = Integer.parseInt(btnNum) - 1; // 1일은 0
		    memo[row][col] = memotxt.getText();
		    btn[col].setBackground(c);
		    color[row][col] = c;
		    c = null;
		 }
		 
		 // 삭제 버튼 눌렀을때
		 else if(e.getSource() == delBtn) {
		    if(btnNum == null) return;
		    int row = monthList.getSelectedIndex();
		     int col = Integer.parseInt(btnNum) - 1;
		     memo[row][col] = null;
		     btn[col].setBackground(Color.WHITE);
		     color[row][col] = Color.WHITE;
		 }
		 
		 // 색 눌렀을때
		 else if(e.getSource() == colorBtn[0] || e.getSource() == colorBtn[1] || e.getSource() == colorBtn[2]) {
		    for(int i = 0; i < 3; i++) {
		       if (e.getSource() == colorBtn[i]) c = colorBtn[i].getBackground();
		    }
		 }
		 
		 // + 버튼 눌렀을때
		 else if(e.getSource() == plusBtn) {
		    if(btnNum == null) {
		       JOptionPane.showMessageDialog(null, "날짜를 누르세요.", "메세지" ,JOptionPane.PLAIN_MESSAGE);
		        return;
		    }
		    if(checkNum >= 10) {
		       JOptionPane.showMessageDialog(null, "리스트 10개 초과", "메세지" ,JOptionPane.PLAIN_MESSAGE);
		        return;
		    }
		    checkBtn[checkNum].setBackground(new Color(227, 227, 227));
		    listPanel[checkNum++].setVisible(true);
		 }
		 
		 // - 버튼 눌렀을때
		 else if(e.getSource() == minusBtn) {
		   if(btnNum == null) return;
		   if(checkNum == 0) return;
		    int row = monthList.getSelectedIndex();
		    int col = Integer.parseInt(btnNum) - 1;
		    listPanel[--checkNum].setVisible(false);
		    checkBtn[checkNum].setBackground(new Color(227, 227, 227));
		    checktxt[checkNum].setText(null);
		    checktxt[checkNum].setEditable(true);
		    if(todo[row][col] == null) return;
		    else if(checkNum == 0) {
		       todo[row][col] = null;
		       btn[col].setForeground(Color.BLACK);
		    }
		    else if(last[checkNum]!= 0) todo[row][col] = todo[row][col].substring(0, last[checkNum]);
		    
		    dateColor[row][col] = btn[col].getForeground();
		    todoColor[row][col] = todoColor[row][col].substring(0, 2*checkNum) + "N" + todoColor[row][col].substring(2*checkNum + 1);
		
		 }
		 
		 // 투두리스트 체크 박스 눌렀을때
		 else if(e.getSource() == checkBtn[0] || e.getSource() == checkBtn[1] || e.getSource() == checkBtn[2] || e.getSource() == checkBtn[3] || e.getSource() == checkBtn[4] ||
		    e.getSource() == checkBtn[5] || e.getSource() == checkBtn[6] || e.getSource() == checkBtn[7] || e.getSource() == checkBtn[8] || e.getSource() == checkBtn[9]) {
		    int row = monthList.getSelectedIndex();
		    int col = Integer.parseInt(btnNum) - 1;
		    for(int i = 0; i < 10; i++) {
		         if (e.getSource() == checkBtn[i]) {
		            if(checkBtn[i].getBackground().equals(new Color(227, 227, 227))) {
		            	checkBtn[i].setBackground(new Color(210, 193, 172));
		                todoColor[row][col] = todoColor[row][col].substring(0, 2*i) + "Y" + todoColor[row][col].substring(2*i + 1);
		            }
		            else{
		               checkBtn[i].setBackground(new Color(227, 227, 227));
		               todoColor[row][col] = todoColor[row][col].substring(0, 2*i) + "N" + todoColor[row][col].substring(2*i + 1);
		            }
		         }
		      }
		 }
		 
		 // 투두리스트 텍스트 필드에서 엔터 눌렀을때
		 else if(e.getSource() == checktxt[0] || e.getSource() == checktxt[1] || e.getSource() == checktxt[2] || e.getSource() == checktxt[3] || e.getSource() == checktxt[4] ||
		       e.getSource() == checktxt[5] || e.getSource() == checktxt[6] || e.getSource() == checktxt[7] || e.getSource() == checktxt[8] || e.getSource() == checktxt[9]) {
		    for(int i = 0; i < 10; i++) {
		         if (e.getSource() == checktxt[i]) {
		            checktxt[i].setEditable(false);
		             int row = monthList.getSelectedIndex();
		             int col = Integer.parseInt(btnNum) - 1;
		             if(todo[row][col] == null) {
		                todo[row][col] = checktxt[i].getText() +',';
		                 btn[col].setForeground(new Color(183, 156, 122));
		                 dateColor[row][col] = btn[col].getForeground();
		             }
		             else todo[row][col] += checktxt[i].getText() +',';
		             last[i] =todo[row][col].length() - checktxt[i].getText().length() - 1;
		         }
		    }
		 }
		 
		 // 날짜 눌렀을때
		 else {
		    JButton bt = (JButton)e.getSource();
		    btnNum = bt.getText();
		    int row = monthList.getSelectedIndex();
		    int col = Integer.parseInt(btnNum) - 1;
		    memotxt.setText(memo[row][col]);
		    
		    // 투두리스트 초기화
		    for(int i = 0; i < 10; i++) {
		       checkBtn[i].setBackground(Color.WHITE);
		       checktxt[i].setText(null);
		       checktxt[i].setEditable(true);
		       listPanel[i].setVisible(false);
		       checkNum = 0;
		    }
		    
		    // 해당 날짜의 투두리스트 복구
		    if(todo[row][col] != null) {
		       String buf[] = todo[row][col].split(",");
		       for(int i = 0 ; i < buf.length; i++) {
		          checktxt[i].setText(buf[i]);
		          checktxt[i].setEditable(false);
		          listPanel[checkNum++].setVisible(true);
		       }
		    }
		    String buf2[] = todoColor[row][col].split(",");
		    for(int i =0 ; i < buf2.length; i++) {
		       if(buf2[i].equals("Y")) {
		          checkBtn[i].setBackground(new Color(210, 193, 172));
		       }
		    }
		    
		    // 메모장 날짜 표시
		    clickDate.setText("2022/"+(row+1)+"/"+bt.getText());
		 }
	}
	
	// RoundedButton(둥근 버튼 표시 함수)(출처:https://the-illusionist.me/42)
	class RoundedButton extends JButton{
	    public RoundedButton() {
	       super();
	       decorate();
	    }
	    public RoundedButton(String string) {
	       super(string);
	       decorate();
	    }
	    protected void decorate() {
	       setBorderPainted(false);
	       setOpaque(false);
	    }
	    protected void paintComponent(Graphics g) {
	       int width = getWidth();
	       int height = getHeight();
	       Graphics2D g2 = (Graphics2D) g;
	       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	       
	       if (getModel().isArmed())g2.setColor(getBackground().darker().darker());
	       else if (getModel().isRollover()) g2.setColor(getBackground().darker());
	       else g2.setColor(getBackground());
	       
	       g2.fillRoundRect(0, 0, width, height, 10, 10);
	       FontMetrics fontMetrics = g2.getFontMetrics();
	       Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), g2).getBounds();
	       int textX = (width - stringBounds.width) / 2;
	       int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
	       g2.setColor(getForeground());
	       g2.setFont(getFont());
	       g2.drawString(getText(), textX, textY);
	       g2.dispose();
	       super.paintComponent(g);
	       }
	}
	
	//RoundedButton2(둥근 버튼 표시 함수2)(출처:https://the-illusionist.me/42)
	class RoundedButton2 extends JButton{
	    public RoundedButton2() {
	       super();
	       decorate();
	    }
	    public RoundedButton2(String string) {
	       super(string);
	       decorate();
	    }
	    protected void decorate() {
	       setBorderPainted(false);
	       setOpaque(false);
	    }
	    protected void paintComponent(Graphics g) {
	       int width = getWidth();
	       int height = getHeight();
	       Graphics2D g2 = (Graphics2D) g;
	       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	       
	       if (getModel().isArmed()) g2.setColor(getBackground().darker().darker());
	       else if (getModel().isRollover()) g2.setColor(getBackground().darker());
	       else g2.setColor(getBackground());
	       
	       g2.fillRect(0, 0, width, height);
	       g2.setColor(new Color(184, 184, 184));
	       g2.drawRect(0, 0, width, height);
	       FontMetrics fontMetrics = g2.getFontMetrics();
	       Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), g2).getBounds();
	       int textX = (width-stringBounds.width) - 10;
	       int textY = (height - stringBounds.height) / fontMetrics.getAscent() + 15;
	       g2.setColor(getForeground());
	       g2.setFont(getFont());
	       g2.drawString(getText(), textX, textY);
	       g2.dispose();
	       super.paintComponent(g);
	       }
	}
	
	public static void main(String[] args) {
	  new Scheduler();      
	}
}