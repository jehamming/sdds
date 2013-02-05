package com.uri;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sdds.Consumer;
import com.sdds.SDDSManager;

@SuppressWarnings("serial")
public class ChatWindow extends JFrame {

	private ChatConsumer consumer;
	private JTextArea ta;
	private JTextField txtField;

	public class ChatConsumer extends Consumer<ChatObject> {
		ChatWindow window;

		public ChatConsumer(ChatWindow window) {
			this.window = window;
		}

		@Override
		public void consume(ChatObject object) {
			window.addText(object.getMessage());
		}
	}

	public class ChatObject {

		private String message;

		public ChatObject(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

	}

	public ChatWindow() {
		init();
		pack();
	}

	private void init() {
		JPanel pnl = new JPanel();
		ta = new JTextArea();
		ta.setRows(20);
		ta.setColumns(40);
		ta.setText("");
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(ta);
		pnl.add(scrollPane);

		JPanel pnl2 = new JPanel();
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String s = txtField.getText();
						sendText(s);
					}
				});

			}
		});
		pnl2.add(btnSend);
		txtField = new JTextField(20);
		txtField.setText("Text to send");
		pnl2.add(txtField);

		JPanel pnl3 = new JPanel();
		pnl3.setLayout(new BoxLayout(pnl3, BoxLayout.Y_AXIS));
		pnl3.add(pnl);
		pnl3.add(pnl2);
		getContentPane().add(pnl3);

	}

	public void setConsumer(ChatConsumer consumer) {
		this.consumer = consumer;
	}

	protected void sendText(String s) {
		ChatObject co = new ChatObject(s);
		SDDSManager.getInstance().produce(co);
	}

	public void addText(String message) {
		String s = ta.getText();
		final String newString = s.concat(message + "\n");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ta.setText(newString);
			}
		});
	}

	private void initializeConsumer() {
		consumer = new ChatConsumer(this);
		SDDSManager.getInstance().getConsumerManager().addConsumer(consumer);
	}

	public static void main(String[] args) {
		ChatWindow w = new ChatWindow();
		w.initializeConsumer();
		w.setVisible(true);

		// ChatWindow w2 = new ChatWindow();
		// w2.setVisible(true);
	}

}
