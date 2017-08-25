package xinshuyuan.com.wrongtitlebook.Model;

/**
 * 综合素质评价学生信息
 * Created by wjt on 2017/8/21.
 */

public class StudentInfo {
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String studentName;
    private Long id;
}
