package com.GMU.StudentManagerSystem;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentManager {
    public static void main(String[] args) {
        System.out.println("==========学籍信息管理系统==========");
        int choice;
        int id;
        String name;
        String gender;
        String major;
        String college;
        String clazz;
        boolean running = true;
        Scanner sc = new Scanner(System.in);
        while (running) {
            System.out.println("\n请选择想要使用的功能：");
            System.out.println("1. 添加学生信息");
            System.out.println("2. 删除学生信息");
            System.out.println("3. 修改学生信息");
            System.out.println("4. 查询学生信息");
            System.out.println("5. 检查学生是否存在");
            System.out.println("6. 退出系统");
            System.out.print("请输入选项：");
            choice = readInt(sc);
            switch (choice) {
                case 1:
                    // ========== 添加学生信息 ==========
                    System.out.println("请输入学号，姓名，性别，专业，学院，班级（空格分隔）");
                    id = readInt(sc);
                    name = sc.next();
                    gender = readValidGender(sc);
                    major = sc.next();
                    college = sc.next();
                    clazz = sc.next();
                    try {
                        // 先检查学号是否已存在
                        if (Function.exists(id)) {
                            System.out.println("添加失败！学号为 " + id + " 的学生已存在。");
                        } else {
                            Student stu1 = new Student(id, name, gender, major, college, clazz);
                            int addreturn = Function.add(stu1);
                            if (addreturn > 0) {
                                System.out.println("添加成功！");
                            } else {
                                System.out.println("添加失败！");
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("数据库错误：" + e.getMessage());
                    }
                    break;

                case 2:
                    // ========== 删除学生信息 ==========
                    System.out.println("请选择删除方式：");
                    System.out.println("1. 按学号删除（控制台确认）");
                    System.out.println("2. 批量删除（控制台确认）");
                    System.out.print("请输入选项：");
                    int deleteChoice = readInt(sc);
                    switch (deleteChoice) {
                        case 1:
                            // 控制台确认删除
                            System.out.println("请输入需要删除的学生的学号");
                            id = readInt(sc);
                            try {
                                Student stu = Function.queryById(id);
                                if (stu == null) {
                                    System.out.println("学号为 " + id + " 的学生不存在！");
                                } else {
                                    System.out.println("确定要删除以下学生吗？");
                                    printStudent(stu);
                                    System.out.print("确认删除请输入 y：");
                                    String confirm = sc.next();
                                    if ("y".equalsIgnoreCase(confirm)) {
                                        int rows = Function.delete(id);
                                        if (rows > 0) {
                                            System.out.println("删除成功！");
                                        } else {
                                            System.out.println("删除失败！");
                                        }
                                    } else {
                                        System.out.println("已取消删除。");
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 2:
                            // 批量删除（控制台确认）
                            System.out.println("请输入要删除的学生学号数量：");
                            int count = readInt(sc);
                            int[] ids = new int[count];
                            System.out.println("请输入 " + count + " 个学号（空格分隔）：");
                            for (int i = 0; i < count; i++) {
                                ids[i] = readInt(sc);
                            }
                            
                            // 显示待删学生列表
                            System.out.println("\n确定要删除以下 " + count + " 名学生吗？\n");
                            int foundCount = 0;
                            try {
                                for (int sid : ids) {
                                    Student s = Function.queryById(sid);
                                    if (s != null) {
                                        System.out.println("● " + s.getId() + "  " + s.getName() + "  " + s.getMajor());
                                        foundCount++;
                                    } else {
                                        System.out.println("● 学号 " + sid + " —— 不存在");
                                    }
                                }
                                System.out.println("\n此操作不可恢复！");
                                System.out.print("确认删除请输入 y：");
                                String confirm = sc.next();
                                
                                if ("y".equalsIgnoreCase(confirm)) {
                                    int rows = Function.deleteBatch(ids);
                                    System.out.println("批量删除完成！共删除 " + rows + " 条记录。");
                                } else {
                                    System.out.println("已取消删除。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("无效选项！");
                    }
                    break;

                case 3:
                    // ========== 修改学生信息 ==========
                    System.out.println("请选择需要修改的内容");
                    System.out.println("1. 修改全部信息（姓名，性别，专业，学院，班级）");
                    System.out.println("2. 修改姓名");
                    System.out.println("3. 修改性别");
                    System.out.println("4. 修改专业");
                    System.out.println("5. 修改学院");
                    System.out.println("6. 修改班级");
                    System.out.print("请输入选项：");
                    int modifychoice = readInt(sc);
                    switch (modifychoice) {
                        case 1:
                            // 修改全部信息
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的姓名，性别，专业，学院，班级（空格分隔）");
                            name = sc.next();
                            gender = readValidGender(sc);
                            major = sc.next();
                            college = sc.next();
                            clazz = sc.next();
                            Student stu2 = new Student(id, name, gender, major, college, clazz);
                            try {
                                int modifyreturn = Function.update(stu2);
                                if (modifyreturn > 0) {
                                    System.out.println("修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 2:
                            // 修改姓名
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的姓名");
                            name = sc.next();
                            try {
                                int r1 = Function.updateName(id, name);
                                if (r1 > 0) {
                                    System.out.println("姓名修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 3:
                            // 修改性别
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的性别");
                            gender = readValidGender(sc);
                            try {
                                int r2 = Function.updateGender(id, gender);
                                if (r2 > 0) {
                                    System.out.println("性别修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 4:
                            // 修改专业
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的专业");
                            major = sc.next();
                            try {
                                int r3 = Function.updateMajor(id, major);
                                if (r3 > 0) {
                                    System.out.println("专业修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 5:
                            // 修改学院
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的学院");
                            college = sc.next();
                            try {
                                int r4 = Function.updateCollege(id, college);
                                if (r4 > 0) {
                                    System.out.println("学院修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        case 6:
                            // 修改班级
                            System.out.println("请输入要修改的学生的学号");
                            id = readInt(sc);
                            System.out.println("请输入新的班级");
                            clazz = sc.next();
                            try {
                                int r5 = Function.updateClazz(id, clazz);
                                if (r5 > 0) {
                                    System.out.println("班级修改成功！");
                                } else {
                                    System.out.println("修改失败！请检查学号是否存在。");
                                }
                            } catch (SQLException e) {
                                System.out.println("数据库错误：" + e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("无效选项！");
                    }
                    break;

                case 4:
                    // ========== 查询学生信息 ==========
                    System.out.println("请选择查询方式：");
                    System.out.println("1. 查询所有学生");
                    System.out.println("2. 按学号查询");
                    System.out.println("3. 按姓名查询（模糊）");
                    System.out.println("4. 按性别查询");
                    System.out.println("5. 按专业查询（模糊）");
                    System.out.println("6. 按学院查询（模糊）");
                    System.out.println("7. 按班级查询（模糊）");
                    System.out.print("请输入选项：");
                    int queryChoice = readInt(sc);
                    try {
                        switch (queryChoice) {
                            case 1:
                                // 查询所有学生
                                List<Student> allStudents = Function.queryAll();
                                if (allStudents.isEmpty()) {
                                    System.out.println("暂无学生信息。");
                                } else {
                                    System.out.println("========== 所有学生信息 ==========");
                                    for (Student s : allStudents) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + allStudents.size() + " 条记录");
                                }
                                break;
                            case 2:
                                // 按学号查询
                                System.out.println("请输入要查询的学号");
                                id = readInt(sc);
                                Student stu3 = Function.queryById(id);
                                if (stu3 == null) {
                                    System.out.println("未找到学号为 " + id + " 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    printStudent(stu3);
                                }
                                break;
                            case 3:
                                // 按姓名模糊查询
                                System.out.println("请输入要查询的姓名（支持模糊查询）");
                                name = sc.next();
                                List<Student> nameResult = Function.queryByName(name);
                                if (nameResult.isEmpty()) {
                                    System.out.println("未找到姓名包含 \"" + name + "\" 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    for (Student s : nameResult) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + nameResult.size() + " 条记录");
                                }
                                break;
                            case 4:
                                // 按性别查询
                                System.out.println("请输入要查询的性别");
                                gender = readValidGender(sc);
                                List<Student> genderResult = Function.queryByGender(gender);
                                if (genderResult.isEmpty()) {
                                    System.out.println("未找到性别为 \"" + gender + "\" 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    for (Student s : genderResult) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + genderResult.size() + " 条记录");
                                }
                                break;
                            case 5:
                                // 按专业模糊查询
                                System.out.println("请输入要查询的专业（支持模糊查询）");
                                major = sc.next();
                                List<Student> majorResult = Function.queryByMajor(major);
                                if (majorResult.isEmpty()) {
                                    System.out.println("未找到专业包含 \"" + major + "\" 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    for (Student s : majorResult) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + majorResult.size() + " 条记录");
                                }
                                break;
                            case 6:
                                // 按学院模糊查询
                                System.out.println("请输入要查询的学院（支持模糊查询）");
                                college = sc.next();
                                List<Student> collegeResult = Function.queryByCollege(college);
                                if (collegeResult.isEmpty()) {
                                    System.out.println("未找到学院包含 \"" + college + "\" 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    for (Student s : collegeResult) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + collegeResult.size() + " 条记录");
                                }
                                break;
                            case 7:
                                // 按班级模糊查询
                                System.out.println("请输入要查询的班级（支持模糊查询）");
                                clazz = sc.next();
                                List<Student> clazzResult = Function.queryByClazz(clazz);
                                if (clazzResult.isEmpty()) {
                                    System.out.println("未找到班级包含 \"" + clazz + "\" 的学生。");
                                } else {
                                    System.out.println("========== 查询结果 ==========");
                                    for (Student s : clazzResult) {
                                        printStudent(s);
                                        System.out.println("----------------------------");
                                    }
                                    System.out.println("共 " + clazzResult.size() + " 条记录");
                                }
                                break;
                            default:
                                System.out.println("无效选项！");
                        }
                    } catch (SQLException e) {
                        System.out.println("数据库错误：" + e.getMessage());
                    }
                    break;

                case 5:
                    // ========== 检查学生是否存在 ==========
                    System.out.println("请输入要检查的学号");
                    id = readInt(sc);
                    try {
                        boolean exists = Function.exists(id);
                        if (exists) {
                            System.out.println("学号为 " + id + " 的学生存在。");
                        } else {
                            System.out.println("学号为 " + id + " 的学生不存在。");
                        }
                    } catch (SQLException e) {
                        System.out.println("数据库错误：" + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("退出系统，再见！");
                    running = false;
                    break;

                default:
                    System.out.println("无效选项，请重新选择！");
            }
        }
        sc.close();
    }

    /**
     * 读取并验证整数输入（防止 InputMismatchException 崩溃）
     */
    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字！");
                sc.nextLine(); // 清除错误的输入
            }
        }
    }

    /**
     * 读取并验证性别输入（必须为"男"或"女"）
     */
    private static String readValidGender(Scanner sc) {
        while (true) {
            String input = sc.next();
            if ("男".equals(input) || "女".equals(input)) {
                return input;
            }
            System.out.print("性别必须输入\"男\"或\"女\"，请重新输入：");
        }
    }

    /**
     * 打印学生信息
     */
    private static void printStudent(Student s) {
        System.out.println("学号：" + s.getId());
        System.out.println("姓名：" + s.getName());
        System.out.println("性别：" + s.getGender());
        System.out.println("专业：" + s.getMajor());
        System.out.println("学院：" + s.getCollege());
        System.out.println("班级：" + s.getClazz());
    }
}
