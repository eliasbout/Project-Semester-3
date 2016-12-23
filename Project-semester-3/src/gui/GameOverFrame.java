/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Arne
 */
public class GameOverFrame extends javax.swing.JFrame {

    /**
     * Creates new form GameOverFrame
     */
    private static long score;
    private static int geom;
    private static String playername;
    
    public GameOverFrame(long score, int geom) {
        initComponents();
        this.score = score;
        this.geom = geom;
        setScore(score);
        setGeom(geom);
    }
    
    public void setScore(long score){
        lblValueScore.setText(Long.toString(score));
    }
    
    public void setGeom(int geom){
        lblValueGeom.setText(Integer.toString(geom));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGameOver = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        lblGeom = new javax.swing.JLabel();
        lblValueScore = new javax.swing.JLabel();
        lblValueGeom = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        btnShop = new javax.swing.JButton();
        lblBackgroundGameOver = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 650));
        setPreferredSize(new java.awt.Dimension(800, 650));
        setSize(new java.awt.Dimension(800, 650));
        getContentPane().setLayout(null);

        lblGameOver.setBackground(new java.awt.Color(255, 255, 255));
        lblGameOver.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblGameOver.setForeground(new java.awt.Color(255, 255, 255));
        lblGameOver.setText("YOU DIED!");
        getContentPane().add(lblGameOver);
        lblGameOver.setBounds(300, 200, 200, 30);

        lblScore.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblScore.setForeground(new java.awt.Color(255, 255, 255));
        lblScore.setText("Score");
        getContentPane().add(lblScore);
        lblScore.setBounds(330, 300, 50, 22);

        lblGeom.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblGeom.setForeground(new java.awt.Color(255, 255, 255));
        lblGeom.setText("Geom");
        getContentPane().add(lblGeom);
        lblGeom.setBounds(330, 330, 46, 22);

        lblValueScore.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblValueScore.setForeground(new java.awt.Color(255, 255, 255));
        lblValueScore.setText("value");
        getContentPane().add(lblValueScore);
        lblValueScore.setBounds(390, 300, 41, 16);

        lblValueGeom.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblValueGeom.setForeground(new java.awt.Color(255, 255, 255));
        lblValueGeom.setText("value");
        getContentPane().add(lblValueGeom);
        lblValueGeom.setBounds(390, 330, 41, 22);

        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buttons/start.png"))); // NOI18N
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        getContentPane().add(btnStart);
        btnStart.setBounds(610, 520, 160, 50);

        btnShop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/buttons/shop.png"))); // NOI18N
        btnShop.setText("btnShop");
        btnShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShopActionPerformed(evt);
            }
        });
        getContentPane().add(btnShop);
        btnShop.setBounds(610, 440, 160, 50);

        lblBackgroundGameOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/backgrounds/game-bg-gameover.png"))); // NOI18N
        getContentPane().add(lblBackgroundGameOver);
        lblBackgroundGameOver.setBounds(0, 0, 800, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShopActionPerformed
        // playername moet nog meegegeven wordenz
        new ShopFrame(playername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnShopActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        new StartGameFrame(playername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnStartActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShop;
    private javax.swing.JButton btnStart;
    private javax.swing.JLabel lblBackgroundGameOver;
    private javax.swing.JLabel lblGameOver;
    private javax.swing.JLabel lblGeom;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblValueGeom;
    private javax.swing.JLabel lblValueScore;
    // End of variables declaration//GEN-END:variables
}
