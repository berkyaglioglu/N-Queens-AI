import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.CardLayout;
import java.awt.Font;

public class ChessBoard {

	private JFrame frame;
	private JPanel[] chessBoardPanels;
	private JLabel[] picLabels;
	private Image img;
	private int divisionNum;
	int[] board;
	private JTextField txt_geneSize;
	private JTextField txt_elitism;
	private JTextField txt_crossover;
	private JTextField txt_mutation;
	private JTextField txt_geneNum;
	private JTextField txt_stateNum;
	private JTextField txt_temp;
	private JTextField txt_cooling;
	private JTextField txt_queen;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessBoard window = new ChessBoard();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChessBoard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	public void addQueens() { // it adds queens to the table
		for(int i=0; i < divisionNum; i++) {
			chessBoardPanels[i+board[i]*divisionNum].add(picLabels[i]); // adds the label of queens to panels
			chessBoardPanels[i+board[i]*divisionNum].repaint();
		}
	}
	
	public void removeQueens() { // it removes queens from table
		for(int i=0; i < divisionNum; i++) {
			chessBoardPanels[i+board[i]*divisionNum].removeAll(); // removes label of queens from panels
			chessBoardPanels[i+board[i]*divisionNum].repaint();
		}
	}
	
	public void generateChessBoard(JPanel panel) { // generate chess board from the beginning with correct number of queens
		int subPanelSide = (panel.getHeight()-4)/divisionNum;
		
		Image queen = img.getScaledInstance(subPanelSide, subPanelSide, 1); // scale image of queen
		picLabels = new JLabel[divisionNum];
		for(int i=0; i < divisionNum; i++) {
			picLabels[i] = new JLabel(new ImageIcon(queen)); // create labels out of queens
		}
		
		chessBoardPanels = new JPanel[(int)Math.pow(divisionNum,2)]; // initialize panels of the board with respect to number of queens 
		int index;
		for(int row=0; row < divisionNum; row++) {
			for(int col=0; col < divisionNum; col++) {
				index = row+col*divisionNum;
				chessBoardPanels[index] = new JPanel(); // create panels of the board and set bounds according to their position
				chessBoardPanels[index].setBounds(row*subPanelSide+2, col*subPanelSide+2, subPanelSide, subPanelSide);
				if(row % 2 != col % 2) { // draw chess board
					chessBoardPanels[index].setBackground(Color.GRAY);
				}
				panel.add(chessBoardPanels[index]);
			}
		}
		panel.repaint();
		panel.revalidate();
		
		board = Solver.getRandomState(divisionNum); // set board with a random state
		
		addQueens(); // add queens according to previously set board
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 680, 617);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JPanel panel_chess = new JPanel();
		panel_chess.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_chess.setBounds(12, 130, 388, 388);
		frame.getContentPane().add(panel_chess);
		panel_chess.setLayout(null);
		
		try {
		    img = ImageIO.read(new File("queen.png")); // read the image file
		}
		catch (IOException e) {
		}
		
		divisionNum = 4; // set the default value
		generateChessBoard(panel_chess); // generate chess board with default values
		
		final JPanel panel_card = new JPanel();
		panel_card.setBounds(412, 193, 238, 329);
		frame.getContentPane().add(panel_card);
		panel_card.setLayout(new CardLayout(0, 0));
		
		final JPanel panel_hill = new JPanel();
		panel_card.add(panel_hill, "name_1400868048782613");
		
		final JPanel panel_simu = new JPanel();
		panel_card.add(panel_simu, "name_1400879471338559");
		panel_simu.setLayout(null);
		
		JLabel lblStartingTemperature = new JLabel("Starting temperature:");
		lblStartingTemperature.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblStartingTemperature.setBounds(12, 13, 197, 26);
		panel_simu.add(lblStartingTemperature);
		
		JLabel lblCoolingFactor = new JLabel("Cooling factor:");
		lblCoolingFactor.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCoolingFactor.setBounds(12, 95, 197, 26);
		panel_simu.add(lblCoolingFactor);
		
		txt_temp = new JTextField();
		txt_temp.setBounds(12, 52, 214, 22);
		panel_simu.add(txt_temp);
		txt_temp.setColumns(10);
		
		txt_cooling = new JTextField();
		txt_cooling.setBounds(12, 136, 214, 22);
		panel_simu.add(txt_cooling);
		txt_cooling.setColumns(10);
		
