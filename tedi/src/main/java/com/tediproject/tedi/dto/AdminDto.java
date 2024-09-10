package com.tediproject.tedi.dto;

import java.util.List;

import com.tediproject.tedi.types.CommentInfo;

public class AdminDto {
    
    private String firstName;
    private String lastName;
    private String email;
    private long phone_number;
    private String workTitle;
    private String workplace;
    private Long id;
    private byte[] profilePicture;

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    private List <CommentInfo> comments_posted;
    private List <String> articles_liked;
    private List <String> articles_posted;
    private List <String> jobs_posted;
    private List <String> connections;
    private List <String> skills;
    private List <String> education;
    private List <String> experience;

    

    public AdminDto() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CommentInfo> getComments_posted() {
        return comments_posted;
    }

    public void setComments_posted(List<CommentInfo> comments_posted) {
        this.comments_posted = comments_posted;
    }

    public List<String> getArticles_liked() {
        return articles_liked;
    }

    public void setArticles_liked(List<String> articles_liked) {
        this.articles_liked = articles_liked;
    }

    public List<String> getArticles_posted() {
        return articles_posted;
    }

    public void setArticles_posted(List<String> articles_posted) {
        this.articles_posted = articles_posted;
    }

    public List<String> getJobs_posted() {
        return jobs_posted;
    }

    public void setJobs_posted(List<String> jobs_posted) {
        this.jobs_posted = jobs_posted;
    }

    public List<String> getConnections() {
        return connections;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getExperience() {
        return experience;
    }

    public void setExperience(List<String> experience) {
        this.experience = experience;
    }




}