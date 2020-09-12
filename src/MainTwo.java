/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jean M Costillas
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Painter;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import java.util.ArrayList;
import java.util.Random;
public class MainTwo extends javax.swing.JFrame {
Clip gamemain;
Clip buttonclick;
Clip correct;
Clip incorrect;
    /**
     * Creates new form MainTwo
     */
     Timer timer; 
    String input_sequence;
    ArrayList<String> sequence;
    HashMap<String,String> codon_table = new HashMap();
    HashMap<String,String> amino_acids = new HashMap();
    HashMap<String,String> inverse_codon_table = new HashMap();
    HashMap<String,String> inverse_amino_acids = new HashMap();
    int elements;
    int ans = 0;
    int seconds = 20;
    String answer,question,aminoAcid,aminoName,input_codon;
    String player_answer = "";
    String player_string = "";
   
    public MainTwo() {
        initComponents();
        playGameMain();
        this.setTitle("CODED");
        this.setLocationRelativeTo(null);
       // try{
        loadProteins();
        loadCodonTable();  
        loadSequence();
        loadAminoAcids();
        TickTock();
       // }catch(Exception e){
        //    JOptionPane.showMessageDialog(this,"Fatal System error occured at"+e);
       // }
    }
     public void playGameMain() {
        try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
              MainTwo.class.getResource("gamemain-1.wav"));
            gamemain=AudioSystem.getClip();
            gamemain.open(audioIn);
            gamemain.loop(Clip.LOOP_CONTINUOUSLY);
          
                }
        catch(Exception e){
            System.out.print(e);
        }
    
    }
     
     public void TickTock(){
        
        
        
        this.timer = new Timer(1000, new ActionListener() {
        private int counter = 30;
        @Override
        public void actionPerformed(ActionEvent e) {
            counter--;
            time.setText(String.valueOf(counter));
            if(counter==0){
                try{
                    FileWriter fw = new FileWriter("score.txt");
                                BufferedWriter bwriter = new BufferedWriter(fw);
                                bwriter.write(Integer.toString(ans));
                                bwriter.close();
                }catch(Exception err){
                    System.err.print(err);
                }
                MainTwo.this.dispose();
                Victory vic = new Victory();
                vic.setVisible(true);
                gamemain.stop();
            }
        }
        });
        timer.start();
    }
    public String convertAmino(String x){
        String a;
        String b;
        x = x.toUpperCase();
        if(codon_table.containsKey(x)) a = codon_table.get(x);
        else return x;
        if(amino_acids.containsKey(a)) b = amino_acids.get(a);
        else return a;
      
        return b;
    }
    public void RandomAntiCodons(){
        player_answer = "";
        Random dice = new Random();
        int x;
        for(int i = 0; i<3;i++){
             x = dice.nextInt(4);
            if(x==0) player_answer = player_answer + "A";
            else if(x==1) player_answer = player_answer + "U";
            else if(x==2) player_answer = player_answer + "C";
            else player_answer = player_answer + "G";
        }
        for(int i=0;i<3;i++){
                switch(i){
                    case 0:
                        anticodon0.setText(""+player_answer.charAt(i)+"");
                        break;
                    case 1:
                        anticodon1.setText(""+player_answer.charAt(i)+"");
                        break;
                    case 2:
                        anticodon2.setText(""+player_answer.charAt(i)+"");
                        break;
                        
                }
            }
    }
    public void loadSequence(){
            RandomAntiCodons();
        for(int i=0;i<3;i++){
                switch(i){
                    case 0:
                        anticodon0.setText(""+player_answer.charAt(i)+"");
                        break;
                    case 1:
                        anticodon1.setText(""+player_answer.charAt(i)+"");
                        break;
                    case 2:
                        anticodon2.setText(""+player_answer.charAt(i)+"");
                        break;
                        
                }
            }
    }
     public String convertAntiCodons(String x){
        if(x.isEmpty()){
            x = "Error";
        }
        else{
             x=x.toUpperCase();
           char[] str = x.toCharArray();
           for(int i=0;i<3;i++){
               if(str[i]=='A') str[i]='U';
               else if(str[i]=='U') str[i]='A';
               else if(str[i]=='C') str[i]='G';
               else  str[i]='C';
           }
           String b = "";
           for(char c: str){
               b = b + c + ""; 
           }
           x = b;
        }
        return x; 
    }
    public void nameToCodons(){
        
    }
    public void loadCodonTable(){
        try{
        FileReader freader = new FileReader("codon-table.txt");
        BufferedReader breader = new BufferedReader(freader);
        String data;
        while((data=breader.readLine())!=null){
            String[] str = data.split(",");
            codon_table.put(str[0],str[1]);
            inverse_codon_table.put(str[1],str[0]);
            }
        breader.close();
        }catch(FileNotFoundException fe){
            System.out.print("File codon-table.txt not found");
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadAminoAcids(){
        try{
        FileReader freader = new FileReader("amino-acids.txt");
        BufferedReader breader = new BufferedReader(freader);
        String data;
        while((data=breader.readLine())!=null){
            String[] str = data.split(",");
            amino_acids.put(str[0],str[1]);
            inverse_amino_acids.put(str[1], str[0]);
            }
        breader.close();
        }catch(FileNotFoundException fe){
            System.out.print("File amino-acids.txt not found");
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }   
    public void loadProteins(){
        try{
        FileReader freader = new FileReader("proteins.txt");
        BufferedReader breader = new BufferedReader(freader);
        String data;
        while((data=breader.readLine())!=null){
            String[] str = data.split(",");
            question=data;
            aminoName = str[0];
            sequence = new ArrayList<String>(Arrays.asList(str[1].split("-")));
            elements = sequence.size();
            }
        breader.close();
        }catch(FileNotFoundException fe){
            System.out.print("File proteins.txt not found");
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
                                                     
    class MyPainterTwo implements Painter<JProgressBar> {

    private final Color color;

    public MyPainterTwo(Color c1) {
        this.color = c1;
    }
    @Override
    public void paint(Graphics2D gd, JProgressBar t, int width, int height) {
        gd.setColor(color);
        gd.fillRect(0, 0, width, height);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        anticodon0 = new javax.swing.JTextField();
        anticodon1 = new javax.swing.JTextField();
        anticodon2 = new javax.swing.JTextField();
        codon0 = new javax.swing.JTextField();
        codon1 = new javax.swing.JTextField();
        codon2 = new javax.swing.JTextField();
        Cbutton = new javax.swing.JButton();
        Gbutton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        Abutton = new javax.swing.JButton();
        Ubutton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        score = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(538, 492));
        setSize(new java.awt.Dimension(538, 492));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ANTI CODON");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, -1, -1));

        anticodon0.setBackground(new java.awt.Color(41, 41, 41));
        anticodon0.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        anticodon0.setForeground(new java.awt.Color(253, 253, 253));
        anticodon0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anticodon0.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(22, 183, 226)));
        anticodon0.setCaretColor(new java.awt.Color(255, 255, 255));
        anticodon0.setOpaque(false);
        jPanel1.add(anticodon0, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 59, 59));

        anticodon1.setBackground(new java.awt.Color(41, 41, 41));
        anticodon1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        anticodon1.setForeground(new java.awt.Color(255, 255, 255));
        anticodon1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anticodon1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(22, 183, 226)));
        anticodon1.setOpaque(false);
        jPanel1.add(anticodon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 59, 59));

        anticodon2.setBackground(new java.awt.Color(41, 41, 41));
        anticodon2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        anticodon2.setForeground(new java.awt.Color(255, 255, 255));
        anticodon2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anticodon2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(22, 183, 226)));
        anticodon2.setOpaque(false);
        jPanel1.add(anticodon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 59, 59));

        codon0.setBackground(new java.awt.Color(41, 41, 41));
        codon0.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        codon0.setForeground(new java.awt.Color(255, 255, 255));
        codon0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codon0.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        codon0.setOpaque(false);
        jPanel1.add(codon0, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 59, 59));

        codon1.setBackground(new java.awt.Color(41, 41, 41));
        codon1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        codon1.setForeground(new java.awt.Color(255, 255, 255));
        codon1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codon1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        codon1.setOpaque(false);
        jPanel1.add(codon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 59, 59));

        codon2.setBackground(new java.awt.Color(41, 41, 41));
        codon2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        codon2.setForeground(new java.awt.Color(255, 255, 255));
        codon2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codon2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(253, 253, 253)));
        codon2.setOpaque(false);
        jPanel1.add(codon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 59, 59));

        Cbutton.setBackground(new java.awt.Color(255, 255, 255));
        Cbutton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Cbutton.setText("C");
        Cbutton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        Cbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbuttonActionPerformed(evt);
            }
        });
        jPanel1.add(Cbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 360, 61, 40));

        Gbutton.setBackground(new java.awt.Color(255, 255, 255));
        Gbutton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Gbutton.setText("G");
        Gbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GbuttonActionPerformed(evt);
            }
        });
        jPanel1.add(Gbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 61, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CODON");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, -1, -1));

        Abutton.setBackground(new java.awt.Color(255, 255, 255));
        Abutton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Abutton.setText("A");
        Abutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbuttonActionPerformed(evt);
            }
        });
        jPanel1.add(Abutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 61, 40));

        Ubutton.setBackground(new java.awt.Color(255, 255, 255));
        Ubutton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Ubutton.setText("U");
        Ubutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UbuttonActionPerformed(evt);
            }
        });
        jPanel1.add(Ubutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 360, 61, 40));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MATCH!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, 20));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 260, 30));

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("X");
        jButton7.setContentAreaFilled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, -1, -1));

        jButton6.setFont(new java.awt.Font("Yu Gothic Light", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("BACK");
        jButton6.setContentAreaFilled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 80, -1));

        score.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        score.setForeground(new java.awt.Color(255, 255, 255));
        score.setText("0");
        jPanel1.add(score, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 30, 20));

        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setText("you can do it");
        jPanel1.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, -1, -1));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 450, 10));

        jLabel7.setBackground(new java.awt.Color(41, 41, 41));
        jLabel7.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CODED");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 30));

        jLabel1.setFont(new java.awt.Font("Yu Gothic Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SCORE:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, -1, 20));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Time: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        time.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 18)); // NOI18N
        time.setForeground(new java.awt.Color(255, 255, 255));
        time.setText("15");
        jPanel1.add(time, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 20, 20));

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\Jean M Costillas\\Documents\\gwy\\cs 4\\moving-back3.gif")); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void loadAnswer(){
        if(player_string.length()==3){
            player_string = convertAntiCodons(player_string);
            if(player_string.equals(player_answer)){
                ans++;
                score.setText(String.valueOf(ans));
                status.setText("CORRECT!");
                status.setForeground(Color.green);
                loadSequence();
                try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               MainTwo.class.getResource("correct.wav"));
            correct=AudioSystem.getClip();
            correct.open(audioIn);
            correct.start();
            player_string="";
                }
        catch(Exception e){
            System.out.print(e);
        }
             }
            else{
                status.setText("Keep going");
                status.setForeground(Color.magenta);
               
                try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               MainTwo.class.getResource("incorrect.wav"));
            incorrect=AudioSystem.getClip();
            incorrect.open(audioIn);
            incorrect.start();
          
                }
        catch(Exception e){
            System.out.print(e);
        }
            }
            player_string = "";
            codon0.setText("");
            codon1.setText("");
            codon2.setText("");
        }
        for(int i=0;i<player_string.length();i++){
                switch(i){
                    case 0:
                        codon0.setText(""+player_string.charAt(i)+"");
                        break;
                    case 1:
                        codon1.setText(""+player_string.charAt(i)+"");
                        break;
                    case 2:
                        codon2.setText(""+player_string.charAt(i)+"");
                        break;     
                }
            }
        
    }
    private void UbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UbuttonActionPerformed
        // TODO add your handling code here:
       // String str = input.getText();
       player_string = player_string + "U";
       loadAnswer();
               try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               TitleMain.class.getResource("buttonclick.wav"));
            buttonclick=AudioSystem.getClip();
            buttonclick.open(audioIn);
            buttonclick.start();
          
                }
        catch(Exception e){
            System.out.print(e);
        }
    }//GEN-LAST:event_UbuttonActionPerformed

    private void AbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbuttonActionPerformed
        // TODO add your handling code here:
         player_string = player_string + "A";
         loadAnswer();
                 try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               TitleMain.class.getResource("buttonclick.wav"));
            buttonclick=AudioSystem.getClip();
            buttonclick.open(audioIn);
            buttonclick.start();
          
                }
        catch(Exception e){
            System.out.print(e);
        }
    }//GEN-LAST:event_AbuttonActionPerformed

    private void GbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GbuttonActionPerformed
        // TODO add your handling code here:
        //String str = input.getText();
         player_string = player_string + "G";
         loadAnswer();
                 try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               TitleMain.class.getResource("buttonclick.wav"));
            buttonclick=AudioSystem.getClip();
            buttonclick.open(audioIn);
            buttonclick.start();
          
                }
        catch(Exception e){
            System.out.print(e);
        }
    }//GEN-LAST:event_GbuttonActionPerformed

    private void CbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbuttonActionPerformed
        // TODO add your handling code here:
        //String str = input.getText();
         player_string = player_string + "C";
        loadAnswer();
                try{
            AudioInputStream audioIn=AudioSystem.getAudioInputStream(
               TitleMain.class.getResource("buttonclick.wav"));
            buttonclick=AudioSystem.getClip();
            buttonclick.open(audioIn);
            buttonclick.start();
          
                }
        catch(Exception e){
            System.out.print(e);
        }
    }//GEN-LAST:event_CbuttonActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        timer.stop();
        gamemain.stop();
        this.dispose();
        TitleMain titlemain = new TitleMain();
        titlemain.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainTwo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainTwo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainTwo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainTwo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainTwo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Abutton;
    private javax.swing.JButton Cbutton;
    private javax.swing.JButton Gbutton;
    private javax.swing.JButton Ubutton;
    private javax.swing.JTextField anticodon0;
    private javax.swing.JTextField anticodon1;
    private javax.swing.JTextField anticodon2;
    private javax.swing.JTextField codon0;
    private javax.swing.JTextField codon1;
    private javax.swing.JTextField codon2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel score;
    private javax.swing.JLabel status;
    private javax.swing.JLabel time;
    // End of variables declaration//GEN-END:variables
}
