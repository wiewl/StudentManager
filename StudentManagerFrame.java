package com.GMU.StudentManagerSystem.ui;

import com.GMU.StudentManagerSystem.controller.StudentController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 学生管理系统主窗口
 * @author Xiao
 */
public class StudentManagerFrame extends JFrame {
    private StudentController controller;
    
    // 输入面板组件
    private JTextField txtStudentId;
    private JTextField txtName;
    private JRadioButton radioMale;
    private JRadioButton radioFemale;
    private ButtonGroup genderGroup;
    private JComboBox<String> cbCollege;
    private JComboBox<String> cbMajor;
    private JTextField txtClazz;

    // 修改功能复选框（勾选后该字段才会被更新）
    private JCheckBox chkName;
    private JCheckBox chkGender;
    private JCheckBox chkCollege;
    private JCheckBox chkMajor;
    private JCheckBox chkClazz;
    
    // 表格面板组件
    private JComboBox<String> cbSearchType;
    private JTextField txtSearch;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    // 状态栏
    private JLabel lblStatus;

    public StudentManagerFrame() {
        initComponents();
        controller = new StudentController(this);
        controller.initializeData();
    }

    private void initComponents() {
        //======== this ========
        setTitle("学籍信息管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));
        
        // 使用 BorderLayout 作为主布局
        setLayout(new BorderLayout(0, 0));
        
        //======== 顶部标题栏 ========
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 51, 102)); // 深蓝色
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        
        JLabel lblTitle = new JLabel("学籍信息管理系统");
        lblTitle.setFont(new Font("微软雅黑", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        //======== 中间内容区域 ========
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(null);
        
        //======== 左侧输入面板 ========
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "学生信息录入",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(0, 51, 102)
        ));
        inputPanel.setLayout(null);
        inputPanel.setMinimumSize(new Dimension(300, 0));
        
        // 学号标签和文本框
        JLabel lblStudentId = new JLabel("学号:");
        lblStudentId.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblStudentId.setBounds(30, 30, 60, 25);
        inputPanel.add(lblStudentId);
        
        txtStudentId = new JTextField();
        txtStudentId.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        txtStudentId.setBounds(100, 30, 180, 28);
        inputPanel.add(txtStudentId);
        
        // 姓名标签和文本框
        JLabel lblName = new JLabel("姓名:");
        lblName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblName.setBounds(30, 70, 60, 25);
        inputPanel.add(lblName);
        
        txtName = new JTextField();
        txtName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        txtName.setBounds(100, 70, 180, 28);
        inputPanel.add(txtName);
        
        chkName = new JCheckBox();
        chkName.setBounds(8, 70, 20, 28);
        chkName.setToolTipText("勾选后修改时将更新姓名");
        inputPanel.add(chkName);
        
        // 性别标签和单选按钮组
        JLabel lblGender = new JLabel("性别:");
        lblGender.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblGender.setBounds(30, 110, 60, 25);
        inputPanel.add(lblGender);
        
        radioMale = new JRadioButton("男");
        radioMale.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        radioMale.setBounds(100, 110, 60, 25);
        radioMale.setSelected(true);
        inputPanel.add(radioMale);
        
        radioFemale = new JRadioButton("女");
        radioFemale.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        radioFemale.setBounds(170, 110, 60, 25);
        inputPanel.add(radioFemale);
        
        genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        
        chkGender = new JCheckBox();
        chkGender.setBounds(8, 110, 20, 25);
        chkGender.setToolTipText("勾选后修改时将更新性别");
        inputPanel.add(chkGender);
        
        // 学院标签和下拉框
        JLabel lblCollege = new JLabel("学院:");
        lblCollege.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblCollege.setBounds(30, 150, 60, 25);
        inputPanel.add(lblCollege);
        
        cbCollege = new JComboBox<>();
        cbCollege.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        cbCollege.setBounds(100, 150, 180, 28);
        inputPanel.add(cbCollege);
        
        chkCollege = new JCheckBox();
        chkCollege.setBounds(8, 150, 20, 28);
        chkCollege.setToolTipText("勾选后修改时将更新学院");
        inputPanel.add(chkCollege);
        
        // 专业标签和下拉框
        JLabel lblMajor = new JLabel("专业:");
        lblMajor.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblMajor.setBounds(30, 190, 60, 25);
        inputPanel.add(lblMajor);
        
        cbMajor = new JComboBox<>();
        cbMajor.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        cbMajor.setBounds(100, 190, 180, 28);
        inputPanel.add(cbMajor);
        
