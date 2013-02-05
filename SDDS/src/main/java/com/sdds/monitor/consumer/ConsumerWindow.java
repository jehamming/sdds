/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConsumerWindow.java
 *
 * Created on 14-feb-2009, 10:23:26
 */
package com.sdds.monitor.consumer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;

import com.sdds.SDDSManager;

/**
 *
 * @author Jan-Egbert
 */
public class ConsumerWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 4675786827419745416L;
	private ConsumerWindowHelper helper;
    private String type;
    private SDDSManager manager;

    /** Creates new form ConsumerWindow */
    public ConsumerWindow(String type, SDDSManager manager) {
        this.type = type;
        this.manager = manager;
        setTitle("C:" + type);
        initComponents();
        initHelper();
    }

    void addConsumedItem(ConsumedItem item) {
        ((DefaultListModel) consumedMessagesList.getModel()).addElement(item);
        if (chkContinous.isSelected()) {
            taConsumedMessageData.setText(item.getXml());
            lblDelay.setText( ""+ item.getDelay() );
        }
        DefaultListModel pModel = (DefaultListModel) producersList.getModel();
        if (!pModel.contains(item.getManagerSourceId())) {
            pModel.addElement(item.getManagerSourceId());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        producersList = new javax.swing.JList();
        lblProducers = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taConsumedMessageData = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        consumedMessagesList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        chkContinous = new javax.swing.JCheckBox();
        lblDelay = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        producersList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(producersList);

        lblProducers.setText("SDDS sources (managers)");

        taConsumedMessageData.setColumns(20);
        taConsumedMessageData.setRows(5);
        jScrollPane2.setViewportView(taConsumedMessageData);

        jLabel1.setText("Consumed Message Data");

        consumedMessagesList.setModel(new DefaultListModel());
        consumedMessagesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                messageClicked(evt);
            }
        });
        jScrollPane3.setViewportView(consumedMessagesList);

        jLabel2.setText("Consumed messages of selection");

        chkContinous.setText("Continous");

        lblDelay.setText("0");
        lblDelay.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel3.setText("Delay:");

        jLabel4.setText("ms");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane3)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, true)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addGap(36, 36, 36)
                                .addComponent(chkContinous))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 424, javax.swing.GroupLayout.DEFAULT_SIZE)))
                    .addComponent(lblProducers)))
//                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblProducers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(chkContinous)
                        .addComponent(lblDelay)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void messageClicked(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_messageClicked
        chkContinous.setSelected(false);
        ConsumedItem item = (ConsumedItem) consumedMessagesList.getSelectedValue();
        taConsumedMessageData.setText(item.getXml());
        lblDelay.setText( ""+ item.getDelay() );
    }//GEN-LAST:event_messageClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkContinous;
    private javax.swing.JList consumedMessagesList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDelay;
    private javax.swing.JLabel lblProducers;
    private javax.swing.JList producersList;
    private javax.swing.JTextArea taConsumedMessageData;
    // End of variables declaration//GEN-END:variables

    private void initHelper() {
        helper = new ConsumerWindowHelper(this, type);
        
        
        addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
				helper.stop();
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
    }
    
    public SDDSManager getManager() {
		return manager;
	}
    
}
