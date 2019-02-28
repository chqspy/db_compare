import db.compare.TableCompare;
import db.domain.DataBaseDO;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        JFrame jf = new JFrame("数据库对比工具");
        FlowLayout fl = new FlowLayout();
        jf.setLayout(fl);
        JPanel panel1 = new JPanel();
        JLabel ipLabel1 = new JLabel("IP:");
        ipLabel1.setBounds(0, 0, 80, 25);
        panel1.add(ipLabel1);
        JTextField ipText1 = new JTextField(15);
        ipText1.setText("127.0.0.1");
        ipText1.setBounds(100, 20, 150, 25);
        panel1.add(ipText1);
        JLabel portLabel1 = new JLabel("端口号:");
        portLabel1.setBounds(0, 0, 80, 25);
        panel1.add(portLabel1);
        JTextField portText1 = new JTextField(15);
        portText1.setText("3306");
        portText1.setBounds(100, 20, 150, 25);
        panel1.add(portText1);
        JLabel nameLabel1 = new JLabel("数据库名称:");
        nameLabel1.setBounds(0, 0, 80, 25);
        panel1.add(nameLabel1);
        JTextField nameText1 = new JTextField(15);
        nameText1.setText("czyth");
        nameText1.setBounds(100, 20, 150, 25);
        panel1.add(nameText1);
        JLabel userLabel1 = new JLabel("用户名:");
        userLabel1.setBounds(0, 0, 80, 25);
        panel1.add(userLabel1);
        JTextField userText1 = new JTextField(15);
        userText1.setText("root");
        userText1.setBounds(100, 20, 150, 25);
        panel1.add(userText1);
        JLabel pwLabel1 = new JLabel("密码:");
        pwLabel1.setBounds(0, 0, 80, 25);
        panel1.add(pwLabel1);
        JPasswordField pwText1 = new JPasswordField(15);
        pwText1.setText("123456");
        pwText1.setBounds(100, 20, 150, 25);
        panel1.add(pwText1);
        jf.add(panel1);

        JPanel panel2 = new JPanel();
        JLabel ipLabel2 = new JLabel("IP:");
        ipLabel2.setBounds(0, 0, 80, 25);
        panel2.add(ipLabel2);
        JTextField ipText2 = new JTextField(15);
        ipText2.setText("127.0.0.1");
        ipText2.setBounds(100, 20, 150, 25);
        panel2.add(ipText2);
        JLabel portLabel2 = new JLabel("端口号:");
        portLabel2.setBounds(0, 0, 80, 25);
        panel2.add(portLabel2);
        JTextField portText2 = new JTextField(15);
        portText2.setText("3306");
        portText2.setBounds(100, 20, 150, 25);
        panel2.add(portText2);
        JLabel nameLabel2 = new JLabel("数据库名称:");
        nameLabel2.setBounds(0, 0, 80, 25);
        panel2.add(nameLabel2);
        JTextField nameText2 = new JTextField(15);
        nameText2.setText("czyth_zs");
        nameText2.setBounds(100, 20, 150, 25);
        panel2.add(nameText2);
        JLabel userLabel2 = new JLabel("用户名:");
        userLabel2.setBounds(0, 0, 80, 25);
        panel2.add(userLabel2);
        JTextField userText2 = new JTextField(15);
        userText2.setText("root");
        userText2.setBounds(100, 20, 150, 25);
        panel2.add(userText2);
        JLabel pwLabel2 = new JLabel("密码:");
        pwLabel2.setBounds(0, 0, 80, 25);
        panel2.add(pwLabel2);
        JPasswordField pwText2 = new JPasswordField(15);
        pwText2.setText("123456");
        pwText2.setBounds(100, 20, 150, 25);
        panel2.add(pwText2);
        jf.add(panel2);

        JPanel buttonPanel = new JPanel();
        JButton tableButton = new JButton("获取表差异性");
        tableButton.setBounds(10, 80, 80, 25);
        buttonPanel.add(tableButton);
        JButton tableStructureButton = new JButton("获取表结构差异性");
        tableStructureButton.setBounds(10, 80, 80, 25);
        buttonPanel.add(tableStructureButton);
        JButton tableDataButton = new JButton("获取表数据差异性");
        tableDataButton.setBounds(10, 80, 80, 25);
        buttonPanel.add(tableDataButton);
        jf.add(buttonPanel);

        JTextArea resultArea = new JTextArea(25, 100);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        JScrollPane pane = new JScrollPane(resultArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jf.add(pane);

        TableCompare tableCompare = new TableCompare();
        tableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableButton == e.getSource()) {
                    String db_ip1 = ipText1.getText().toString();
                    String db_port1 = portText1.getText().toString();
                    String user_name1 = userText1.getText().toString();
                    char[] password1 = pwText1.getPassword();
                    String pass_word1 = String.valueOf(password1);
                    String db_name1 = nameText1.getText().toString();
                    String db_url1 = "jdbc:mysql://" + db_ip1 + ":" + db_port1 + "/" + db_name1 + "?useUnicode=true&characterEncoding=utf8";
                    String db_ip2 = ipText2.getText().toString();
                    String db_port2 = portText2.getText().toString();
                    String user_name2 = userText2.getText().toString();
                    char[] password2 = pwText2.getPassword();
                    String pass_word2 = String.valueOf(password2);
                    String db_name2 = nameText2.getText().toString();
                    String db_url2 = "jdbc:mysql://" + db_ip2 + ":" + db_port2 + "/" + db_name2 + "?useUnicode=true&characterEncoding=utf8";
                    if (StringUtils.isNotEmpty(db_ip1) && StringUtils.isNotEmpty(db_port1) && StringUtils.isNotEmpty(user_name1)
                            && StringUtils.isNotEmpty(pass_word1) && StringUtils.isNotEmpty(db_name1)
                            && StringUtils.isNotEmpty(db_ip2) && StringUtils.isNotEmpty(db_port2) && StringUtils.isNotEmpty(user_name2)
                            && StringUtils.isNotEmpty(pass_word2) && StringUtils.isNotEmpty(db_name2)) {
                        DataBaseDO dataBaseDO1 = new DataBaseDO();
                        dataBaseDO1.setDbUrl(db_url1);
                        dataBaseDO1.setUserName(user_name1);
                        dataBaseDO1.setPassWord(pass_word1);
                        DataBaseDO dataBaseDO2 = new DataBaseDO();
                        dataBaseDO2.setDbUrl(db_url2);
                        dataBaseDO2.setUserName(user_name2);
                        dataBaseDO2.setPassWord(pass_word2);
                        try {
                            resultArea.setText("数据对比中……");
                            resultArea.paintImmediately(resultArea.getBounds());
                            String result = tableCompare.compareTableName(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
                            resultArea.setText(result);
                        } catch (Exception ex) {
                            resultArea.setText("数据库连接失败");
                            JOptionPane.showMessageDialog(jf, "数据库连接失败");
                        }
                    } else {
                        resultArea.setText("数据库连接失败");
                        JOptionPane.showMessageDialog(jf, "数据库连接失败");
                    }
                }
            }
        });
        tableStructureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableStructureButton == e.getSource()) {
                    String db_ip1 = ipText1.getText().toString();
                    String db_port1 = portText1.getText().toString();
                    String user_name1 = userText1.getText().toString();
                    char[] password1 = pwText1.getPassword();
                    String pass_word1 = String.valueOf(password1);
                    String db_name1 = nameText1.getText().toString();
                    String db_url1 = "jdbc:mysql://" + db_ip1 + ":" + db_port1 + "/" + db_name1 + "?useUnicode=true&characterEncoding=utf8";
                    String db_ip2 = ipText2.getText().toString();
                    String db_port2 = portText2.getText().toString();
                    String user_name2 = userText2.getText().toString();
                    char[] password2 = pwText2.getPassword();
                    String pass_word2 = String.valueOf(password2);
                    String db_name2 = nameText2.getText().toString();
                    String db_url2 = "jdbc:mysql://" + db_ip2 + ":" + db_port2 + "/" + db_name2 + "?useUnicode=true&characterEncoding=utf8";
                    if (StringUtils.isNotEmpty(db_ip1) && StringUtils.isNotEmpty(db_port1) && StringUtils.isNotEmpty(user_name1)
                            && StringUtils.isNotEmpty(pass_word1) && StringUtils.isNotEmpty(db_name1)
                            && StringUtils.isNotEmpty(db_ip2) && StringUtils.isNotEmpty(db_port2) && StringUtils.isNotEmpty(user_name2)
                            && StringUtils.isNotEmpty(pass_word2) && StringUtils.isNotEmpty(db_name2)) {
                        DataBaseDO dataBaseDO1 = new DataBaseDO();
                        dataBaseDO1.setDbUrl(db_url1);
                        dataBaseDO1.setUserName(user_name1);
                        dataBaseDO1.setPassWord(pass_word1);
                        DataBaseDO dataBaseDO2 = new DataBaseDO();
                        dataBaseDO2.setDbUrl(db_url2);
                        dataBaseDO2.setUserName(user_name2);
                        dataBaseDO2.setPassWord(pass_word2);
                        try {
                            resultArea.setText("数据对比中……");
                            resultArea.paintImmediately(resultArea.getBounds());
                            String result = tableCompare.compareTableStructure(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
                            resultArea.setText(result);
                        } catch (Exception ex) {
                            resultArea.setText("数据库连接失败");
                            JOptionPane.showMessageDialog(jf, "数据库连接失败");
                        }
                    } else {
                        resultArea.setText("数据库连接失败");
                        JOptionPane.showMessageDialog(jf, "数据库连接失败");
                    }
                }
            }
        });
        tableDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableDataButton == e.getSource()) {
                    String db_ip1 = ipText1.getText().toString();
                    String db_port1 = portText1.getText().toString();
                    String user_name1 = userText1.getText().toString();
                    char[] password1 = pwText1.getPassword();
                    String pass_word1 = String.valueOf(password1);
                    String db_name1 = nameText1.getText().toString();
                    String db_url1 = "jdbc:mysql://" + db_ip1 + ":" + db_port1 + "/" + db_name1 + "?useUnicode=true&characterEncoding=utf8";
                    String db_ip2 = ipText2.getText().toString();
                    String db_port2 = portText2.getText().toString();
                    String user_name2 = userText2.getText().toString();
                    char[] password2 = pwText2.getPassword();
                    String pass_word2 = String.valueOf(password2);
                    String db_name2 = nameText2.getText().toString();
                    String db_url2 = "jdbc:mysql://" + db_ip2 + ":" + db_port2 + "/" + db_name2 + "?useUnicode=true&characterEncoding=utf8";
                    if (StringUtils.isNotEmpty(db_ip1) && StringUtils.isNotEmpty(db_port1) && StringUtils.isNotEmpty(user_name1)
                            && StringUtils.isNotEmpty(pass_word1) && StringUtils.isNotEmpty(db_name1)
                            && StringUtils.isNotEmpty(db_ip2) && StringUtils.isNotEmpty(db_port2) && StringUtils.isNotEmpty(user_name2)
                            && StringUtils.isNotEmpty(pass_word2) && StringUtils.isNotEmpty(db_name2)) {
                        DataBaseDO dataBaseDO1 = new DataBaseDO();
                        dataBaseDO1.setDbUrl(db_url1);
                        dataBaseDO1.setUserName(user_name1);
                        dataBaseDO1.setPassWord(pass_word1);
                        DataBaseDO dataBaseDO2 = new DataBaseDO();
                        dataBaseDO2.setDbUrl(db_url2);
                        dataBaseDO2.setUserName(user_name2);
                        dataBaseDO2.setPassWord(pass_word2);
                        try {
                            resultArea.setText("数据对比中……");
                            resultArea.paintImmediately(resultArea.getBounds());
                            String result = tableCompare.compareTableData(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
                            resultArea.setText(result);
                        } catch (Exception ex) {
                            resultArea.setText("数据库连接失败");
                            JOptionPane.showMessageDialog(jf, "数据库连接失败");
                        }
                    } else {
                        resultArea.setText("数据库连接失败");
                        JOptionPane.showMessageDialog(jf, "数据库连接失败");
                    }

                }
            }
        });

        jf.setSize(1200, 600);
        jf.setResizable(false);
        jf.setLocation(300, 200);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);


        //测试
        //String db_url1 = "jdbc:mysql://127.0.0.1:3306/czyth?useUnicode=true&characterEncoding=utf8";
        //String user_name1 = "root";
        //String pass_word1 = "123456";
        //String db_name1 = "czyth";
        //String db_url2 = "jdbc:mysql://127.0.0.1:3306/czyth_zs?useUnicode=true&characterEncoding=utf8";
        //String user_name2 = "root";
        //String pass_word2 = "123456";
        //String db_name2 = "czyth_zs";
        //TableCompare tableCompare = new TableCompare();
        //DataBaseDO dataBaseDO1 = new DataBaseDO();
        //dataBaseDO1.setDbUrl(db_url1);
        //dataBaseDO1.setUserName(user_name1);
        //dataBaseDO1.setPassWord(pass_word1);
        //DataBaseDO dataBaseDO2 = new DataBaseDO();
        //dataBaseDO2.setDbUrl(db_url2);
        //dataBaseDO2.setUserName(user_name2);
        //dataBaseDO2.setPassWord(pass_word2);
        //Long l1 = System.currentTimeMillis();
        ////String result = tableCompare.compareTableName(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
        ////String result = tableCompare.compareTableStructure(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
        //String result = tableCompare.compareTableData(dataBaseDO1, db_name1, dataBaseDO2, db_name2);
        //Long l2 = System.currentTimeMillis();
        //System.out.println(result);
        //System.out.println(l2 - l1);
    }

}
