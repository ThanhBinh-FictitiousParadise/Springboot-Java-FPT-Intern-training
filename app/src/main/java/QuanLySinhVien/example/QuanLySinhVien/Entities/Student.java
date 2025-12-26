package QuanLySinhVien.example.QuanLySinhVien.Entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Integer status;  // 0 for deleted, 1 for active

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String contact;

    @Column(nullable = true)
    private String imageFilename;

    // Constructors
    public Student() {}

    public Student(String sid, String name, String email, LocalDate dateOfBirth, Integer status, String address, String contact) {
        this.sid = sid;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.address = address;
        this.contact = contact;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public boolean isActive() {
        return status == 1;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", sid='" + sid + "', name='" + name + "', email='" + email + "', dateOfBirth=" + dateOfBirth + ", status=" + status + ", address='" + address + "', contact='" + contact + "', imageFilename='" + imageFilename + "'}";
    }
}