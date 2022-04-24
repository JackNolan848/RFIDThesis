import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{	
	String cmd = null;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	//private JPanel exportConceptDataPanel;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;



	private static QueryTableModel TableModel = new QueryTableModel();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	//Buttons for inserting, and updating members
	//also a clear button to clear details panel
	private JTextField StudentNumTF  = new JTextField(12);
	private JButton  StudentNum = new JButton("Search:");
	
	//private JTextField DisplayTF  = new JTextField(12);

	
	


	public JDBCMainWindowContent( String aTitle)
	{	
		//setting up the GUI
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		//add the 'main' panel to the Internal Frame
		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.lightGray);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		//setup details panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(3,2));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Search By Student Number"));
		exportButtonPanel.add(StudentNumTF);
		exportButtonPanel.add(StudentNum);
		
		//exportButtonPanel.add(DisplayTF);
		exportButtonPanel.setSize(450, 200);
		exportButtonPanel.setLocation(3, 100);
		content.add(exportButtonPanel);



		this.StudentNum.addActionListener(this);

		


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.lightGray);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));
		dbContentsPanel.setSize(700, 500);
		dbContentsPanel.setLocation(477, 0);

		
		content.add(dbContentsPanel);

		setSize(982,645);
		setVisible(true);

		TableModel.refreshFromDB(stmt);
	}

	public void initiate_db_conn()
	{
		try
		{
			// Load the JConnector Driver
			//Class.forName("com.mysql.jdbc.Driver");
			// Specify the DB Name
			String url="jdbc:mysql://localhost:3306/Attendance?serverTimezone=GMT";
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "admin");
			//Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}
//	public  void pieGraph(ResultSet rs, String title) {
//		try {
//			DefaultPieDataset dataset = new DefaultPieDataset();
//
//			while (rs.next()) {
//				String category = rs.getString(1);
//				String value = rs.getString(2);
//				dataset.setValue(category+ " "+value, new Double(value));
//			}
//			JFreeChart chart = ChartFactory.createPieChart(
//						title,  
//						dataset,            
//						false,              
//						true,
//						true
//		);
//
//			ChartFrame frame = new ChartFrame(title, chart);
//			chart.setBackgroundPaint(Color.WHITE);
//			frame.pack();
//			frame.setVisible(true);
//		}
//			catch (Exception e) {
//			e.printStackTrace();
//			}
//		}

	//event handling 
	public void actionPerformed(ActionEvent e)
	{
		
		Object target=e.getSource();
		ResultSet rs=null;
		String cmd = null;


		if(target == this.StudentNum){
			String tap= this.StudentNumTF.getText();

			cmd = "select * from details where studentnum = '"  +tap+"';";

			System.out.println(cmd);
			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
				
			}
			catch(Exception e1){e1.printStackTrace();}

		}




			//cmd = "select distinct Campus from details;";

	}


		//event handling
		




		

	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("Jacks.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}

