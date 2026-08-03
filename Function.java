package com.GMU.StudentManagerSystem;
import java.sql.*;
import java.util.*;

public class Function {
    // ==================== 数据库连接配置 ====================
    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "xsh061218";

    /**
     * 获取数据库连接
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 将 ResultSet 中的一行数据封装为 Student 对象
     */
    private static Student mapStudent(ResultSet rs) throws SQLException {
        int id = rs.getInt("student_id");
        String name = rs.getString("name");
        String gender = rs.getString("gender");
        String major = rs.getString("major");
        String college = rs.getString("college");
        String clazz = rs.getString("clazz");
        return new Student(id, name, gender, major, college, clazz);
    }

    // ==================== 查询功能 ====================

    /**
     * 查询所有学生
     * @return 所有学生列表
     */
    public static List<Student> queryAll() throws SQLException {
        String sql = "SELECT * FROM student";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                studentList.add(mapStudent(rs));
            }
        }
        return studentList;
    }

    /**
     * 按学号查询学生
     * @param studentId 学号
     * @return 匹配的学生对象，未找到返回 null
     */
    public static Student queryById(int studentId) throws SQLException {
        String sql = "SELECT * FROM student WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        }
        return null;
    }

    /**
     * 按姓名查询学生（支持模糊查询）
     * @param name 姓名
     * @return 匹配的学生列表
     */
    public static List<Student> queryByName(String name) throws SQLException {
        String sql = "SELECT * FROM student WHERE name LIKE ?";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentList.add(mapStudent(rs));
                }
            }
        }
        return studentList;
    }

    /**
     * 按性别查询学生
     * @param gender 性别
     * @return 匹配的学生列表
     */
    public static List<Student> queryByGender(String gender) throws SQLException {
        String sql = "SELECT * FROM student WHERE gender = ?";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gender);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentList.add(mapStudent(rs));
                }
            }
        }
        return studentList;
    }

    /**
     * 按专业查询学生（支持模糊查询）
     * @param major 专业
     * @return 匹配的学生列表
     */
    public static List<Student> queryByMajor(String major) throws SQLException {
        String sql = "SELECT * FROM student WHERE major LIKE ?";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + major + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentList.add(mapStudent(rs));
                }
            }
        }
        return studentList;
    }

    /**
     * 按学院查询学生（支持模糊查询）
     * @param college 学院
     * @return 匹配的学生列表
     */
    public static List<Student> queryByCollege(String college) throws SQLException {
        String sql = "SELECT * FROM student WHERE college LIKE ?";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + college + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentList.add(mapStudent(rs));
                }
            }
        }
        return studentList;
    }

    /**
     * 按班级查询学生（支持模糊查询）
     * @param clazz 班级
     * @return 匹配的学生列表
     */
    public static List<Student> queryByClazz(String clazz) throws SQLException {
        String sql = "SELECT * FROM student WHERE clazz LIKE ?";
        List<Student> studentList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + clazz + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentList.add(mapStudent(rs));
                }
            }
        }
        return studentList;
    }

    // ==================== 添加功能 ====================

    /**
     * 添加学生
     * @param student 学生对象
     * @return 受影响的行数（>0 表示成功）
     */
    public static int add(Student student) throws SQLException {
        String sql = "INSERT INTO student (student_id, name, gender, major, college, clazz) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getMajor());
            pstmt.setString(5, student.getCollege());
            pstmt.setString(6, student.getClazz());

            return pstmt.executeUpdate();
        }
    }

    // ==================== 更新功能 ====================

    /**
     * 更新学生全部信息（按学号定位）
     * @param student 学生对象（包含要更新的全部字段）
     * @return 受影响的行数（>0 表示成功）
     */
    public static int update(Student student) throws SQLException {
        String sql = "UPDATE student SET name = ?, gender = ?, major = ?, college = ?, clazz = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGender());
            pstmt.setString(3, student.getMajor());
            pstmt.setString(4, student.getCollege());
            pstmt.setString(5, student.getClazz());
            pstmt.setInt(6, student.getId());

            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号部分更新学生信息（动态拼接 SQL，只更新指定字段）
     * @param studentId 学号
     * @param fieldUpdates 字段名 -> 新值 的映射（支持的 key: name, gender, major, college, clazz）
     * @return 受影响的行数（>0 表示成功）
     */
    public static int updatePartial(int studentId, Map<String, String> fieldUpdates) throws SQLException {
        if (fieldUpdates == null || fieldUpdates.isEmpty()) {
            return 0;
        }

        // 白名单校验，防止 SQL 注入
        Set<String> allowedFields = new HashSet<>(Arrays.asList("name", "gender", "major", "college", "clazz"));
        for (String field : fieldUpdates.keySet()) {
            if (!allowedFields.contains(field)) {
                throw new IllegalArgumentException("非法字段名: " + field);
            }
        }

        // 动态拼接 UPDATE 语句
        StringBuilder sql = new StringBuilder("UPDATE student SET ");
        List<String> setClauses = new ArrayList<>();
        for (String field : fieldUpdates.keySet()) {
            setClauses.add(field + " = ?");
        }
        sql.append(String.join(", ", setClauses));
        sql.append(" WHERE student_id = ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (String value : fieldUpdates.values()) {
                pstmt.setString(index++, value);
            }
            pstmt.setInt(index, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号更新姓名
     * @param studentId 学号
     * @param name 新姓名
     * @return 受影响的行数
     */
    public static int updateName(int studentId, String name) throws SQLException {
        String sql = "UPDATE student SET name = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号更新性别
     * @param studentId 学号
     * @param gender 新性别
     * @return 受影响的行数
     */
    public static int updateGender(int studentId, String gender) throws SQLException {
        String sql = "UPDATE student SET gender = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gender);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号更新专业
     * @param studentId 学号
     * @param major 新专业
     * @return 受影响的行数
     */
    public static int updateMajor(int studentId, String major) throws SQLException {
        String sql = "UPDATE student SET major = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号更新学院
     * @param studentId 学号
     * @param college 新学院
     * @return 受影响的行数
     */
    public static int updateCollege(int studentId, String college) throws SQLException {
        String sql = "UPDATE student SET college = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, college);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 按学号更新班级
     * @param studentId 学号
     * @param clazz 新班级
     * @return 受影响的行数
     */
    public static int updateClazz(int studentId, String clazz) throws SQLException {
        String sql = "UPDATE student SET clazz = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clazz);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate();
        }
    }

    // ==================== 删除功能 ====================

    /**
     * 按学号删除学生（带事务支持）
     * @param studentId 学号
     * @return 受影响的行数（>0 表示成功）
     */
    public static int delete(int studentId) throws SQLException {
        String sql = "DELETE FROM student WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 批量删除学生（带事务支持）
     * @param studentIds 学号数组
     * @return 总共删除的行数
     */
    public static int deleteBatch(int[] studentIds) throws SQLException {
        String sql = "DELETE FROM student WHERE student_id = ?";
        int totalDeleted = 0;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // 开启事务
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int id : studentIds) {
                    pstmt.setInt(1, id);
                    totalDeleted += pstmt.executeUpdate();
                }
                conn.commit(); // 提交事务
            } catch (SQLException e) {
                conn.rollback(); // 回滚事务
                throw e;
            } finally {
                conn.setAutoCommit(true); // 恢复自动提交
            }
        }
        return totalDeleted;
    }

    /**
     * 检查指定学号的学生是否存在
     * @param studentId 学号
     * @return true=存在，false=不存在
     */
    public static boolean exists(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
