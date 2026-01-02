
package Model;


public class UserModel {
    //TT đăng nhập
    private String userName;
    private String password;
    
    //TT cụ thể của người dùng trong chương trình
    private String hoTen;
    private String email;
    private String soDT;
    private String diaChi;
 
    
    public UserModel(String username, String password){
        this.userName = username;
        this.password = password;
    }
    
    public UserModel(String hoTen, String email, String soDT, String diaChi){
        this.hoTen = hoTen;
        this.email = email;
        this.soDT = soDT;
        this.diaChi = diaChi;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String gethoTen(){
        return hoTen;
    }
    
    public void sethoTen(String hoTen){
        this.hoTen = hoTen;
    }
    
    public String getemail(){
        return email;
    }
    
    public void setemail(String email){
        this.email = email;
    }
    
    public String getdiaChi(){
        return diaChi;
    }
    
    public void setdiaChi(String diaChi){
        this.diaChi = diaChi;
    }
    
    public String getsoDT(){
        return soDT;
    }
    
    public void setsoDT(String soDT){
        this.soDT = soDT;
    }
}
