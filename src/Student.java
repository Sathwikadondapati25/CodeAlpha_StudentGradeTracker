import java.time.Instant;

public class Student {
    private final String id;
    private String studentName;
    private int marks;
    private final Instant createdAt;

    public Student(String id, String studentName, int marks, Instant createdAt) {
        this.id = id;
        this.studentName = studentName;
        this.marks = marks;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getMarks() {
        return marks;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getGrade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 50) return "C";
        return "F";
    }

    public boolean isPass() {
        return marks >= 50;
    }
}
