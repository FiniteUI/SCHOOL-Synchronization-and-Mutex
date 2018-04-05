import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

//this is the parents making the money
class parent extends Thread{
    
    private int speed;
    private bankAccount bA;
    private int salary;
    private Random rand;
    private boolean run = true;
    private int temp;
    private String outputBuffer = "";
    
    //constuctor
    public parent(int speed, bankAccount bA, int salary) {
        this.speed = speed;
        this.bA = bA;
        this.salary = salary;
        rand = new Random();
    }//end constructor
    
    //deposit money until stopped
    public void run() {
        while (run) {
            temp = rand.nextInt(salary) + 1;
            bA.deposit(temp);
            output(Thread.currentThread().getName() + " depositing " + temp);
            try {
                Thread.sleep(speed);
            }//end try
            catch (Exception e) {
                e.printStackTrace();
            }//end catch
        }//end while
    }//end run
    
    //stop the thread
    public void kill() {
        run = false;
    }//end stop
    
    //set salary
    public void setSalary(int x) {
        salary = x;
    }//end setCost
    
    //set speed
    public void setSpeed(int x) {
        speed = x;
    }//end setSpeed
    
    //add to output buffer, for gui
    public void output(String x) {
        outputBuffer += x + "\n";
    }//end output
    
    //return output buffer, for gui
    public String getOutput() {
        String temp = outputBuffer;
        outputBuffer = "";
        return temp;
    }//end getOutput
}//end parent

//------------------------------------------------------------------------------

//this is the children taking the monet
class child extends Thread{
    private int speed;
    private bankAccount bA;
    private Random rand;
    private boolean run = true;
    private int cost;
    private int temp;
    private String outputBuffer = "";
    
    //constructor
    public child(int speed, bankAccount bA, int cost) {
        this.speed = speed;
        this.bA = bA;
        rand = new Random();
        this.cost = cost;
    }//end constructor
    
    //try to withdraw until stopped
    public void run() {
        while(run) {
            temp = rand.nextInt(cost) + 1;
            if (bA.withdrawal(temp)) 
                output(Thread.currentThread().getName() + " withdrawing " + temp);
            else
                output(Thread.currentThread().getName() + " unable to withdraw " + temp + ", not enough funds...");
            try {
                Thread.sleep(speed);
            }//end try
            catch(Exception e) {
                e.printStackTrace();
            }//end catch
        }//end while
    }//end run
    
    //stop the thread
    public void kill() {
        run = false;
    }//end stop
    
    //set cost
    public void setCost(int x) {
        cost = x;
    }//end setCost
    
    //setSpeed
    public void setSpeed(int x) {
        speed = x;
    }//end setSpeed
    
    //add to output buffer, for GUI
    public void output(String x) {
        outputBuffer += x + "\n";
    }//end output
    
    //return output buffer, for GUI
    public String getOutput() {
        String temp = outputBuffer;
        outputBuffer = "";
        return temp;
    }//end getOutput
}//end child

//------------------------------------------------------------------------------

//this is the basic form of the bank account cclass
abstract class bankAccount {
    
    protected int balance = 0;
    
    //put some money in
    abstract void deposit(int x);
    
    //get some money out
    abstract boolean withdrawal(int x);
    
    //return balance
    public int getBalance() {
        return balance;
    }//end getBalance
}//end bankaccount

//------------------------------------------------------------------------------

//this is the semaphore implementation of the bankAccount class
class semaphoreBankAccount extends bankAccount {
    private Semaphore s = new Semaphore(1);
    
