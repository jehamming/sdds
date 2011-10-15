/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Tool.java
 *
 * Created on 13-feb-2009, 20:19:32
 */
package com.sdds;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sdds.monitor.MessageReciever;
import com.sdds.monitor.ReceivedMessage;
import com.sdds.monitor.consumer.ConsumerWindow;
import com.sdds.monitor.consumer.XMLConsumer;
import com.sdds.monitor.producer.ProducerWindow;

/**
 *
 * @author Jan-Egbert
 */
public class Tool extends javax.swing.JFrame implements MessageReciever, RealmListener{

	private static final long serialVersionUID = -7243445031045416598L;
	private XMLConsumer consumer;
    private Manager manager;
    private javax.swing.JButton btnConsume;
    private javax.swing.JButton btnProduce;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblConsumedMessages;
    private javax.swing.JList recievedMessages;
    private JComboBox cmbRealms;


    /** Creates new form Tool */
    public Tool() {
        setTitle("SDDS Monitor Tool");
        initComponents();
        initialize();
        registerConsumers();
    }

    /** 
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        recievedMessages = new javax.swing.JList();
        lblConsumedMessages = new javax.swing.JLabel();
        btnConsume = new javax.swing.JButton();
        btnProduce = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        cmbRealms = new JComboBox();
        JLabel lblRealms = new JLabel("Available Realms:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        recievedMessages.setModel(new DefaultListModel());
        recievedMessages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recievedMessages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                messageSelected(evt);
            }
        });
        jScrollPane1.setViewportView(recievedMessages);

        lblConsumedMessages.setText("Types of messages consumed");

        btnConsume.setText("Consume");
        btnConsume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consume(evt);
            }
        });

        btnProduce.setText("Produce");
        btnProduce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProduceActionPerformed(evt);
            }
        });
        
        cmbRealms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbRealmsClicked();
			}
		});

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        JPanel mainPanel = new JPanel();
        JPanel pnlRealms = new JPanel();
        JPanel pnlConsumed = new JPanel();
        JPanel pnlButtons = new JPanel();
        
        pnlRealms.add(lblRealms);
        pnlRealms.add(cmbRealms);        
        pnlConsumed.add( jScrollPane1 );
        pnlButtons.add( btnConsume );
        pnlButtons.add( btnProduce );
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        
        mainPanel.add(pnlRealms);
        mainPanel.add(pnlConsumed);
        mainPanel.add(pnlButtons);
        getContentPane().add( mainPanel );
                

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * When the Realm ComboBox is clicked, reinitialize everything
     */
    protected void cmbRealmsClicked() {
    	String chosenRealm = (String) cmbRealms.getSelectedItem();
    	if ( !chosenRealm.equals(manager.getRealm() ) ) {
    		manager.removeRealmListener(this);
        	manager.getConsumerManager().removeConsumer(consumer);
        	manager = Manager.getInstance(chosenRealm);
        	manager.addRealmListener(this);
        	registerConsumers();
        	initializeHmi();
    	}
	}

	/**
	 * When the consume button is pressed
	 * @param evt
	 */
	private void consume(java.awt.event.ActionEvent evt) {
        ReceivedMessage m = (ReceivedMessage) recievedMessages.getSelectedValue();
        ConsumerWindow w = new ConsumerWindow(m.getName(), manager);
        w.setVisible( true );
    }

    /**
     * When a message is clicked
     * @param evt
     */
    private void messageSelected(javax.swing.event.ListSelectionEvent evt) {
        if ( !btnConsume.isEnabled() && !btnProduce.isEnabled() && recievedMessages.getSelectedIndex() != -1 ) {
            btnConsume.setEnabled(true);
            btnProduce.setEnabled(true);
        }
    }

    /**
     * When the Produce button is clicked
     * @param evt
     */
    private void btnProduceActionPerformed(java.awt.event.ActionEvent evt) {
        ReceivedMessage m = (ReceivedMessage) recievedMessages.getSelectedValue();
        ProducerWindow w = new ProducerWindow(m.getName(), m.getXml());
        w.setVisible( true );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Tool().setVisible(true);
            }
        });
    }


    private void initialize() {
        manager = Manager.getInstance();        
        manager.addRealmListener(this);        
        initializeHmi();
    }

    private void initializeHmi() {
        btnConsume.setEnabled(false);
        btnProduce.setEnabled(false);
        String[] realms = Manager.getRealms();
        for ( String realm : realms ) {
        	realmAdded(realm);
        }
        ((DefaultListModel) recievedMessages.getModel()).removeAllElements();
	}

	private void registerConsumers() {
        consumer = new XMLConsumer(this);
        manager.getConsumerManager().addConsumer(consumer);
    }

    public void messageReceived(ReceivedMessage message) {
        DefaultListModel model = (DefaultListModel) recievedMessages.getModel();
        if (!model.contains(message)) {
            model.addElement(message);
        }
    }

	@Override
	public synchronized void realmAdded(String realm) {
		boolean contains = false;
		for (int i = 0; i < cmbRealms.getItemCount(); i++) {
			if ( cmbRealms.getItemAt(i).equals(realm)) {
				contains = true;
				break;
			}			
		}
		if (!contains) cmbRealms.addItem(realm);
	}

}
