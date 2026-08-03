package com.GMU.StudentManagerSystem;

import com.GMU.StudentManagerSystem.ui.StudentManagerFrame;

import javax.swing.*;

/**
 * 学生管理系统 - 图形界面版本启动类
 */
public class StudentManagerGUI {
    public static void main(String[] args) {
        // 使用系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 在事件调度线程中启动界面
        SwingUtilities.invokeLater(() -> {
            new StudentManagerFrame().setVisible(true);
        });
    }
}