    @Override
    public void deposit(int x) {
        try {
            s.acquire();
            balance += x;
            s.release();
        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch
    }//end deposit
    
    @Override
    public boolean withdrawal(int x) {
        boolean temp = true;
        try {
            s.acquire();
            if (x <= balance)
                balance -= x;
            else
                temp = false;
            s.release();
        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch
        return temp;
    }//end withdrawal
}//end semaphoreBankAccount

//-----------------------------------------------------------------------------

//this is the synchronized implementation of the bank account class
class synchronizedBankAccount extends bankAccount {
    
    @Override
    synchronized public void deposit(int x) {
        balance += x;
    }//end deposit
    
    @Override
    synchronized public boolean withdrawal(int x) {
        boolean temp = true;
        if (x <= balance)
            balance -= x;
        else
            temp = false;
        return temp;
    }//end withdrawal
}//end synchronized bank account

//------------------------------------------------------------------------------

//this is the GUI
public class Assignment4 extends javax.swing.JFrame {
    
    private boolean semaphoreOrSynchronized;  //false = synchronized, true = semaphore
    private int momSalary = 50;
    private int momSpeed = 5000;
    private int dadSalary = 50;
    private int dadSpeed = 5000;
    private int childrenSpeed = 5000;
    private int iceCreamCost = 50;
    private bankAccount bA;
    private parent mom;
    private parent dad;
    private ArrayList<child> childArray;
    private ExecutorService guiUpdater;
    
    //runnable to update GUI with info from threads
    Runnable updateGUI = new Runnable(){
        public void run() {
            while(true) {
                AccountBalanceTextField.setText(Integer.toString(bA.getBalance()));
                MotherTextArea.append(mom.getOutput());
                MotherScrollPane.getVerticalScrollBar().setValue(MotherScrollPane.getVerticalScrollBar().getMaximum());
                FatherTextArea.append(dad.getOutput());
                FatherScrollPane.getVerticalScrollBar().setValue(FatherScrollPane.getVerticalScrollBar().getMaximum());
                for (int i = 0; i < childArray.size(); i++)
                    ChildrenTextArea.append(childArray.get(i).getOutput());
                ChildrenScrollPane.getVerticalScrollBar().setValue(ChildrenScrollPane.getVerticalScrollBar().getMaximum());
            }//end while
        }//end run
    };
    
    public Assignment4() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        implementationChoiceBox = new java.awt.Choice();
        ImplementationLabel = new javax.swing.JLabel();
        AddChildButton = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        DadSalarySlider = new javax.swing.JSlider();
        MomSalarySlider = new javax.swing.JSlider();
        MomSalaryLabel = new javax.swing.JLabel();
        DadSalaryLabel = new javax.swing.JLabel();
        MomSpeedLabel = new javax.swing.JLabel();
        DadSpeedLabel = new javax.swing.JLabel();
        MomSpeedSlider = new javax.swing.JSlider();
        DadSpeedSlider = new javax.swing.JSlider();
        StartButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        NumberOfChildrenLabel = new javax.swing.JLabel();
        AccountBalanceLabel = new javax.swing.JLabel();
        NumberOfChildrenTextField = new javax.swing.JTextField();
        AccountBalanceTextField = new javax.swing.JTextField();
        FatherScrollPane = new javax.swing.JScrollPane();
        FatherTextArea = new javax.swing.JTextArea();
        ChildrenScrollPane = new javax.swing.JScrollPane();
        ChildrenTextArea = new javax.swing.JTextArea();
        MotherScrollPane = new javax.swing.JScrollPane();
        MotherTextArea = new javax.swing.JTextArea();
        FatherLabel = new javax.swing.JLabel();
        ChildrenLabel = new javax.swing.JLabel();
        MotherLabel = new javax.swing.JLabel();
        IceCreamCostSlider = new javax.swing.JSlider();
        ChildrenSpeedSlider = new javax.swing.JSlider();
        IceCreamCostLabel = new javax.swing.JLabel();
        ChildrenSpeedLabel = new javax.swing.JLabel();
        StopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        implementationChoiceBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                implementationChoiceBoxItemStateChanged(evt);
            }
        });

        ImplementationLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ImplementationLabel.setText("Implementation Type:");

        AddChildButton.setText("Add Child");
        AddChildButton.setEnabled(false);
        AddChildButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddChildButtonActionPerformed(evt);
            }
        });

        ResetButton.setText("Reset");
        ResetButton.setEnabled(false);
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });

        DadSalarySlider.setMaximum(1000);
        DadSalarySlider.setMinimum(1);
        DadSalarySlider.setValue(500);
        DadSalarySlider.setEnabled(false);
        DadSalarySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DadSalarySliderStateChanged(evt);
            }
        });

        MomSalarySlider.setMaximum(1000);
        MomSalarySlider.setMinimum(1);
        MomSalarySlider.setValue(500);
        MomSalarySlider.setEnabled(false);
        MomSalarySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                MomSalarySliderStateChanged(evt);
            }
        });

        MomSalaryLabel.setText("Mom Salary:");
        MomSalaryLabel.setEnabled(false);

        DadSalaryLabel.setText("Dad Salary: ");
        DadSalaryLabel.setEnabled(false);

        MomSpeedLabel.setText("Mom Speed:");
        MomSpeedLabel.setEnabled(false);

        DadSpeedLabel.setText("Dad Speed:");
        DadSpeedLabel.setEnabled(false);

        MomSpeedSlider.setMaximum(10000);
        MomSpeedSlider.setValue(5000);
        MomSpeedSlider.setEnabled(false);
        MomSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                MomSpeedSliderStateChanged(evt);
            }
        });

        DadSpeedSlider.setMaximum(10000);
        DadSpeedSlider.setValue(5000);
        DadSpeedSlider.setEnabled(false);
        DadSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DadSpeedSliderStateChanged(evt);
            }
        });

        StartButton.setText("Start");
        StartButton.setEnabled(false);
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        NumberOfChildrenLabel.setText("Children: ");
        NumberOfChildrenLabel.setEnabled(false);

        AccountBalanceLabel.setText("Account Balance: ");
        AccountBalanceLabel.setEnabled(false);

        NumberOfChildrenTextField.setEditable(false);
        NumberOfChildrenTextField.setText("0");
        NumberOfChildrenTextField.setEnabled(false);

        AccountBalanceTextField.setEditable(false);
        AccountBalanceTextField.setText("0");
        AccountBalanceTextField.setEnabled(false);

        FatherScrollPane.setEnabled(false);

        FatherTextArea.setColumns(20);
        FatherTextArea.setRows(5);
        FatherTextArea.setEnabled(false);
        FatherScrollPane.setViewportView(FatherTextArea);

        ChildrenScrollPane.setEnabled(false);

        ChildrenTextArea.setColumns(20);
        ChildrenTextArea.setRows(5);
        ChildrenTextArea.setEnabled(false);
        ChildrenScrollPane.setViewportView(ChildrenTextArea);

        MotherScrollPane.setEnabled(false);

        MotherTextArea.setColumns(20);
        MotherTextArea.setRows(5);
        MotherTextArea.setEnabled(false);
        MotherScrollPane.setViewportView(MotherTextArea);

        FatherLabel.setText("Father");
        FatherLabel.setEnabled(false);

        ChildrenLabel.setText("Children");
        ChildrenLabel.setEnabled(false);

        MotherLabel.setText("Mother");
        MotherLabel.setEnabled(false);

        IceCreamCostSlider.setMinimum(1);
        IceCreamCostSlider.setEnabled(false);
        IceCreamCostSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IceCreamCostSliderStateChanged(evt);
            }
        });

        ChildrenSpeedSlider.setMaximum(10000);
        ChildrenSpeedSlider.setValue(5000);
        ChildrenSpeedSlider.setEnabled(false);
        ChildrenSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ChildrenSpeedSliderStateChanged(evt);
            }
        });

        IceCreamCostLabel.setText("Ice Cream Cost: ");
        IceCreamCostLabel.setEnabled(false);

        ChildrenSpeedLabel.setText("Children Speed: ");
        ChildrenSpeedLabel.setEnabled(false);

        StopButton.setText("Stop");
        StopButton.setEnabled(false);
        StopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(ResetButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ImplementationLabel)
                    .addComponent(implementationChoiceBox, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DadSalaryLabel)
                    .addComponent(MomSalaryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MomSalarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DadSalarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MomSpeedLabel)
                    .addComponent(DadSpeedLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DadSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MomSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(StopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addGap(86, 86, 86))
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(539, 539, 539)
                        .addComponent(AddChildButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(447, 447, 447)
                                    .addComponent(NumberOfChildrenLabel))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(153, 153, 153)
                                    .addComponent(FatherLabel)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(69, 69, 69)
                                        .addComponent(ChildrenSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(130, 130, 130)
                                        .addComponent(ChildrenSpeedLabel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(AccountBalanceLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(AccountBalanceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(NumberOfChildrenTextField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(IceCreamCostSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(IceCreamCostLabel)
                                        .addGap(61, 61, 61))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(ChildrenLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(MotherLabel)
                                .addGap(83, 83, 83)))))
                .addGap(80, 80, 80))
            .addGroup(layout.createSequentialGroup()
                .addComponent(FatherScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChildrenScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MotherScrollPane))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(MomSalarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(MomSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DadSalarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DadSalaryLabel)
                                    .addComponent(DadSpeedLabel)
                                    .addComponent(DadSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ResetButton)
                                .addComponent(MomSalaryLabel)
                                .addComponent(MomSpeedLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ImplementationLabel)
                                .addGap(2, 2, 2)
                                .addComponent(implementationChoiceBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(AddChildButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(StartButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StopButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NumberOfChildrenLabel)
                            .addComponent(NumberOfChildrenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChildrenSpeedSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(AccountBalanceLabel)
                                .addComponent(AccountBalanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(IceCreamCostLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IceCreamCostSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ChildrenSpeedLabel)
                        .addGap(22, 22, 22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FatherLabel)
                    .addComponent(ChildrenLabel)
                    .addComponent(MotherLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ChildrenScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FatherScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MotherScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)))
        );

        implementationChoiceBox.add("");
        implementationChoiceBox.add("Semaphore");
        implementationChoiceBox.add("Syncrhonized");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void implementationChoiceBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_implementationChoiceBoxItemStateChanged

        if (!implementationChoiceBox.getSelectedItem().equals("")) {
            implementationChoiceBox.setEnabled(false);
            if (implementationChoiceBox.getSelectedItem().equals("Semaphore"))
                semaphoreOrSynchronized = true;
            else
                semaphoreOrSynchronized = false;
            
            if (semaphoreOrSynchronized)
                bA = new semaphoreBankAccount();
            else
                bA = new synchronizedBankAccount();
            mom = new parent(momSpeed, bA, momSalary);
            dad = new parent(dadSpeed, bA, dadSalary);
            dad.setName("Dad");
            mom.setName("Mom");
            childArray = new ArrayList<>();
            implementationChoiceBox.setEnabled(false);
            setEnabledAll(true);
            AddChildButton.setEnabled(false);
            ResetButton.setEnabled(true);
        }//end if
    }//GEN-LAST:event_implementationChoiceBoxItemStateChanged

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        setEnabledAll(false);
        StopButton.setEnabled(true);
        StopButton.doClick();
        StopButton.setEnabled(false);
        implementationChoiceBox.select("");
        MomSalarySlider.setValue(500);
        DadSalarySlider.setValue(500);
        IceCreamCostSlider.setValue(50);
        MomSpeedSlider.setValue(5000);
        DadSpeedSlider.setValue(5000);
        ChildrenSpeedSlider.setValue(5000);
        AccountBalanceTextField.setText("0");
        NumberOfChildrenTextField.setText("0");
        ChildrenTextArea.setText("");
        FatherTextArea.setText("");
        MotherTextArea.setText("");
        implementationChoiceBox.setEnabled(true);
        ResetButton.setEnabled(false);
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void MomSalarySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_MomSalarySliderStateChanged
        momSalary = MomSalarySlider.getValue();
        mom.setSalary(momSalary);
    }//GEN-LAST:event_MomSalarySliderStateChanged

    private void DadSalarySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_DadSalarySliderStateChanged
        dadSalary = DadSalarySlider.getValue();
        dad.setSalary(dadSalary);
    }//GEN-LAST:event_DadSalarySliderStateChanged

    private void MomSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_MomSpeedSliderStateChanged
        momSpeed = 10000 - MomSpeedSlider.getValue();
        mom.setSpeed(momSpeed);
    }//GEN-LAST:event_MomSpeedSliderStateChanged

    private void DadSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_DadSpeedSliderStateChanged
        dadSpeed = 10000 - DadSpeedSlider.getValue();
        dad.setSpeed(dadSpeed);
    }//GEN-LAST:event_DadSpeedSliderStateChanged

    private void ChildrenSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ChildrenSpeedSliderStateChanged
        childrenSpeed = 10000 - ChildrenSpeedSlider.getValue();
        for(int i = 0; i < childArray.size(); i++)
           childArray.get(i).setSpeed(childrenSpeed);
    }//GEN-LAST:event_ChildrenSpeedSliderStateChanged

    private void IceCreamCostSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IceCreamCostSliderStateChanged
        iceCreamCost = IceCreamCostSlider.getValue();
        for(int i = 0; i < childArray.size(); i++)
           childArray.get(i).setCost(iceCreamCost);
    }//GEN-LAST:event_IceCreamCostSliderStateChanged

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
        StartButton.setEnabled(false);
        mom.start();
        dad.start();
        guiUpdater = Executors.newSingleThreadExecutor();
        guiUpdater.execute(updateGUI);
        AddChildButton.setEnabled(true);
        StopButton.setEnabled(true);
    }//GEN-LAST:event_StartButtonActionPerformed

    private void AddChildButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddChildButtonActionPerformed
        child c = new child(childrenSpeed, bA, iceCreamCost);
        childArray.add(c);
        c.setName("Child " + childArray.size());
        c.output("Child " + childArray.size() + " is born");
        c.start();
        NumberOfChildrenTextField.setText(Integer.toString(childArray.size()));
    }//GEN-LAST:event_AddChildButtonActionPerformed

    private void StopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopButtonActionPerformed
        mom.kill();
        dad.kill();
        for(int i = 0; i < childArray.size(); i++)
           childArray.get(i).kill();
        StopButton.setEnabled(false);
        AddChildButton.setEnabled(false);
        try {
            guiUpdater.shutdown();
        }//end try
        catch (NullPointerException e) {
            //do nothing, this is if they hit reset before ever starting
        }//end catch
    }//GEN-LAST:event_StopButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Assignment4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Assignment4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Assignment4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Assignment4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Assignment4().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccountBalanceLabel;
    private javax.swing.JTextField AccountBalanceTextField;
    private javax.swing.JButton AddChildButton;
    private javax.swing.JLabel ChildrenLabel;
    private javax.swing.JScrollPane ChildrenScrollPane;
    private javax.swing.JLabel ChildrenSpeedLabel;
    private javax.swing.JSlider ChildrenSpeedSlider;
    private javax.swing.JTextArea ChildrenTextArea;
    private javax.swing.JLabel DadSalaryLabel;
    private javax.swing.JSlider DadSalarySlider;
    private javax.swing.JLabel DadSpeedLabel;
    private javax.swing.JSlider DadSpeedSlider;
    private javax.swing.JLabel FatherLabel;
    private javax.swing.JScrollPane FatherScrollPane;
    private javax.swing.JTextArea FatherTextArea;
    private javax.swing.JLabel IceCreamCostLabel;
    private javax.swing.JSlider IceCreamCostSlider;
    private javax.swing.JLabel ImplementationLabel;
    private javax.swing.JLabel MomSalaryLabel;
    private javax.swing.JSlider MomSalarySlider;
    private javax.swing.JLabel MomSpeedLabel;
    private javax.swing.JSlider MomSpeedSlider;
    private javax.swing.JLabel MotherLabel;
    private javax.swing.JScrollPane MotherScrollPane;
    private javax.swing.JTextArea MotherTextArea;
    private javax.swing.JLabel NumberOfChildrenLabel;
    private javax.swing.JTextField NumberOfChildrenTextField;
    private javax.swing.JButton ResetButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton StopButton;
    private java.awt.Choice implementationChoiceBox;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
   
    //enable or disable a bunch of GUI components
    private void setEnabledAll(boolean x) {
        AddChildButton.setEnabled(x);
        ChildrenScrollPane.setEnabled(x);
        AccountBalanceLabel.setEnabled(x);
        AccountBalanceTextField.setEnabled(x);
        ChildrenLabel.setEnabled(x);
        ChildrenTextArea.setEnabled(x);
        DadSalaryLabel.setEnabled(x);
        DadSalarySlider.setEnabled(x);
        DadSpeedLabel.setEnabled(x);
        DadSpeedSlider.setEnabled(x);
        FatherLabel.setEnabled(x);
        FatherScrollPane.setEnabled(x);
        FatherTextArea.setEnabled(x);
        MomSalaryLabel.setEnabled(x);
        MomSalarySlider.setEnabled(x);
        MomSpeedLabel.setEnabled(x);
        MomSpeedSlider.setEnabled(x);
        MotherLabel.setEnabled(x);
        MotherScrollPane.setEnabled(x);
        MotherTextArea.setEnabled(x);
        NumberOfChildrenLabel.setEnabled(x);
        NumberOfChildrenTextField.setEnabled(x);
        StartButton.setEnabled(x);
        ChildrenSpeedLabel.setEnabled(x);
        ChildrenSpeedSlider.setEnabled(x);
        IceCreamCostLabel.setEnabled(x);
        IceCreamCostSlider.setEnabled(x);
    }//end setenabledAll    
}//end GUI