        chkMajor = new JCheckBox();
        chkMajor.setBounds(8, 190, 20, 28);
        chkMajor.setToolTipText("勾选后修改时将更新专业");
        inputPanel.add(chkMajor);

        // 班级标签和文本框
        JLabel lblClazz = new JLabel("班级:");
        lblClazz.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblClazz.setBounds(30, 230, 60, 25);
        inputPanel.add(lblClazz);

        txtClazz = new JTextField();
        txtClazz.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        txtClazz.setBounds(100, 230, 180, 28);
        inputPanel.add(txtClazz);
        
        chkClazz = new JCheckBox();
        chkClazz.setBounds(8, 230, 20, 28);
        chkClazz.setToolTipText("勾选后修改时将更新班级");
        inputPanel.add(chkClazz);
        
        // 提示标签
        JLabel lblHint = new JLabel("提示：修改时请勾选需要更改的字段");
        lblHint.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        lblHint.setForeground(Color.GRAY);
        lblHint.setBounds(20, 265, 280, 20);
        inputPanel.add(lblHint);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBounds(20, 290, 310, 50);
        
        JButton btnAdd = new JButton("添加");
        btnAdd.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        btnAdd.setPreferredSize(new Dimension(70, 35));
        btnAdd.addActionListener(e -> controller.addStudent());
        buttonPanel.add(btnAdd);
        
        JButton btnDelete = new JButton("删除");
        btnDelete.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        btnDelete.setPreferredSize(new Dimension(70, 35));
        btnDelete.addActionListener(e -> controller.deleteStudent());
        buttonPanel.add(btnDelete);
        
        JButton btnUpdate = new JButton("修改");
        btnUpdate.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        btnUpdate.setPreferredSize(new Dimension(70, 35));
        btnUpdate.setToolTipText("仅更新勾选的字段");
        btnUpdate.addActionListener(e -> controller.updateStudent());
        buttonPanel.add(btnUpdate);
        
        JButton btnClear = new JButton("清空");
        btnClear.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        btnClear.setPreferredSize(new Dimension(70, 35));
        btnClear.addActionListener(e -> controller.clearForm());
        buttonPanel.add(btnClear);
        
        inputPanel.add(buttonPanel);
        
        splitPane.setLeftComponent(inputPanel);
        
        //======== 右侧表格面板 ========
        JPanel tablePanel = new JPanel(new BorderLayout(0, 0));
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "学生信息列表",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(0, 51, 102)
        ));
        
        // 搜索栏
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel lblSearchType = new JLabel("搜索方式:");
        lblSearchType.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchPanel.add(lblSearchType);
        
        cbSearchType = new JComboBox<>(new String[]{"学号", "姓名", "性别", "专业", "学院", "班级"});
        cbSearchType.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchPanel.add(cbSearchType);
        
        JLabel lblSearch = new JLabel("关键词:");
        lblSearch.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchPanel.add(lblSearch);
        
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(200, 28));
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("查询");
        btnSearch.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        btnSearch.addActionListener(e -> controller.searchStudents());
        searchPanel.add(btnSearch);
        
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        
        // 表格
        String[] columnNames = {"学号", "姓名", "性别", "专业", "学院", "班级"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        studentTable.setRowHeight(28);
        studentTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 双击表格行回填数据
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    controller.fillFormFromTable();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        splitPane.setRightComponent(tablePanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        //======== 底部状态栏 ========
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statusPanel.setPreferredSize(new Dimension(getWidth(), 35));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        lblStatus = new JLabel("就绪");
        lblStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusPanel.add(lblStatus);
        
        add(statusPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null); // 居中显示
    }

    // ==================== Getter 方法（供 Controller 调用）====================
    
    public JTextField getTxtStudentId() {
        return txtStudentId;
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JRadioButton getRadioMale() {
        return radioMale;
    }

    public JRadioButton getRadioFemale() {
        return radioFemale;
    }

    public JComboBox<String> getCbCollege() {
        return cbCollege;
    }

    public JComboBox<String> getCbMajor() {
        return cbMajor;
    }

    public JTextField getTxtClazz() {
        return txtClazz;
    }

    public JCheckBox getChkName() {
        return chkName;
    }

    public JCheckBox getChkGender() {
        return chkGender;
    }

    public JCheckBox getChkCollege() {
        return chkCollege;
    }

    public JCheckBox getChkMajor() {
        return chkMajor;
    }

    public JCheckBox getChkClazz() {
        return chkClazz;
    }

    public JComboBox<String> getCbSearchType() {
        return cbSearchType;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JTable getStudentTable() {
        return studentTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JLabel getLblStatus() {
        return lblStatus;
    }

}
