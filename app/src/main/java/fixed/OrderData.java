package fixed;

import inf.ItemInnerCancelOrderListener;
import inf.ItemInnerFinishedOrderListener;

public class OrderData {




    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private String time;
    private String servicePeople;
    private String issue;
    private String commitTime;
    private String solve;
//
    public OrderData(String id,String name,String phoneNumber,String address,String time,String servicePeople,String issue,String commitTime,String solve){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.time = time;
        this.servicePeople = servicePeople;
        this.issue = issue;
        this.commitTime = commitTime;
        this.solve = solve;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServicePeople() {
        return servicePeople;
    }

    public void setServicePeople(String servicePeople) {
        this.servicePeople = servicePeople;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getSolve() {
        return solve;
    }

    public void setSolve(String solve) {
        this.solve = solve;
    }
}
