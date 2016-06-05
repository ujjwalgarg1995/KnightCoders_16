package com.osgrip.iclean.Modals;

/**
 * Created by Pranjal on 12-Jan-16.
 */
public class Complains {
    private int id;
    String complain, complain_id, address,mobileNo, time, areaCode, image, status, comment;
    int volid;
    public Complains(String complain, String complain_id, String mobileNo, String time, String areaCode, String image, String status, String comment, int volid, String address)
    {
        this.complain = complain;
        this.complain_id = complain_id;
        this.address = address;
        this.time = time;
        this.areaCode = areaCode;
        this.image = image;
        this.status = status;
        this.comment = comment;
        this.volid = volid;
        this.mobileNo = mobileNo;
    }
    public Complains(int id, String complain, String complain_id, String mobileNo, String time, String areaCode, String image, String status, String comment, int volid, String address)
    {
        this.id = id;
        this.complain = complain;
        this.complain_id = complain_id;
        this.address = address;
        this.time = time;
        this.areaCode = areaCode;
        this.image = image;
        this.status = status;
        this.comment = comment;
        this.volid = this.volid;
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public int getVolid() {
        return volid;
    }

    public int getId()
    {
        return this.id;
    }


    public String getComplain()
    {
        return this.complain;
    }

    public String getComplain_id()
    {
        return this.complain_id;
    }
    public String getImage()
    {
        return this.image;
    }
    public String getTime() {return  this.time; }
    public String getAreaCode() {return  this.areaCode; }
    public String getAddress() {return  this.address; }
    public String getStatus() {return  this.status; }
    public String getComment() {return  this.comment; }


}
