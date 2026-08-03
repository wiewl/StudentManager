package com.GMU.StudentManagerSystem.controller;

import com.GMU.StudentManagerSystem.Function;
import com.GMU.StudentManagerSystem.Student;
import com.GMU.StudentManagerSystem.ui.StudentManagerFrame;

import javax.swing.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器 - 处理业务逻辑和界面交互
 */
public class StudentController {
    private StudentManagerFrame frame;
    
    // 学院-专业映射关系
    private Map<String, String[]> collegeMajorMap;
    
    // 标记监听器是否已注册（防止重复注册）
    private boolean listenerAdded = false;

    public StudentController(StudentManagerFrame frame) {
        this.frame = frame;
        initializeCollegeMajorMap();
    }

    /**
     * 初始化学院-专业映射关系
     */
    private void initializeCollegeMajorMap() {
        collegeMajorMap = new HashMap<>();
        collegeMajorMap.put("人工智能学院", new String[]{"计算机科学与技术", "软件工程", "物联网工程", "人工智能"});
        collegeMajorMap.put("电子信息学院", new String[]{"电子信息工程", "通信工程", "自动化", "电气工程"});
        collegeMajorMap.put("机械工程学院", new String[]{"机械工程", "车辆工程", "工业设计", "智能制造"});
        collegeMajorMap.put("经济管理学院", new String[]{"工商管理", "会计学", "金融学", "市场营销"});
        collegeMajorMap.put("文学院", new String[]{"汉语言文学", "新闻学", "广告学", "秘书学"});
        collegeMajorMap.put("理学院", new String[]{"数学与应用数学", "物理学", "化学", "生物科学"});
    }

    /**
     * 初始化界面数据
     */
    public void initializeData() {
        // 初始化学院下拉框
        String[] colleges = collegeMajorMap.keySet().toArray(new String[0]);
        frame.getCbCollege().removeAllItems();
        for (String college : colleges) {
            frame.getCbCollege().addItem(college);
        }
        
        // 设置默认学院的专业列表
        if (colleges.length > 0) {
            updateMajorCombo(colleges[0]);
        }
        
        // 添加学院选择监听器（联动更新专业，仅注册一次）
        if (!listenerAdded) {
            frame.getCbCollege().addActionListener(e -> {
                String selectedCollege = (String) frame.getCbCollege().getSelectedItem();
                if (selectedCollege != null) {
                    updateMajorCombo(selectedCollege);
                }
            });
            listenerAdded = true;
        }
        
        // 加载所有学生数据
        loadAllStudents();
    }

    /**
     * 根据学院更新专业下拉框
     */
    private void updateMajorCombo(String college) {
        String[] majors = collegeMajorMap.get(college);
        frame.getCbMajor().removeAllItems();
        if (majors != null) {
            for (String major : majors) {
                frame.getCbMajor().addItem(major);
            }
        }
    }

    /**
     * 加载所有学生数据到表格
     */
    private void loadAllStudents() {
        try {
            List<Student> students = Function.queryAll();
            refreshTable(students);
            updateStatus("共加载 " + students.size() + " 条记录");
        } catch (SQLException e) {
            showError("加载数据失败：" + e.getMessage());
        }
    }

