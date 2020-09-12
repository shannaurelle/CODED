/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GO_internet
 */
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.Painter;
import javax.swing.text.DefaultEditorKit;


public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    Timer timer; 
    String input_sequence;
    ArrayList<String> sequence;
    HashMap<String,String> codon_table = new HashMap();
    HashMap<String,String> amino_acids = new HashMap();
    int elements;
    int ans;
    int seconds = 20;
    String answer;
    String question;
    public Main() {
        
        initComponents();
        this.setTitle("CODED");
        this.setLocationRelativeTo(null);
        try{
        loadProteins();
        loadCodonTable();  
        //addBindings();
        loadSequence();
        loadAminoAcids();
        TickTock();
        showProgress();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Fatal System error occured at"+e);
        }
      
    }
    public void showProgress(){
         
                
                
    }
    public void TickTock(){
        
        answered.setStringPainted(false); // display numbers or percent
        answered.setValue(0);
        answered.setBorderPainted(false);
        
        this.timer = new Timer(1000, new ActionListener() {
        private int counter = seconds;
        @Override
        public void actionPerformed(ActionEvent e) {
            
            secs.setText(String.valueOf(counter));
   
            UIDefaults defaults = new UIDefaults();
            defaults.put("ProgressBar[Enabled].foregroundPainter", new MyPainter(Color.GREEN));
            defaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter(Color.GREEN));
                
            answered.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
            answered.putClientProperty("Nimbus.Overrides", defaults);
            
            SwingWorker worker = new SwingWorker() {
                    int current = ans, lengthOfTask = elements;

                    @Override
                    public Void doInBackground() {
                        while (current <= lengthOfTask && !isCancelled()) {

                            try { // dummy task
                                Thread.sleep(50);
                            } catch (InterruptedException ie) {
                            }

                            setProgress(100 * current / elements);
                        }
                        return null;
                    }
                };
                worker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {

                        String strPropertyName = pce.getPropertyName();
                        if ("progress".equals(strPropertyName)) {
                            int progress = (Integer) pce.getNewValue();
                            answered.setValue(progress);

                        if(answered.getValue()>=25&&answered.getValue()<50){ // if progress is at 25%
                            UIDefaults defaults2 = new UIDefaults();
                            defaults2.put("ProgressBar[Enabled].foregroundPainter", new MyPainter(Color.yellow));
                            defaults2.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter(Color.yellow));
                    
                            answered.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
                            answered.putClientProperty("Nimbus.Overrides", defaults);
                        }
             
                        if(answered.getValue()>=50&&answered.getValue()<75){ // if progress is at 50%
                            UIDefaults defaults2 = new UIDefaults();
                            defaults2.put("ProgressBar[Enabled].foregroundPainter", new MyPainter(Color.orange));
                            defaults2.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter(Color.orange));
                    
                            answered.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
                            answered.putClientProperty("Nimbus.Overrides", defaults);
                        }
             
                        if(answered.getValue()>=75&&answered.getValue()<100){ // if progress is at 50%
                            UIDefaults defaults2 = new UIDefaults();
                            defaults2.put("ProgressBar[Enabled].foregroundPainter", new MyPainter(Color.RED));
                            defaults2.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter(Color.RED));
                    
                            answered.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
                            answered.putClientProperty("Nimbus.Overrides", defaults);
                        }
                        
                      }
                    }
                });
                worker.execute();
                
            if(answered.getValue()==100){
                        try{
                                FileWriter fw = new FileWriter("score.txt");
                                BufferedWriter bwriter = new BufferedWriter(fw);
                                bwriter.write(Integer.toString(ans));
                                bwriter.close();
                                FileReader fr = new FileReader("proteins.txt");
                                BufferedReader breader = new BufferedReader(fr);
                                String data;
                                sequence.clear();
                                sequence.add(question);
                                while((data=breader.readLine())!=null){
                                    if(!(data.equals(question))) sequence.add(data);
                                }
                                breader.close();
                                FileWriter fw2 = new FileWriter("proteins.txt");
                                BufferedWriter bwriter2 = new BufferedWriter(fw2);
                                for(String x : sequence){
                                     bwriter2.write(x+"\r\n");
                                }
                                bwriter2.close();
                        }catch(Exception exc){
                            JOptionPane.showMessageDialog(Main.this,"Error occurred at: "+exc);
                        }
                        Main.this.dispose();
                        Victory victory = new Victory();
                        victory.setVisible(true);
                        timer.stop();
            }    
            
            if (counter == 0 && answered.getValue()<100) {
                try{
                                FileWriter fw = new FileWriter("score.txt");
                                BufferedWriter bwriter = new BufferedWriter(fw);
                                bwriter.write(Integer.toString(ans));
                                bwriter.close();
                                FileReader fr = new FileReader("proteins.txt");
                                BufferedReader breader = new BufferedReader(fr);
                                String data;
                                sequence.clear();
                                sequence.add(question);
                                while((data=breader.readLine())!=null){
                                    if(!(data.equals(question))) sequence.add(data);
                                }
                                breader.close();
                                FileWriter fw2 = new FileWriter("proteins.txt");
                                BufferedWriter bwriter2 = new BufferedWriter(fw2);
                                for(String x : sequence){
                                     bwriter2.write(x+"\r\n");
                                }
                                bwriter2.close();
                        }catch(Exception exc){
                            JOptionPane.showMessageDialog(Main.this,"Error occurred at: "+exc);
                        }
                Main.this.dispose();
                GameOver g_over = new GameOver();
                g_over.setVisible(true);
                   timer.stop();
            }   
            counter--;
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
    public void loadSequence(){
        if(ans<elements){
            aminoAcid.setText(sequence.get(ans));
            num.setText(String.valueOf(elements-ans));
        }
    }
    public void addBindings(){
        
    }
    public String convertAntiCodons(String x){
        x=x.toUpperCase();
        if(x.length()==3){
           char[] str = x.toCharArray();
           for(char c : str){
               switch (c) {
                   case 'A':
                       c = 'U';
                       break;
                   case 'U':
                       c = 'A';
                       break;
                   case 'C':
                       c = 'G';
                       break;
                   default:
                       c = 'C';
                       break;
               }
           }
           x = Arrays.toString(str);
        }
        return x; 
    }
    public void loadCodonTable(){
        try{
        FileReader freader = new FileReader("codon-table.txt");
        BufferedReader breader = new BufferedReader(freader);
        String data;
        while((data=breader.readLine())!=null){
            String[] str = data.split(",");
            codon_table.put(str[0],str[1]);
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
            proteinName.setText(str[0]);
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        secs = new javax.swing.JLabel();
        input = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        proteinName = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        aminoAcid = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        num = new javax.swing.JLabel();
        enterButton = new javax.swing.JButton();
        status = new javax.swing.JLabel();
        Ubutton = new javax.swing.JButton();
        Abutton = new javax.swing.JButton();
        Cbutton = new javax.swing.JButton();
        Gbutton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        answered = new javax.swing.JProgressBar();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Time left:");

        secs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        secs.setText("0");

        input.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Codons: ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Protein Structure: ");

        proteinName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        proteinName.setText("protein");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Amino Acid: ");

        aminoAcid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        aminoAcid.setText("amino_acid");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Amino acids left:");

        num.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        num.setText("num");

        enterButton.setText("Enter");
        enterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterButtonActionPerformed(evt);
            }
        });

        status.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        status.setText("You can do it :)");

        Ubutton.setText("U");
        Ubutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UbuttonActionPerformed(evt);
            }
        });

        Abutton.setText("A");
        Abutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbuttonActionPerformed(evt);
            }
        });

        Cbutton.setText("G");
        Cbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbuttonActionPerformed(evt);
            }
        });

        Gbutton.setText("C");
        Gbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GbuttonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Hint:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        jLabel8.setText("This is where the hint is located");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Protein:");

        jLabel10.setText("Please insert a 200 by 200 picture here.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Ubutton)
                        .addGap(14, 14, 14)
                        .addComponent(Abutton)
                        .addGap(15, 15, 15)
                        .addComponent(Cbutton)
                        .addGap(18, 18, 18)
                        .addComponent(Gbutton))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(enterButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(proteinName))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel6))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(num)
                                    .addComponent(secs)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(57, 57, 57)
                                .addComponent(aminoAcid))
                            .addComponent(jLabel4))))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(status)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(secs)
                            .addComponent(status))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(num)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(proteinName))
                    .addComponent(jLabel8))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(aminoAcid, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ubutton)
                    .addComponent(Abutton)
                    .addComponent(Cbutton)
                    .addComponent(Gbutton))
                .addGap(18, 18, 18)
                .addComponent(enterButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton5.setText("Exit");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Back to Main Menu");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Pause");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Progress: ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("CODED");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(answered, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jButton6))
                            .addComponent(jLabel1)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(answered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        TitleMain t_main = new TitleMain();
        t_main.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputActionPerformed

    private void enterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterButtonActionPerformed
        // TODO add your handling code here:
        String x = input.getText();
        //answer = convertAntiCodons(x);
        answer = convertAmino(x);
        if(answer.equals(aminoAcid.getText())){
            ans++;
            status.setForeground(Color.GREEN);
            status.setText("Correct! ");
            
        }
        else{          
            status.setForeground(Color.ORANGE);
            status.setText("Go on. You're making progress.");
        }
        input.setText("");
        loadSequence();
    }//GEN-LAST:event_enterButtonActionPerformed

    private void UbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UbuttonActionPerformed
        // TODO add your handling code here:
        String str = input.getText();
        input.setText(str+"U");
    }//GEN-LAST:event_UbuttonActionPerformed

    private void AbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbuttonActionPerformed
        // TODO add your handling code here:
        String str = input.getText();
        input.setText(str+"A");
    }//GEN-LAST:event_AbuttonActionPerformed

    private void CbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbuttonActionPerformed
        // TODO add your handling code here:
        String str = input.getText();
        input.setText(str+"G");
    }//GEN-LAST:event_CbuttonActionPerformed

    private void GbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GbuttonActionPerformed
        // TODO add your handling code here:
        String str = input.getText();
        input.setText(str+"C");
    }//GEN-LAST:event_GbuttonActionPerformed
    class MyPainter implements Painter<JProgressBar> {

    private final Color color;

    public MyPainter(Color c1) {
        this.color = c1;
    }
    @Override
    public void paint(Graphics2D gd, JProgressBar t, int width, int height) {
        gd.setColor(color);
        gd.fillRect(0, 0, width, height);
        }
    }
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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Abutton;
    private javax.swing.JButton Cbutton;
    private javax.swing.JButton Gbutton;
    private javax.swing.JButton Ubutton;
    private javax.swing.JLabel aminoAcid;
    private javax.swing.JProgressBar answered;
    private javax.swing.JButton enterButton;
    private javax.swing.JTextField input;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel num;
    private javax.swing.JLabel proteinName;
    private javax.swing.JLabel secs;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables
}

