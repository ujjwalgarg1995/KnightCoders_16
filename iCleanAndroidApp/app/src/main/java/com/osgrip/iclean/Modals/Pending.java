package com.osgrip.iclean.Modals;

/**
 * Created by Pranjal on 15-Jan-16.
 */
public class Pending {
    private int id;
    String complain, address, time, ward, image,imgPath;

    public Pending(String complain, String address, String time, String ward, String image,String imgPath)
    {
        this.complain = complain;
        this.address = address;
        this.time = time;
        this.ward = ward;
        this.image = image;
        this.imgPath = imgPath;
    }
    public Pending(int id,String complain, String address, String time, String ward, String image,String imgPath)
    {
        this.id = id;
        this.complain = complain;
        this.address = address;
        this.time = time;
        this.ward = ward;
        this.image = image;
        this.imgPath = imgPath;
    }
    public int getId() { return this.id; }

    public String getComplain(){ return this.complain; }

    public String getImage() { return this.image; }
    public String getImgPath() { return this.imgPath; }
    public String getTime() { return  this.time; }
    public String getWard() { return  this.ward; }
    public String getAddress() {return  this.address; }
}
