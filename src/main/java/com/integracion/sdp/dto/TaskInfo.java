package com.integracion.sdp.dto;

public class TaskInfo {
    private int id;
    private String slineIdRemedy;
    private String statusName;

    // Constructor, getters y setters


    public TaskInfo(int id, String slineIdRemedy, String statusName) {
        this.id = id;
        this.slineIdRemedy = slineIdRemedy;
        this.statusName = statusName;
    }

    public TaskInfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlineIdRemedy() {
        return slineIdRemedy;
    }

    public void setSlineIdRemedy(String slineIdRemedy) {
        this.slineIdRemedy = slineIdRemedy;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


    @Override
    public String toString() {
        return "TaskInfo{" +
                "id=" + id +
                ", slineIdRemedy='" + slineIdRemedy + '\'' +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}