		final JPanel panel_beam = new JPanel();
		panel_card.add(panel_beam, "name_1400894523429483");
		panel_beam.setLayout(null);
		
		JLabel lblNumberOfStates = new JLabel("Number of states:");
		lblNumberOfStates.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblNumberOfStates.setBounds(12, 31, 197, 26);
		panel_beam.add(lblNumberOfStates);
		
		txt_stateNum = new JTextField();
		txt_stateNum.setBounds(12, 70, 214, 22);
		panel_beam.add(txt_stateNum);
		txt_stateNum.setColumns(10);
		
		final JPanel panel_gene = new JPanel();
		panel_card.add(panel_gene, "name_1400909469625692");
		panel_gene.setLayout(null);
		
		JLabel lblSizeOfA = new JLabel("Size of a single generation:");
		lblSizeOfA.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblSizeOfA.setBounds(12, 13, 214, 26);
		panel_gene.add(lblSizeOfA);
		
		JLabel lblPercentOfElitism = new JLabel("Percent of elitism:");
		lblPercentOfElitism.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblPercentOfElitism.setBounds(12, 70, 197, 26);
		panel_gene.add(lblPercentOfElitism);
		
		JLabel lblCrossoverProbability = new JLabel("Crossover probability:");
		lblCrossoverProbability.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCrossoverProbability.setBounds(12, 133, 197, 26);
		panel_gene.add(lblCrossoverProbability);
		
		JLabel lblMutationProbability = new JLabel("Mutation probability:");
		lblMutationProbability.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblMutationProbability.setBounds(12, 198, 197, 26);
		panel_gene.add(lblMutationProbability);
		
		JLabel lblNumberOfGenerations = new JLabel("Number of generations:");
		lblNumberOfGenerations.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblNumberOfGenerations.setBounds(12, 263, 197, 26);
		panel_gene.add(lblNumberOfGenerations);
		
		txt_geneSize = new JTextField();
		txt_geneSize.setBounds(12, 41, 214, 22);
		panel_gene.add(txt_geneSize);
		txt_geneSize.setColumns(10);
		
		txt_elitism = new JTextField();
		txt_elitism.setBounds(12, 98, 214, 22);
		panel_gene.add(txt_elitism);
		txt_elitism.setColumns(10);
		
		txt_crossover = new JTextField();
		txt_crossover.setBounds(12, 163, 214, 22);
		panel_gene.add(txt_crossover);
		txt_crossover.setColumns(10);
		
		txt_mutation = new JTextField();
		txt_mutation.setBounds(12, 228, 214, 22);
		panel_gene.add(txt_mutation);
		txt_mutation.setColumns(10);
		
		txt_geneNum = new JTextField();
		txt_geneNum.setBounds(12, 294, 214, 22);
		panel_gene.add(txt_geneNum);
		txt_geneNum.setColumns(10);
		
		final JPanel panel_msg = new JPanel();
		panel_msg.setBounds(12, 519, 388, 38);
		frame.getContentPane().add(panel_msg);
		panel_msg.setLayout(null);
		
		final JLabel lbl_msg = new JLabel("");
		lbl_msg.setForeground(Color.RED);
		lbl_msg.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lbl_msg.setBounds(12, 0, 320, 38);
		panel_msg.add(lbl_msg);
		
		JPanel panel_choose = new JPanel();
		panel_choose.setBounds(12, 13, 621, 104);
		frame.getContentPane().add(panel_choose);
		panel_choose.setLayout(null);
		
		JLabel lblChooseNumberOf = new JLabel("Choose number of queens:");
		lblChooseNumberOf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblChooseNumberOf.setBounds(12, 13, 204, 19);
		panel_choose.add(lblChooseNumberOf);
		