    /**
     * 刷新表格数据
     */
    private void refreshTable(List<Student> students) {
        frame.getTableModel().setRowCount(0); // 清空表格
        
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getName(),
                student.getGender(),
                student.getMajor(),
                student.getCollege(),
                student.getClazz()
            };
            frame.getTableModel().addRow(row);
        }
    }

    /**
     * 添加学生
     */
    public void addStudent() {
        try {
            // 获取表单数据
            String idStr = frame.getTxtStudentId().getText().trim();
            String name = frame.getTxtName().getText().trim();
            String gender = frame.getRadioMale().isSelected() ? "男" : "女";
            String college = (String) frame.getCbCollege().getSelectedItem();
            String major = (String) frame.getCbMajor().getSelectedItem();
            
            String clazz = frame.getTxtClazz().getText().trim();

            // 验证输入
            if (idStr.isEmpty() || name.isEmpty()) {
                showWarning("请填写完整的学号和姓名！");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            
            // 检查学号是否已存在
            if (Function.exists(id)) {
                showWarning("学号为 " + id + " 的学生已存在！");
                return;
            }
            
            // 创建学生对象并添加
            Student student = new Student(id, name, gender, major, college, clazz);
            int rows = Function.add(student);
            
            if (rows > 0) {
                showMessage("添加成功！");
                clearForm();
                loadAllStudents();
            } else {
                showError("添加失败！");
            }
            
        } catch (NumberFormatException e) {
            showWarning("学号必须是数字！");
        } catch (SQLException e) {
            showError("数据库错误：" + e.getMessage());
        }
    }

    /**
     * 删除学生
     */
    public void deleteStudent() {
        String idStr = frame.getTxtStudentId().getText().trim();
        
        if (idStr.isEmpty()) {
            showWarning("请先选择要删除的学生（双击表格行）！");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // 1. 检查学生是否存在
            Student student = Function.queryById(id);
            if (student == null) {
                showWarning("学号为 " + id + " 的学生不存在！");
                return;
            }
            
            // 2. 构造确认信息
            String message = "确定要删除以下学生吗？\n\n" +
                    "学号：" + student.getId() + "\n" +
                    "姓名：" + student.getName() + "\n" +
                    "性别：" + student.getGender() + "\n" +
                    "专业：" + student.getMajor() + "\n" +
                    "学院：" + student.getCollege() + "\n" +
                    "班级：" + student.getClazz() + "\n\n" +
                    "此操作不可恢复！";
            
            // 3. 弹出确认对话框
            int choice = JOptionPane.showConfirmDialog(frame,
                    message, "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            // 4. 用户点击"是"才执行删除
            if (choice == JOptionPane.YES_OPTION) {
                int rows = Function.delete(id);
                if (rows > 0) {
                    showMessage("学生 " + student.getName() + "（学号：" + id + "）已成功删除！");
                    clearForm();
                    loadAllStudents();
                } else {
                    showError("删除失败！");
                }
            }
            
        } catch (NumberFormatException e) {
            showWarning("学号格式错误！");
        } catch (SQLException e) {
            showError("数据库错误：" + e.getMessage());
        }
    }

    /**
     * 修改学生（仅更新勾选的字段）
     */
    public void updateStudent() {
        try {
            String idStr = frame.getTxtStudentId().getText().trim();
            
            if (idStr.isEmpty()) {
                showWarning("请先选择要修改的学生（双击表格行）！");
                return;
            }
            
            int id = Integer.parseInt(idStr);

            // 收集勾选的字段，只更新被勾选的部分
            Map<String, String> fieldUpdates = new LinkedHashMap<>();

            if (frame.getChkName().isSelected()) {
                String name = frame.getTxtName().getText().trim();
                if (name.isEmpty()) {
                    showWarning("勾选了姓名但姓名不能为空！");
                    return;
                }
                fieldUpdates.put("name", name);
            }

            if (frame.getChkGender().isSelected()) {
                String gender = frame.getRadioMale().isSelected() ? "男" : "女";
                fieldUpdates.put("gender", gender);
            }

            if (frame.getChkCollege().isSelected()) {
                String college = (String) frame.getCbCollege().getSelectedItem();
                fieldUpdates.put("college", college);
            }

            if (frame.getChkMajor().isSelected()) {
                String major = (String) frame.getCbMajor().getSelectedItem();
                fieldUpdates.put("major", major);
            }

            if (frame.getChkClazz().isSelected()) {
                String clazz = frame.getTxtClazz().getText().trim();
                fieldUpdates.put("clazz", clazz);
            }

            if (fieldUpdates.isEmpty()) {
                showWarning("请至少勾选一个要修改的字段！");
                return;
            }

            // 调用 DAO 层部分更新方法
            int rows = Function.updatePartial(id, fieldUpdates);
            
            if (rows > 0) {
                showMessage("修改成功！共更新了 " + fieldUpdates.size() + " 个字段。");
                clearForm();
                loadAllStudents();
            } else {
                showError("修改失败！请检查学号是否存在。");
            }
            
        } catch (NumberFormatException e) {
            showWarning("学号格式错误！");
        } catch (SQLException e) {
            showError("数据库错误：" + e.getMessage());
        }
    }

    /**
     * 清空表单
     */
    public void clearForm() {
        frame.getTxtStudentId().setText("");
        frame.getTxtName().setText("");
        frame.getTxtClazz().setText("");
        frame.getRadioMale().setSelected(true);
        
        // 重置学号文本框为可编辑状态
        frame.getTxtStudentId().setEnabled(true);

        // 取消所有复选框的勾选状态
        frame.getChkName().setSelected(false);
        frame.getChkGender().setSelected(false);
        frame.getChkCollege().setSelected(false);
        frame.getChkMajor().setSelected(false);
        frame.getChkClazz().setSelected(false);

        // 重置学院和专业到默认值
        if (frame.getCbCollege().getItemCount() > 0) {
            frame.getCbCollege().setSelectedIndex(0);
        }
    }

    /**
     * 搜索学生
     */
    public void searchStudents() {
        String searchType = (String) frame.getCbSearchType().getSelectedItem();
        String keyword = frame.getTxtSearch().getText().trim();
        
        if (keyword.isEmpty()) {
            loadAllStudents();
            return;
        }
        
        try {
            List<Student> students = null;
            
            switch (searchType) {
                case "学号":
                    try {
                        int id = Integer.parseInt(keyword);
                        Student student = Function.queryById(id);
                        if (student != null) {
                            students = List.of(student);
                        } else {
                            students = List.of();
                        }
                    } catch (NumberFormatException e) {
                        showWarning("学号必须是数字！");
                        return;
                    }
                    break;
                    
                case "姓名":
                    students = Function.queryByName(keyword);
                    break;
                    
                case "性别":
                    students = Function.queryByGender(keyword);
                    break;
                    
                case "专业":
                    students = Function.queryByMajor(keyword);
                    break;
                    
                case "学院":
                    students = Function.queryByCollege(keyword);
                    break;
                    
                case "班级":
                    students = Function.queryByClazz(keyword);
                    break;
                    
                default:
                    loadAllStudents();
                    return;
            }
            
            if (students == null) {
                students = List.of();
            }
            
            refreshTable(students);
            updateStatus("找到 " + students.size() + " 条记录");
            
        } catch (SQLException e) {
            showError("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 从表格回填数据到表单
     */
    public void fillFormFromTable() {
        int selectedRow = frame.getStudentTable().getSelectedRow();
        
        if (selectedRow == -1) {
            showWarning("请先选择一行数据！");
            return;
        }
        
        // 从表格获取数据
        int id = (int) frame.getTableModel().getValueAt(selectedRow, 0);
        String name = (String) frame.getTableModel().getValueAt(selectedRow, 1);
        String gender = (String) frame.getTableModel().getValueAt(selectedRow, 2);
        String major = (String) frame.getTableModel().getValueAt(selectedRow, 3);
        String college = (String) frame.getTableModel().getValueAt(selectedRow, 4);
        String clazz = (String) frame.getTableModel().getValueAt(selectedRow, 5);
        
        // 回填到表单
        frame.getTxtStudentId().setText(String.valueOf(id));
        frame.getTxtStudentId().setEnabled(false); // 禁用学号编辑
        
        frame.getTxtName().setText(name);
        frame.getTxtClazz().setText(clazz);
        
        if ("男".equals(gender)) {
            frame.getRadioMale().setSelected(true);
        } else {
            frame.getRadioFemale().setSelected(true);
        }
        
        // 设置学院
        frame.getCbCollege().setSelectedItem(college);
        
        // 设置专业（会触发联动更新）
        frame.getCbMajor().setSelectedItem(major);

        // 取消所有复选框的勾选状态（由用户自行勾选需要修改的字段）
        frame.getChkName().setSelected(false);
        frame.getChkGender().setSelected(false);
        frame.getChkCollege().setSelected(false);
        frame.getChkMajor().setSelected(false);
        frame.getChkClazz().setSelected(false);

        updateStatus("已选择学号 " + id + " 的学生信息，请勾选需要修改的字段");
    }

    /**
     * 更新状态栏
     */
    private void updateStatus(String message) {
        frame.getLblStatus().setText(message);
    }

    /**
     * 显示错误消息
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 显示警告消息
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(frame, message, "警告", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 显示信息消息
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
