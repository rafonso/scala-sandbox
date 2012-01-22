package sandbox.fx;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class TesteLayoutSwing extends JFrame {

	private static final long serialVersionUID = 3949212697850594441L;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TesteLayoutSwing frame = new TesteLayoutSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TesteLayoutSwing() {
		setTitle("Test Layout Swing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JButton btnTop = new JButton("Top");
		contentPane.add(btnTop, BorderLayout.NORTH);

		JButton btnCenter = new JButton("Center");
		contentPane.add(btnCenter, BorderLayout.CENTER);

		JButton btnBottom = new JButton("Bottom");
		contentPane.add(btnBottom, BorderLayout.SOUTH);
	}

}