		JLabel lblNewLabel = new JLabel("Choose algorithm:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(12, 45, 157, 19);
		panel_choose.add(lblNewLabel);
		
		txt_queen = new JTextField();
		txt_queen.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txt_queen.setText("4");
		txt_queen.setBounds(228, 10, 148, 25);
		panel_choose.add(txt_queen);
		txt_queen.setColumns(10);
		
		final JComboBox comboBox_algo = new JComboBox();
		comboBox_algo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBox_algo.setModel(new DefaultComboBoxModel(new String[] {"Hill climbing", "Simulated annealing", "Local beam search", "Genetic algorithm"}));
		comboBox_algo.setBounds(152, 42, 224, 25);
		panel_choose.add(comboBox_algo);
		
		JButton btn_generate = new JButton("Generate");
		btn_generate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_generate.addActionListener(new ActionListener() { // when generate button is clicked
			public void actionPerformed(ActionEvent arg0) {
				lbl_msg.setText(""); // set text as empty
				if(txt_queen.getText() == String.valueOf(divisionNum)) { // if board is already set with the same number of queens
					removeQueens();
					board = Solver.getRandomState(divisionNum);
					addQueens();
				}
				else {
					panel_chess.removeAll(); // if board is going to be set with different number of queens
					divisionNum = Integer.valueOf(txt_queen.getText());
					generateChessBoard(panel_chess); // generate chess board again
				}
				
				String algo = (String)comboBox_algo.getSelectedItem();
				if(algo == "Hill climbing") { // add panels to card layout according to algorithm
					panel_card.removeAll();
					panel_card.add(panel_hill);
					panel_card.repaint();
					panel_card.revalidate();
				}
				else if(algo == "Simulated annealing") {
					panel_card.removeAll();
					panel_card.add(panel_simu);
					panel_card.repaint();
					panel_card.revalidate();
				}
				else if(algo == "Local beam search") {
					panel_card.removeAll();
					panel_card.add(panel_beam);
					panel_card.repaint();
					panel_card.revalidate();
				}
				else {
					panel_card.removeAll();
					panel_card.add(panel_gene);
					panel_card.repaint();
					panel_card.revalidate();
				}
			}
		});
		btn_generate.setBounds(12, 79, 364, 25);
		panel_choose.add(btn_generate);
		
		JLabel label = new JLabel("N-Queens");
		label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		label.setBounds(432, 10, 177, 37);
		panel_choose.add(label);
		
		JLabel lblProblem = new JLabel("Problem");
		lblProblem.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblProblem.setBounds(432, 47, 177, 37);
		panel_choose.add(lblProblem);
		
		JPanel panel_run = new JPanel();
		panel_run.setBounds(412, 134, 238, 59);
		frame.getContentPane().add(panel_run);
		panel_run.setLayout(null);
		
		JButton btn_run = new JButton("RUN");
		btn_run.addActionListener(new ActionListener() { // if run button is clicked
			public void actionPerformed(ActionEvent e) {
				lbl_msg.setText("");
				String algo = (String)comboBox_algo.getSelectedItem();
				int[] temp;
				
				if(algo == "Hill climbing") { // do action according to which algorithm was chosen
					temp = Algorithms.hillClimbing(divisionNum);
					if(temp == null) { // when there is no solution
						lbl_msg.setText("NO SOLUTION!");
					}
					else {
						removeQueens(); // when there is solution, set board and  add the new board
						board = Arrays.copyOf(temp,divisionNum);
						addQueens();
					}
				}
				else if(algo == "Simulated annealing") { 
					temp = Algorithms.simulatedAnnealing(divisionNum, Double.valueOf(txt_temp.getText()), 
							Double.valueOf(txt_cooling.getText()));
					
					if(temp == null) { // when there is no solution
						lbl_msg.setText("NO SOLUTION!");
					}
					else {
						removeQueens(); // when there is solution, set board and  add the new board
						board = Arrays.copyOf(temp,divisionNum);
						addQueens();
					}
				}
				else if(algo == "Local beam search") {
					temp = Algorithms.localBeamSearch(divisionNum, Integer.valueOf(txt_stateNum.getText()));
					if(temp == null) { // when there is no solution
						lbl_msg.setText("NO SOLUTION!");
					}
					else {
						removeQueens(); // when there is solution, set board and  add the new board
						board = Arrays.copyOf(temp,divisionNum);
						addQueens();
					}
				}
				else {
					temp = Algorithms.geneticAlgorithm(divisionNum, Integer.valueOf(txt_geneSize.getText()),
							Double.valueOf(txt_elitism.getText()), Double.valueOf(txt_crossover.getText()),
							Double.valueOf(txt_mutation.getText()), Integer.valueOf(txt_geneNum.getText()));
					if(temp == null) { // when there is no solution
						lbl_msg.setText("NO SOLUTION!");
					}
					else {
						removeQueens(); // when there is solution, set board and  add the new board
						board = Arrays.copyOf(temp,divisionNum);
						addQueens();
					}
				}
			}
		});
		btn_run.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_run.setBounds(12, 13, 214, 33);
		panel_run.add(btn_run);
		
	}
}

