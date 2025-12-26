package QuanLySinhVien.example.QuanLySinhVien.DTO;

public class SubjectDTO {
    private Long id;
    private String name;
    private String description;
    private Integer credits;
    private int status;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}