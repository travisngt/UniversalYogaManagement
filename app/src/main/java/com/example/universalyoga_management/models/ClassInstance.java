package com.example.universalyoga_management.models;

public class ClassInstance {
    private long id;
    private long classId;
    private String instanceDate;
    private String teacher;
    private String comments;

    public ClassInstance(long id, long classId, String instanceDate, String teacher, String comments) {
        this.id = id;
        this.classId = classId;
        this.instanceDate = instanceDate;
        this.teacher = teacher;
        this.comments = comments;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getInstanceDate() {
        return instanceDate;
    }

    public void setInstanceDate(String instanceDate) {
        this.instanceDate = instanceDate;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
