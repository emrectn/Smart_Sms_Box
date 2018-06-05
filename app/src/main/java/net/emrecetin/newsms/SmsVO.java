package net.emrecetin.newsms;

public class SmsVO{
    private String  _id;
    private String _address;
    private String _msg;
    private String _time;
    private String _folderName;
    private double latitude;
    private double longitude;
    private int status;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int type;

    public String getId(){
        return _id;
    }
    public String getMsg(){
        return _msg;
    }
    public String getAddress(){
        return _address;
    }
    public String getTime(){
        return _time;
    }
    public String getFolderName(){
        return _folderName;
    }
    public int getType() {
        return type;
    }

    public void setId(String id){
        _id = id;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setTime(String time){
        _time = time;
    }
    public void setFolderName(String folderName){
        _folderName = folderName;
    }

    public String toString() {
        return "Address : " + getAddress() + "\nMsg : " + getMsg() + "\nTime : " + getTime() + "\nType : " + getType();
    }
}
