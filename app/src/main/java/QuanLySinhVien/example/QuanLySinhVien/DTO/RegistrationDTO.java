package QuanLySinhVien.example.QuanLySinhVien.DTO;

import java.time.LocalDateTime;

public class RegistrationDTO {
    private Long id;
    private Long studentId;
    private Long subjectId;
    private String semester;
    private Integer academicYear;
    private Integer status;
    private Double grade;
    private LocalDateTime registrationDate;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusText() {
        switch (status) {
            case 1:
                return "Enrolling";
            case 2:
                return "Finished";
            case 3:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}