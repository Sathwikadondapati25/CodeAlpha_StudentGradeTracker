import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class StudentService {
    private final ArrayList<Student> students = new ArrayList<>();

    public void addStudent(String name, int marks) {
        students.add(new Student(UUID.randomUUID().toString(), name, marks, Instant.now()));
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    public void clearAll() {
        students.clear();
    }

    public void removeAt(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
        }
    }

    public Student getById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    public boolean updateStudent(String id, String newName, int newMarks) {
        Student s = getById(id);
        if (s == null) return false;
        s.setStudentName(newName);
        s.setMarks(newMarks);
        return true;
    }

    public int getCount() {
        return students.size();
    }

    public int getTotalMarks() {
        int total = 0;
        for (Student s : students) total += s.getMarks();
        return total;
    }

    public double getAverageMarks() {
        if (students.isEmpty()) return 0.0;
        return (double) getTotalMarks() / students.size();
    }

    public int getHighestMarks() {
        if (students.isEmpty()) return 0;
        int best = Integer.MIN_VALUE;
        for (Student s : students) best = Math.max(best, s.getMarks());
        return best;
    }

    public int getLowestMarks() {
        if (students.isEmpty()) return 0;
        int low = Integer.MAX_VALUE;
        for (Student s : students) low = Math.min(low, s.getMarks());
        return low;
    }

    public Student getTopper() {
        if (students.isEmpty()) return null;
        return students.stream().max(Comparator.comparingInt(Student::getMarks)).orElse(null);
    }

    public Student getLowestScorer() {
        if (students.isEmpty()) return null;
        return students.stream().min(Comparator.comparingInt(Student::getMarks)).orElse(null);
    }

    public double getPassPercentage() {
        if (students.isEmpty()) return 0.0;
        int pass = 0;
        for (Student s : students) if (s.isPass()) pass++;
        return (pass * 100.0) / students.size();
    }

    public GradeCounts getGradeCounts() {
        int a = 0, b = 0, c = 0, f = 0;
        for (Student s : students) {
            switch (s.getGrade()) {
                case "A": a++; break;
                case "B": b++; break;
                case "C": c++; break;
                default: f++;
            }
        }
        return new GradeCounts(a, b, c, f);
    }

    public static final class GradeCounts {
        public final int a, b, c, f;
        public GradeCounts(int a, int b, int c, int f) {
            this.a = a; this.b = b; this.c = c; this.f = f;
        }
    }
}
