package com.tediproject.tedi.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;

    @Column
    private LocalDateTime date_posted;

    @Lob 
    @Column(length = 16777216)
    private String job_title;

    @Lob 
    @Column(length = 16777216)
    private String job_description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author") 
    private UserEntity author;

    @ManyToMany
    @JoinTable(name = "job_applicants",joinColumns = @JoinColumn(name = "job_id"),inverseJoinColumns = @JoinColumn(name = "applicant"))
    private List <UserEntity> applicants;



    public Job() {
        this.date_posted = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }


    public LocalDateTime getDate_posted() {
        return date_posted;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public List<UserEntity> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<UserEntity> applicants) {
        this.applicants = applicants;
    }


}
